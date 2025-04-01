package com.fancymansion.presentation.editor.selectorContent

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName.NAME_BOOK_ID
import com.fancymansion.core.common.const.ArgName.NAME_BOOK_TITLE
import com.fancymansion.core.common.const.ArgName.NAME_EPISODE_ID
import com.fancymansion.core.common.const.ArgName.NAME_PAGE_ID
import com.fancymansion.core.common.const.ArgName.NAME_READ_MODE
import com.fancymansion.core.common.const.ArgName.NAME_SELECTOR_ID
import com.fancymansion.core.common.const.ArgName.NAME_USER_ID
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditorSelectorContentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseMakeBook: UseCaseMakeBook,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<EditorSelectorContentContract.State, EditorSelectorContentContract.Event, EditorSelectorContentContract.Effect>() {

    private var isUpdateResume = false
    private val pageId: Long by lazy { requireNotNull(savedStateHandle.get<Long>(NAME_PAGE_ID)) }
    private val selectorId: Long by lazy { requireNotNull(savedStateHandle.get<Long>(NAME_SELECTOR_ID)) }

    private val episodeRef: EpisodeRef = savedStateHandle.run {
        EpisodeRef(
            requireNotNull(get<String>(NAME_USER_ID)),
            requireNotNull(get<ReadMode>(NAME_READ_MODE)),
            requireNotNull(get<String>(NAME_BOOK_ID)),
            requireNotNull(get<String>(NAME_EPISODE_ID))
        )
    }

    init {
        initializeState()
    }

    override fun setInitialState() = EditorSelectorContentContract.State()

    override fun handleEvents(event: EditorSelectorContentContract.Event) {
        when(event) {
            EditorSelectorContentContract.Event.SelectorSaveToFile -> {
                // TODO Handle 04.01
            }
        }
    }

    override fun handleCommonEvents(event: CommonEvent) {
        when(event){
            is CommonEvent.OnResume -> handleOnResume()
            is CommonEvent.CloseEvent -> {
                // TODO Check edit 04.01
                super.handleCommonEvents(CommonEvent.CloseEvent)
            }
            else -> super.handleCommonEvents(event)
        }
    }

    private fun initializeState() {
        launchWithInit {
            val bookTitle = requireNotNull(savedStateHandle.get<String>(NAME_BOOK_TITLE))
            // TODO Update 04.01
            setState {
                copy(
                    isInitSuccess = true,
                    bookTitle = bookTitle
                )
            }
        }
    }

    // CommonEvent
    private fun handleOnResume() {
        if (isUpdateResume) {
            isUpdateResume = false
            // TODO Update 04.01
        }
    }
}