package com.wj.player.ui.view.player.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class PlayerUiConfig private constructor(
    val showControls: Boolean,
    val showPlayPause: Boolean,
    val showSeekBar: Boolean,
    val showFullscreen: Boolean,
    val showSpeedControl: Boolean,
    val showTimeDisplay: Boolean,
    val showBufferingIndicator: Boolean,
    val controlsBackgroundColor: Color,
    val controlsContentColor: Color,
    val playIcon: @Composable () -> Unit,
    val pauseIcon: @Composable () -> Unit,
    val fullscreenIcon: @Composable () -> Unit,
    val exitFullscreenIcon: @Composable () -> Unit,
    val forwardIcon: @Composable () -> Unit,
    val backwardIcon: @Composable () -> Unit,
) {

    class Builder {
        private var showControls: Boolean = true
        private var showPlayPause: Boolean = true
        private var showSeekBar: Boolean = true
        private var showFullscreen: Boolean = true
        private var showSpeedControl: Boolean = false
        private var showTimeDisplay: Boolean = true
        private var showBufferingIndicator: Boolean = true
        private var controlsBackgroundColor: Color = Color.Black.copy(alpha = 0.5f)
        private var controlsContentColor: Color = Color.White
        private var playIcon: @Composable () -> Unit = {
            Icon(Icons.Default.PlayArrow, "Play", tint = controlsContentColor)
        }
        private var pauseIcon: @Composable () -> Unit = {
            Icon(Icons.Default.PlayArrow, "Pause", tint = controlsContentColor)
        }
        private var fullscreenIcon: @Composable () -> Unit = {
            Icon(Icons.Default.PlayArrow, "Fullscreen", tint = controlsContentColor)
        }
        private var exitFullscreenIcon: @Composable () -> Unit = {
            Icon(Icons.Default.PlayArrow, "Exit Fullscreen", tint = controlsContentColor)
        }
        private var forwardIcon: @Composable () -> Unit = {
            Icon(Icons.Default.PlayArrow, "Forward", tint = controlsContentColor)
        }
        private var backwardIcon: @Composable () -> Unit = {
            Icon(Icons.Default.PlayArrow, "Backward", tint = controlsContentColor)
        }

        fun showControls(show: Boolean) = apply { this.showControls = show }
        fun showPlayPause(show: Boolean) = apply { this.showPlayPause = show }
        fun showSeekBar(show: Boolean) = apply { this.showSeekBar = show }
        fun showFullscreen(show: Boolean) = apply { this.showFullscreen = show }
        fun showSpeedControl(show: Boolean) = apply { this.showSpeedControl = show }
        fun showTimeDisplay(show: Boolean) = apply { this.showTimeDisplay = show }
        fun showBufferingIndicator(show: Boolean) = apply { this.showBufferingIndicator = show }
        fun controlsBackgroundColor(color: Color) = apply { this.controlsBackgroundColor = color }
        fun controlsContentColor(color: Color) = apply { this.controlsContentColor = color }
        fun playIcon(icon: @Composable () -> Unit) = apply { this.playIcon = icon }
        fun pauseIcon(icon: @Composable () -> Unit) = apply { this.pauseIcon = icon }
        fun fullscreenIcon(icon: @Composable () -> Unit) = apply { this.fullscreenIcon = icon }
        fun exitFullscreenIcon(icon: @Composable () -> Unit) = apply { this.exitFullscreenIcon = icon }
        fun forwardIcon(icon: @Composable () -> Unit) = apply { this.forwardIcon = icon }
        fun backwardIcon(icon: @Composable () -> Unit) = apply { this.backwardIcon = icon }

        fun build() = PlayerUiConfig(
            showControls = showControls,
            showPlayPause = showPlayPause,
            showSeekBar = showSeekBar,
            showFullscreen = showFullscreen,
            showSpeedControl = showSpeedControl,
            showTimeDisplay = showTimeDisplay,
            showBufferingIndicator = showBufferingIndicator,
            controlsBackgroundColor = controlsBackgroundColor,
            controlsContentColor = controlsContentColor,
            playIcon = playIcon,
            pauseIcon = pauseIcon,
            fullscreenIcon = fullscreenIcon,
            exitFullscreenIcon = exitFullscreenIcon,
            forwardIcon = forwardIcon,
            backwardIcon = backwardIcon,
        )
    }
}
