package com.wj.player.ui.pager.videolist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wj.player.data.source.local.video.room.VideoEntity
import com.wj.player.ui.view.TextCaption
import com.wj.player.utils.VideoTimeUtils


@Composable
fun VideoGroupItemTitle(
    modifier: Modifier,
    videoSize: Int,
    date: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        TextCaption(text = date)

        TextCaption(text = "|", modifier = Modifier.padding(horizontal = 4.dp))

        TextCaption(text = "$videoSize 项")
    }
}


@Composable
fun VideoItemDesc(
    videoSize: Long,
    videoUpdateTime: Long,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        TextCaption(text = VideoTimeUtils.formatVideoSize(videoSize))

        TextCaption(text = "|", modifier = Modifier.padding(horizontal = 4.dp))

        TextCaption(text = VideoTimeUtils.formatVideoSimpleDate(videoUpdateTime))
    }
}

@Preview(showBackground = true)
@Composable
private fun VideoItemDescPreview() {
    Column() {
        VideoGroupItemTitle(
            modifier = Modifier,
            videoSize = 100,
            date = "2023-01-01",
        )

        VideoItemDesc(
            videoSize = 1024 * 1024 * 100,
            videoUpdateTime = System.currentTimeMillis(),
        )
    }
}
