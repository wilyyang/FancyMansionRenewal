package com.fancymansion.presentation.viewer.content

import com.fancymansion.core.common.const.PageTheme
import com.fancymansion.core.presentation.base.ViewEvent
import com.fancymansion.core.presentation.base.ViewSideEffect
import com.fancymansion.core.presentation.base.ViewState
import com.fancymansion.domain.model.book.PageSettingModel
import com.fancymansion.domain.model.book.SelectorModel
import java.io.File

class ViewerContentContract {
    companion object {
        const val NAME = "viewer_content"
    }

    data class State(
        val bookTitle: String = "",
        val episodeTitle: String = "",
        val pageSetting: PageSettingModel = PageSettingModel(),
        val pageWrapper: PageWrapper? = null,
        val selectors: List<SelectorModel> = listOf()
    ) : ViewState

    sealed class Event : ViewEvent {
        data class OnConfirmMoveSavePageDialog(val pageId: Long) : Event()
        data object OnCancelMoveSavePageDialog : Event()
        data class OnClickSelector(val pageId: Long, val selectorId: Long) : Event()

        sealed class SettingEvent : Event() {
            data class ChangeSettingPageTheme(val pageTheme: PageTheme) : SettingEvent()

            data object IncrementSettingContentTextSize : SettingEvent()
            data object IncrementSettingContentLineHeight : SettingEvent()
            data object IncrementSettingContentTextMarginHorizontal : SettingEvent()
            data object IncrementSettingContentImageMarginHorizontal : SettingEvent()
            data object IncrementSettingSelectorTextSize : SettingEvent()
            data object IncrementSettingSelectorPaddingVertical : SettingEvent()

            data object DecrementSettingContentTextSize : SettingEvent()
            data object DecrementSettingContentLineHeight : SettingEvent()
            data object DecrementSettingContentTextMarginHorizontal : SettingEvent()
            data object DecrementSettingContentImageMarginHorizontal : SettingEvent()
            data object DecrementSettingSelectorTextSize : SettingEvent()
            data object DecrementSettingSelectorPaddingVertical : SettingEvent()
        }
    }

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect()
    }
}

data class PageWrapper(
    val id: Long,
    val title: String,
    val sources: List<SourceWrapper>,
    val diff: Int = 0
)

sealed class SourceWrapper {
    data class TextWrapper(val description: String) : SourceWrapper()
    data class ImageWrapper(val imageFile: File) : SourceWrapper()
}