package com.wj.player.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.wj.player.R
import com.wujia.toolkit.jetpack.datastore.HiDataStoreManager
import com.wujia.toolkit.utils.HiLog
import kotlinx.coroutines.flow.first

private const val PREF_KEY_PERMISSION_REQUESTED = "permission_requested"

object PermissionUtils {

    /**
     * 检查单个权限是否已授予
     */
    fun hasPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) ==
                android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    /**
     * 跳转到应用设置页（权限被永久拒绝时）
     */
    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        runCatching {
            context.startActivity(intent)
        }.onFailure {
            HiLog.e("openAppSettings error $it")
        }
    }

    // 获取权限名称（用于提示）
    fun getPermissionDisplayName(permission: String): String {
        return when (permission) {
            android.Manifest.permission.READ_MEDIA_VIDEO,
            android.Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                -> "视频"

            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                -> "存储"

            android.Manifest.permission.ACCESS_FINE_LOCATION -> "位置"
            else -> ""
        }
    }
}

/**
 * Compose多权限请求组件
 * @param permissions 目标权限列表（如Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE）
 * @param onAllGranted 所有权限授予回调
 * @param onDenied 任意权限拒绝回调
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MultiplePermissionsRequest(
    permissions: List<String>,
    onAllGranted: () -> Unit,
    onDenied: (List<String>) -> Unit,
) {
    val context = LocalContext.current
    val multiplePermissionsState = rememberMultiplePermissionsState(
        permissions = permissions,
        onPermissionsResult = { result: Map<String, Boolean> ->
            val deniedPermissions = result.filter { !it.value }
            if (deniedPermissions.isNotEmpty()) {
                onDenied(deniedPermissions.map { it.key })
            } else {
                onAllGranted()
            }
        },
    )

    // 状态：是否正在显示Rationale弹窗
    var showRationaleDialog by remember { mutableStateOf(false) }
    var shouldShowRationale by remember { mutableStateOf(false) }
    // 记录是否已经请求过权限
    var hasRequestedBefore by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        HiLog.e("MultiplePermissionsRequest1 ${multiplePermissionsState.permissions.map { it.permission }}")
        // 如果已经全部授予，直接回调
        if (multiplePermissionsState.allPermissionsGranted) {
            onAllGranted()
            return@LaunchedEffect
        }

        hasRequestedBefore = HiDataStoreManager.getInstance(context)
            .getPreference(PREF_KEY_PERMISSION_REQUESTED, false).first()

        // 检查是否需要显示Rationale（仅在未显示弹窗时）
        if (!showRationaleDialog) {
            shouldShowRationale = multiplePermissionsState.permissions.any {
                HiLog.e("MultiplePermissionsRequest${it.permission} ${it.status.shouldShowRationale} ${it.status.isGranted}")
                it.status.shouldShowRationale
            }
            val deniedPermissions = multiplePermissionsState.permissions.any {
                !it.status.isGranted
            }
            HiLog.e("MultiplePermissionsRequest $shouldShowRationale $deniedPermissions")
            if (deniedPermissions) {
                showRationaleDialog = true
            }
        }
    }

    // ========== Rationale弹窗 ==========
    if (showRationaleDialog) {
        val rationalePermissions = multiplePermissionsState.permissions
            .filter { it.status.shouldShowRationale && !it.status.isGranted }
            .map { it.permission }

        val permissionNames = rationalePermissions.joinToString("、") {
            PermissionUtils.getPermissionDisplayName(it)
        }

        PermissionRationaleDialog(
            message = stringResource(R.string.permission_rationale_message, permissionNames),
            onConfirm = {
                HiDataStoreManager.getInstance(context)
                    .putPreference(PREF_KEY_PERMISSION_REQUESTED, true)
                showRationaleDialog = false
                if (shouldShowRationale) {
                    // 发起权限请求
                    multiplePermissionsState.launchMultiplePermissionRequest()
                } else {
                    if (hasRequestedBefore) {
                        PermissionUtils.openAppSettings(context)
                    } else {
                        // 首次请求权限
                        multiplePermissionsState.launchMultiplePermissionRequest()
                    }
                }
            },
            onDismiss = {
                showRationaleDialog = false
                // 用户取消弹窗，视为拒绝
                val deniedPermissions = multiplePermissionsState.permissions
                    .filter { !it.status.isGranted }
                    .map { it.permission }
                if (deniedPermissions.isNotEmpty()) {
                    onDenied(deniedPermissions)
                }
            },
        )
    }
}

/**
 * 权限说明弹窗（解释为何需要权限）
 */
@Composable
fun PermissionRationaleDialog(
    title: String = stringResource(R.string.permission_rationale_title),
    message: String = stringResource(R.string.permission_rationale_message),
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = androidx.compose.ui.Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            androidx.compose.foundation.layout.Column(
                modifier = androidx.compose.ui.Modifier.padding(16.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    text = message,
                    modifier = androidx.compose.ui.Modifier.padding(vertical = 8.dp),
                )
                androidx.compose.foundation.layout.Row(
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End,
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("取消")
                    }
                    TextButton(onClick = onConfirm) {
                        Text("确定")
                    }
                }
            }
        }
    }
}

