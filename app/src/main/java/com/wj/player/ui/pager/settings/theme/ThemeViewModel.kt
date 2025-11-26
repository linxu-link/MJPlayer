package com.wj.player.ui.pager.settings.theme

import androidx.lifecycle.viewModelScope
import com.wj.player.MJConstants
import com.wj.player.arch.ArchViewModel
import com.wj.player.data.entity.ThemeEntity
import com.wj.player.ui.theme.ThemeType
import com.wj.player.utils.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class ThemeUiState(
    val androidThemes: List<ThemeEntity> = emptyList(),
    val classicThemes: List<ThemeEntity> = emptyList(),
    val otherThemes: List<ThemeEntity> = emptyList(),
)

@HiltViewModel
class SettingViewModel @Inject constructor() : ArchViewModel() {

    private val _androidThemes = MutableStateFlow<List<ThemeEntity>>(emptyList())
    private val _classicThemes = MutableStateFlow<List<ThemeEntity>>(emptyList())
    private val _otherThemes = MutableStateFlow<List<ThemeEntity>>(emptyList())

    // 对外暴露的UI状态（聚合所有状态流）
    val uiState: StateFlow<ThemeUiState> = combine(
        _androidThemes,
        _classicThemes,
        _otherThemes,
    ) { androidThemes, classicThemes, otherThemes ->
        ThemeUiState(
            androidThemes = androidThemes,
            classicThemes = classicThemes,
            otherThemes = otherThemes,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = ThemeUiState(),
    )

    init {
        _androidThemes.value = MJConstants.Theme.androidThemes
        _classicThemes.value = MJConstants.Theme.classicThemes
        _otherThemes.value = MJConstants.Theme.otherThemes

        MJConstants.Theme.addThemeListener { themeType ->
            _otherThemes.value = _otherThemes.value.map {
                it.copy(isSelected = it.themeType == themeType)
            }
            _classicThemes.value = _classicThemes.value.map {
                it.copy(isSelected = it.themeType == themeType)
            }
            _androidThemes.value = _androidThemes.value.map {
                it.copy(isSelected = it.themeType == themeType)
            }
        }
    }

    fun selectTheme(themeType: ThemeType) {
        _otherThemes.value = _otherThemes.value.map {
            it.copy(isSelected = it.themeType == themeType)
        }
        _classicThemes.value = _classicThemes.value.map {
            it.copy(isSelected = it.themeType == themeType)
        }
        _androidThemes.value = _androidThemes.value.map {
            it.copy(isSelected = it.themeType == themeType)
        }
        MJConstants.Theme.setThemeType(themeType)
    }

}
