package com.fancymansion.presentation.main.tab.editor

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.testEpisodeRef
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditorTabViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseMakeBook: UseCaseMakeBook,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<EditorTabContract.State, EditorTabContract.Event, EditorTabContract.Effect>() {

    private var isUpdateResume = false

    init {
        initializeState()
    }

    override fun setInitialState() = EditorTabContract.State()

    override fun handleEvents(event: EditorTabContract.Event) {
        when(event) {
            is EditorTabContract.Event.EditorBookHolderClicked -> {
                setEffect {
                    EditorTabContract.Effect.Navigation.NavigateEditorBookOverviewScreen(event.episodeRef)
                }
            }
        }
    }

    override fun handleCommonEvents(event: CommonEvent) {
        when(event){
            is CommonEvent.OnResume -> handleOnResume()
            else -> super.handleCommonEvents(event)
        }
    }

    private fun initializeState() {
        launchWithInit {
            // TODO 07.14 init Editor Tab
            if(!useCaseMakeBook.bookLogicFileExists(episodeRef = testEpisodeRef)){
                useCaseMakeBook.makeSampleEpisode()
            }

            setState {
                copy(
                    isInitSuccess = true
                )
            }
        }
    }

    // EditorTabEvent

    // CommonEvent
    private fun handleOnResume() {
        if (isUpdateResume) {
            isUpdateResume = false
            launchWithLoading {
                // TODO 07.14 load Editor Tab
            }
        }
    }
}