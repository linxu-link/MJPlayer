package com.wj.player

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class TestActivity : ComponentActivity() {

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 步骤 1: 禁用自动适配
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MaterialTheme {  // 可替换为你的主题
                Surface(modifier = Modifier.fillMaxSize()) {
                    SystemBarsHandler()
                }
            }
        }
    }

    @Composable
    fun SystemBarsHandler() {
        val view = LocalView.current
        val density = LocalDensity.current
        var statusBarHeight: Dp by remember { mutableStateOf(0.dp) }
        var navigationBarHeight: Dp by remember { mutableStateOf(0.dp) }

        // 初始化 insetsController
        val insetsController = remember(view) {
            WindowCompat.getInsetsController(window, view).apply {
                // 可选: 设置沉浸式行为（滑动临时显示）
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

        // 使用 LaunchedEffect 在首次 composition 后捕获初始 insets 并隐藏
        LaunchedEffect(Unit) {
            if (statusBarHeight == 0.dp && navigationBarHeight == 0.dp) {
                // 捕获初始 insets（栏可见时）
                val systemBarsInsets = WindowInsets.systemBars.getInsets(WindowInsetsCompat.Type.systemBars())
                statusBarHeight = with(density) { systemBarsInsets.top.toDp() }
                navigationBarHeight = with(density) { systemBarsInsets.bottom.toDp() }

                // 初始隐藏系统栏
                insetsController.hide(WindowInsetsCompat.Type.systemBars())
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                // 应用固定 padding
                .padding(top = statusBarHeight, bottom = navigationBarHeight)
                // 消费当前 insets（隐藏后为 0，防止进一步传播）
                .consumeWindowInsets(WindowInsets.systemBars)
                // 添加点击事件
                .clickable {
                    // 显示系统栏
                    insetsController.show(WindowInsetsCompat.Type.systemBars())

                    // 移除任何 pending 的隐藏任务
                    handler.removeCallbacksAndMessages(null)

                    // 10秒后隐藏
                    handler.postDelayed({
                        insetsController.hide(WindowInsetsCompat.Type.systemBars())
                    }, 10000)
                }
        ) {
            // 你的内容 Composable，例如：
            Text("点击屏幕显示系统栏，10秒后隐藏")
        }
    }
}

}
