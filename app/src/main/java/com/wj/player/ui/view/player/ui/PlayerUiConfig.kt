package com.wj.player.ui.view.player.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CropRotate
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PauseCircleFilled
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.PlayCircleFilled
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.wj.player.R

class PlayerUiConfig private constructor(
    val showCenterBar: Boolean,
    val showBottomSeekBar: Boolean,
    val showSpeedControl: Boolean,

    val showBufferingIndicator: Boolean,
    val controlsBackgroundColor: Color,
    val controlsContentColor: Color,
    val title: String,

    val backIcon: @Composable () -> Unit,
    val speedIcon: @Composable () -> Unit,
    val playIcon: @Composable () -> Unit,
    val pauseIcon: @Composable () -> Unit,
    val rotateIcon: @Composable () -> Unit,
    val lockIcon: @Composable () -> Unit,
    val unlockIcon: @Composable () -> Unit,
    val forwardIcon: @Composable () -> Unit,
    val backwardIcon: @Composable () -> Unit,

    val speedOptions: List<SheetOption>,
) {

    class Builder {
        private var showCenterBar: Boolean = true
        private var showBottomSeekBar: Boolean = true
        private var showSpeedControl: Boolean = false
        private var showBufferingIndicator: Boolean = true
        private var controlsBackgroundColor: Color = Color.Black.copy(alpha = 0.5f)
        private var controlsContentColor: Color = Color.White

        private var backIcon: @Composable () -> Unit = {
            Icon(Icons.Default.ArrowBack, "Back", tint = controlsContentColor)
        }
        private var playIcon: @Composable () -> Unit = {
            Icon(Icons.Rounded.PlayArrow, "Play", tint = controlsContentColor)
        }
        private var pauseIcon: @Composable () -> Unit = {
            Icon(Icons.Rounded.Pause, "Pause", tint = controlsContentColor)
        }
        private var speedIcon: @Composable () -> Unit = {
            Icon(Icons.Default.Speed, "Speed", tint = controlsContentColor)
        }
        private var rotateIcon: @Composable () -> Unit = {
            Icon(Icons.Default.CropRotate, "Rotate", tint = controlsContentColor)
        }
        private var lockIcon: @Composable () -> Unit = {
            Icon(Icons.Default.Lock, "Lock", tint = controlsContentColor)
        }
        private var unlockIcon: @Composable () -> Unit = {
            Icon(Icons.Default.LockOpen, "Unlock", tint = controlsContentColor)
        }
        private var forwardIcon: @Composable () -> Unit = {
            Icon(Icons.Default.PlayArrow, "Forward", tint = controlsContentColor)
        }
        private var backwardIcon: @Composable () -> Unit = {
            Icon(Icons.Default.PlayArrow, "Backward", tint = controlsContentColor)
        }

        private var speedOptions: List<SheetOption> = emptyList()
        private var title: String = ""

        fun setTitle(title: String) = apply { this.title = title }
        fun setSpeedOptions(options: List<SheetOption>) = apply { this.speedOptions = options }

        fun showCenterBar(show: Boolean) = apply { this.showCenterBar = show }
        fun showBottomSeekBar(show: Boolean) = apply { this.showBottomSeekBar = show }
        fun showSpeedControl(show: Boolean) = apply { this.showSpeedControl = show }
        fun showBufferingIndicator(show: Boolean) = apply { this.showBufferingIndicator = show }

        fun controlsBackgroundColor(color: Color) = apply { this.controlsBackgroundColor = color }
        fun controlsContentColor(color: Color) = apply { this.controlsContentColor = color }

        fun backIcon(icon: @Composable () -> Unit) = apply { this.backIcon = icon }
        fun speedIcon(icon: @Composable () -> Unit) = apply { this.speedIcon = icon }
        fun playIcon(icon: @Composable () -> Unit) = apply { this.playIcon = icon }
        fun pauseIcon(icon: @Composable () -> Unit) = apply { this.pauseIcon = icon }
        fun rotateIcon(icon: @Composable () -> Unit) = apply { this.rotateIcon = icon }
        fun lockIcon(icon: @Composable () -> Unit) = apply { this.lockIcon = icon }
        fun unlockIcon(icon: @Composable () -> Unit) = apply { this.unlockIcon = icon }
        fun forwardIcon(icon: @Composable () -> Unit) = apply { this.forwardIcon = icon }
        fun backwardIcon(icon: @Composable () -> Unit) = apply { this.backwardIcon = icon }

        fun build() = PlayerUiConfig(
            showCenterBar = showCenterBar,
            showBottomSeekBar = showBottomSeekBar,
            showSpeedControl = showSpeedControl,
            showBufferingIndicator = showBufferingIndicator,

            controlsBackgroundColor = controlsBackgroundColor,
            controlsContentColor = controlsContentColor,
            playIcon = playIcon,
            pauseIcon = pauseIcon,
            rotateIcon = rotateIcon,
            forwardIcon = forwardIcon,
            backwardIcon = backwardIcon,
            backIcon = backIcon,
            speedIcon = speedIcon,
            lockIcon = lockIcon,
            unlockIcon = unlockIcon,

            title = title,
            speedOptions = speedOptions,
        )
    }
}
