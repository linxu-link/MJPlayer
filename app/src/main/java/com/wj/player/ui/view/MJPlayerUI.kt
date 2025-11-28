package com.wj.player.ui.view

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wj.player.R
import com.wj.player.ui.theme.colors.LocalColorScheme
import com.wj.player.ui.theme.dimens.LocalAppDimensions
import com.wj.player.ui.theme.textstyle.LocalTypography
import com.wujia.toolkit.utils.ktx.loadVideoThumbnailNative

@Composable
fun TextTitle(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = LocalColorScheme.current.textPrimary,
    textAlign: TextAlign = TextAlign.Start,
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
    modifier: Modifier = Modifier,
    text: String,
    color: Color = LocalColorScheme.current.textPrimary,
    textAlign: TextAlign = TextAlign.Start,
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        overflow = TextOverflow.Ellipsis,
        style = LocalTypography.current.body,
        color = color,
    )
}

@Composable
fun TextCaption(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = LocalColorScheme.current.textSecondary,
    textAlign: TextAlign = TextAlign.Start,
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
fun TextCaption(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    color: Color = LocalColorScheme.current.textSecondary,
    textAlign: TextAlign = TextAlign.Start,
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
fun TextSmall(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = LocalColorScheme.current.textSecondary,
    textAlign: TextAlign = TextAlign.Start,
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        softWrap = false,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = LocalTypography.current.small,
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
            isLoading -> painterResource(id = R.drawable.ic_loading_dark)
            else -> thumbnail?.let { BitmapPainter(it.asImageBitmap()) }
                ?: painterResource(id = R.drawable.ic_loading_dark)
        },
        contentDescription = videoTitle,
        modifier = modifier
            .aspectRatio(16f / 11f)
            .clip(RoundedCornerShape(4.dp)),
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun IconTint(
    imageVector: ImageVector,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    tint: Color = LocalColorScheme.current.textPrimary,
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint,
    )
}

@Preview(showBackground = true)
@Composable
private fun Demo() {
    Column() {
        TextTitle(text = "这是一个标题")
        TextBody(text = "这是一个正文")
        TextCaption(text = "这是一个caption")
        TextSmall(text = "这是一个small")
    }
}
