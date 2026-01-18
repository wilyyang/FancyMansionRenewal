package com.fancymansion.presentation.launch.launch

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.getBookId
import com.fancymansion.core.common.const.getEpisodeId
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.domain.usecase.book.UseCaseBookList
import com.fancymansion.domain.usecase.user.UseCaseGoogleLogin
import com.fancymansion.domain.usecase.util.UseCaseGetResource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseBookList: UseCaseBookList,
    private val useCaseGoogleLogin: UseCaseGoogleLogin,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<LaunchContract.State, LaunchContract.Event, LaunchContract.Effect>() {

    init {
        initializeState()
    }

    override fun setInitialState() = LaunchContract.State()

    override fun handleEvents(event: LaunchContract.Event) {
        when(event){
            LaunchContract.Event.OnClickGoogleLogin -> {
                setEffect {
                    LaunchContract.Effect.Navigation.GoogleLoginLauncherCall
                }
            }
            LaunchContract.Event.GoogleLoginLauncherCancel -> {}
            is LaunchContract.Event.GoogleLoginLauncherFail -> {}
            is LaunchContract.Event.GoogleLoginLauncherSuccess -> {
                launchWithLoading {
                    val userInfo = useCaseGoogleLogin(event.idToken)

                    if (!userInfo.hasCompletedOnboarding) {
                        useCaseBookList.makeSampleEpisode(
                            episodeRef = EpisodeRef(
                                userId = userInfo.userId,
                                mode = ReadMode.EDIT,
                                bookId = getBookId(userInfo.userId, ReadMode.EDIT, 0),
                                episodeId = getEpisodeId(userInfo.userId, ReadMode.EDIT, 0)
                            )
                        )
                    }

                    setEffect {
                        LaunchContract.Effect.Navigation.NavigateMain
                    }
                }
            }
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
}