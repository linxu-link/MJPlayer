package com.wj.player.data.entity

import androidx.annotation.IntDef

const val LAYOUT_TYPE_LIST = 0
const val LAYOUT_TYPE_GRID = 1

@IntDef(
    LAYOUT_TYPE_LIST,
    LAYOUT_TYPE_GRID,
)
@Retention(AnnotationRetention.SOURCE)
annotation class LayoutType
