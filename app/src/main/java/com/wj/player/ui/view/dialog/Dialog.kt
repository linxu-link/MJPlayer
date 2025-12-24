package com.wj.player.ui.view.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.wj.player.ui.theme.colors.LocalColorScheme
import com.wj.player.ui.theme.textstyle.LocalTypography
import com.wj.player.ui.view.TextCaption
import com.wujia.toolkit.compose.noRippleClickable


@Composable
fun MasterDialog(
    title: String,
    body: String,
    leftButtonText: String,
    rightButtonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = true,
        ),
    ) {
        Box(
            modifier = Modifier
                .width(270.dp)
                .background(LocalColorScheme.current.surface, shape = RoundedCornerShape(12.dp))
                .padding(24.dp),
        ) {
            Column {
                Text(
                    text = title,
                    color = LocalColorScheme.current.accent,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    style = LocalTypography.current.title,
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = body,
                    color = LocalColorScheme.current.textSecondary,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    style = LocalTypography.current.caption,
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextCaption(
                        text = leftButtonText,
                        color = LocalColorScheme.current.textSecondary,
                        modifier = Modifier.noRippleClickable { onDismiss() },
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    TextCaption(
                        text = rightButtonText,
                        color = LocalColorScheme.current.accent,
                        modifier = Modifier.noRippleClickable { onConfirm() },
                    )
                }
            }
        }
    }
}
