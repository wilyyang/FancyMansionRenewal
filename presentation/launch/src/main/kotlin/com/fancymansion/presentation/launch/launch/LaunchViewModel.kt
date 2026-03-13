package com.fancymansion.presentation.launch.launch

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.getBookId
import com.fancymansion.core.common.const.getEpisodeId
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.domain.model.book.EditorModel
import com.fancymansion.domain.usecase.book.UseCaseBookList
import com.fancymansion.domain.usecase.user.UseCaseGoogleLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseBookList: UseCaseBookList,
    private val useCaseGoogleLogin: UseCaseGoogleLogin
) : BaseViewModel<LaunchContract.State, LaunchContract.Event, LaunchContract.Effect>() {

    init {
        setState {
            copy(
                isAnimationStart = requireNotNull(savedStateHandle.get<Boolean>(ArgName.NAME_IS_ANIMATION_START))
            )
        }
    }

    override fun setInitialState() = LaunchContract.State()

    override fun handleEvents(event: LaunchContract.Event) {
        when(event){
            LaunchContract.Event.OnClickGoogleLogin -> {
                setState {
                    copy(
                        isUserLoginVisible = false
                    )
                }
                setEffect {
                    LaunchContract.Effect.Navigation.GoogleLoginLauncherCall
                }
            }
            is LaunchContract.Event.GoogleTokenAcquired -> {
                launchWithException {
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
            is LaunchContract.Event.GoogleLoginFail,
            LaunchContract.Event.GoogleLoginNeedUserAction,
            LaunchContract.Event.GoogleLoginCancel -> {
                setState {
                    copy(
                        isUserLoginVisible = true
                    )
                }
            }
        }
    }

    override fun handleCommonEvents(event: CommonEvent) {
        when(event){
            is CommonEvent.OnResume -> {
                if(!uiState.value.isAutoLoginChecked){
                    setState {
                        copy(
                            isAutoLoginChecked = true,
                            isUserLoginVisible = false
                        )
                    }
                    setEffect {
                        LaunchContract.Effect.Navigation.AttemptGoogleAutoLogin
                    }
                }
            }
            else -> super.handleCommonEvents(event)
        }
    }
}