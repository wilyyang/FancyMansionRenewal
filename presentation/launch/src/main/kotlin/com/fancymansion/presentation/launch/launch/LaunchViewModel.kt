package com.fancymansion.presentation.launch.launch

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.domain.usecase.app.UseCaseCompleteOnboarding
import com.fancymansion.domain.usecase.app.UseCaseIsFirstLaunch
import com.fancymansion.domain.usecase.book.UseCaseBookList
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseIsFirstLaunch: UseCaseIsFirstLaunch,
    private val useCaseBookList: UseCaseBookList,
    private val useCaseCompleteOnboarding: UseCaseCompleteOnboarding,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<LaunchContract.State, LaunchContract.Event, LaunchContract.Effect>() {

    init {
        initializeState()
    }

    override fun setInitialState() = LaunchContract.State()

    override fun handleEvents(event: LaunchContract.Event) {}

    private fun initializeState() {
        launchWithInit {
            val isFirst = useCaseIsFirstLaunch()
            if (isFirst) {
                useCaseBookList.makeSampleEpisode()
                useCaseCompleteOnboarding()
            }

            setState {
                copy(
                    isInitSuccess = true
                )
            }
        }
    }
}