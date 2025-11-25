package com.wj.player.ui.view

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wj.player.R
import com.wj.player.ui.theme.colors.LocalColorScheme
import com.wj.player.ui.theme.size.LocalTypography
import com.wujia.toolkit.utils.ktx.loadVideoThumbnailNative

@Composable
fun TextTitle(
    text: String,
    color: Color = LocalColorScheme.current.textPrimary,
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        softWrap = false,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = LocalTypography.current.title,
        color = color,
    )
}

@Composable
fun TextBody(
    text: String,
    color: Color = LocalColorScheme.current.textPrimary,
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        softWrap = false,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = LocalTypography.current.body,
        color = color,
    )
}

@Composable
fun TextCaption(
    text: String,
    color: Color = LocalColorScheme.current.textSecondary,
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        softWrap = false,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = LocalTypography.current.caption,
        color = color,
    )
}

@Composable
fun ImageVideo(
    videoTitle: String,
    videoId: Long,
    videoPath: String,
    thumbnailPath: String,
    modifier: Modifier = Modifier,
) {
    var isLoading by remember { mutableStateOf(true) }
    var thumbnail by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(videoId, videoPath, thumbnailPath) {
        isLoading = true
        thumbnail = loadVideoThumbnailNative(
            videoId = videoId,
            videoPath = videoPath,
            thumbnailPath = thumbnailPath,
        )
        isLoading = false
    }

    Image(
        painter = when {
            isLoading -> painterResource(id = R.drawable.ic_loading)
            else -> thumbnail?.let { BitmapPainter(it.asImageBitmap()) }
                ?: painterResource(id = R.drawable.ic_loading)
        },
        contentDescription = videoTitle,
        modifier = modifier
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(4.dp)),
        contentScale = ContentScale.Crop,
    )
}

@Preview(showBackground = true)
@Composable
private fun Demo() {
    Column() {
        TextTitle("这是一个标题")
        TextBody("这是一个正文")
        TextCaption("这是一个caption")
    }
}
