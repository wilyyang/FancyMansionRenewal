package com.fancymansion.presentation.main.content

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<MainContract.State, MainContract.Event, MainContract.Effect>() {

    private var isUpdateResume = false

    init {
        initializeState()
    }

    override fun setInitialState() = MainContract.State()

    override fun handleEvents(event: MainContract.Event) {
        when(event) {
            else -> {}
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
        }
    }
}