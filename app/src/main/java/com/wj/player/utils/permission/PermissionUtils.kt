package com.wj.player.utils.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.activity.compose.rememberLauncherForActivityResult
import com.wj.player.R
import com.wujia.toolkit.HiAppGlobal
import com.wujia.toolkit.jetpack.datastore.HiDataStoreManager
import com.wujia.toolkit.utils.HiLog
import kotlinx.coroutines.flow.first

private const val PREF_KEY_PERMISSION_REQUESTED = "permission_requested"

object PermissionUtils {

    fun hasPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED
    }

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

    fun getPermissionDisplayName(permission: String): String {
        return when (permission) {
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                -> HiAppGlobal.getApplication().getString(R.string.permission_video)

            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                -> HiAppGlobal.getApplication().getString(R.string.permission_storage)

            else -> ""
        }
    }

    // 检查是否需要显示权限说明（原生判断逻辑）
    fun shouldShowRequestPermissionRationale(context: Context, permission: String): Boolean {
        // 注意：需要Activity上下文才能正确判断，这里简化实现
        return context is android.app.Activity &&
                context.shouldShowRequestPermissionRationale(permission)
    }
}

@Composable
fun MultiplePermissionsRequest(
    permissions: List<String>,
    onAllGranted: () -> Unit,
    onDenied: (List<String>) -> Unit,
) {
    val context = LocalContext.current
    var showRationaleDialog by remember { mutableStateOf(false) }
    var shouldShowRationale by remember { mutableStateOf(false) }
    var hasRequestedBefore by remember { mutableStateOf(false) }
    var currentDeniedPermissions by remember { mutableStateOf(emptyList<String>()) }

    // 原生权限请求 launcher
    val permissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionResults ->
        val denied = permissionResults.filter { !it.value }.keys.toList()

        if (denied.isEmpty()) {
            onAllGranted()
        } else {
            currentDeniedPermissions = denied
            // 检查是否需要显示rationale
            shouldShowRationale = denied.any {
                PermissionUtils.shouldShowRequestPermissionRationale(context, it)
            }
            showRationaleDialog = true
            onDenied(denied)
        }
    }

    // 初始化逻辑
    LaunchedEffect(Unit) {
        // 检查是否已全部授权
        val allGranted = permissions.all { PermissionUtils.hasPermission(context, it) }
        if (allGranted) {
            onAllGranted()
            return@LaunchedEffect
        }

        hasRequestedBefore = HiDataStoreManager.getInstance(context).getPreference(PREF_KEY_PERMISSION_REQUESTED, false).first()

        // 首次进入检查未授权权限
        val initiallyDenied = permissions.filter { !PermissionUtils.hasPermission(context, it) }
        if (initiallyDenied.isNotEmpty()) {
            currentDeniedPermissions = initiallyDenied
            showRationaleDialog = true
        }
    }

    // 权限说明弹窗
    if (showRationaleDialog) {
        val permissionNames = currentDeniedPermissions.joinToString("、") {
            PermissionUtils.getPermissionDisplayName(it)
        }

        PermissionRationaleDialog(
            message = stringResource(R.string.permission_rationale_message, permissionNames),
            onConfirm = {
                HiDataStoreManager.getInstance(context).putPreference(PREF_KEY_PERMISSION_REQUESTED, true)
                showRationaleDialog = false

                if (shouldShowRationale || !hasRequestedBefore) {
                    // 发起权限请求
                    permissionLauncher.launch(currentDeniedPermissions.toTypedArray())
                } else {
                    // 永久拒绝，跳设置页
                    PermissionUtils.openAppSettings(context)
                }
            },
            onDismiss = {
                showRationaleDialog = false
                if (currentDeniedPermissions.isNotEmpty()) {
                    onDenied(currentDeniedPermissions)
                }
            },
        )
    }
}

@Composable
fun PermissionRationaleDialog(
    title: String = stringResource(R.string.permission_rationale_title),
    message: String = stringResource(R.string.permission_rationale_message),
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    text = message,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel))
                    }
                    TextButton(onClick = onConfirm) {
                        Text(stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}
