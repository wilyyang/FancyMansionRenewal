package com.fancymansion.presentation.main.content

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
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseMakeBook: UseCaseMakeBook,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<MainContract.State, MainContract.Event, MainContract.Effect>() {

    private var isUpdateResume = false

    init {
        initializeState()
    }

    override fun setInitialState() = MainContract.State()

    override fun handleEvents(event: MainContract.Event) {
        when(event) {
            is MainContract.Event.EditorBookHolderClicked -> {
                setEffect {
                    MainContract.Effect.Navigation.NavigateEditorBookOverviewScreen(event.episodeRef)
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
            // TODO 07.11 init Main
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

    // MainEvent

    // CommonEvent
    private fun handleOnResume() {
        if (isUpdateResume) {
            isUpdateResume = false
            launchWithLoading {
                // TODO 07.11 load Main
            }
        }
    }
}