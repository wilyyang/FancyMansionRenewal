package com.fancymansion.presentation.launch.launch

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.getBookId
import com.fancymansion.core.common.const.getEpisodeId
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.model.book.EditorModel
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
            is LaunchContract.Event.GoogleTokenAcquired -> {
                launchWithLoading {
                    val userInfo = useCaseGoogleLogin(event.idToken)

                    if (!userInfo.hasCompletedOnboarding) {
                        useCaseBookList.makeSampleEpisode(
                            episodeRef = EpisodeRef(
                                userId = userInfo.userId,
                                mode = ReadMode.EDIT,
                                bookId = getBookId(userInfo.userId, 0),
                                episodeId = getEpisodeId(userInfo.userId, 0)
                            ),
                            editorModel = EditorModel(
                                editorId = userInfo.userId,
                                editorName = userInfo.nickname,
                                editorEmail = userInfo.email
                            )
                        )
                    }

                    setEffect {
                        LaunchContract.Effect.Navigation.NavigateMain
                    }
                }
            }
            LaunchContract.Event.GoogleLoginNeedUserAction -> {
                setLoadStateIdle()
            }
            is LaunchContract.Event.GoogleLoginFail -> {}
            LaunchContract.Event.GoogleLoginCancel -> {}
        }
    }

    override fun handleCommonEvents(event: CommonEvent) {
        when(event){
            is CommonEvent.OnResume -> {
                if(!uiState.value.isAutoLoginChecked){
                    setState {
                        copy(
                            isAutoLoginChecked = true
                        )
                    }
                    setLoadState(LoadState.Loading())
                    setEffect {
                        LaunchContract.Effect.Navigation.AttemptGoogleAutoLogin
                    }
                }
            }
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
}