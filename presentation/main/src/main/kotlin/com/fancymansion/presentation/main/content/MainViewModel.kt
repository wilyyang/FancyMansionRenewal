package com.fancymansion.presentation.main.content

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.common.const.PublishStatus
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.model.book.BookMetaModel
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import com.fancymansion.domain.usecase.remoteBook.UseCaseDownloadBook
import com.fancymansion.domain.usecase.remoteBook.UseCaseGetBookCoverImageUrl
import com.fancymansion.domain.usecase.remoteBook.UseCaseGetHomeBookList
import com.fancymansion.domain.usecase.user.UseCaseGetUserInfoLocal
import com.fancymansion.presentation.main.common.MainScreenTab
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseGetUserInfoLocal: UseCaseGetUserInfoLocal,
    private val useCaseGetHomeBookList: UseCaseGetHomeBookList,
    private val useCaseGetBookCoverImageUrl: UseCaseGetBookCoverImageUrl,
    private val useCaseDownloadBook: UseCaseDownloadBook,
    private val useCaseMakeBook: UseCaseMakeBook,
    private val useCaseLoadBook: UseCaseLoadBook
) : BaseViewModel<MainContract.State, MainContract.Event, MainContract.Effect>() {

    private lateinit var userId: String
    private var isUpdateResume = false

    init {
        initializeState()
    }

    override fun setInitialState() = MainContract.State()

    override fun handleEvents(event: MainContract.Event) {
        when(event) {
            is MainContract.Event.TabSelected -> {
                when(event.tab){
                    MainScreenTab.Home -> {
                        launchWithLoading {
                            loadHomeBookList()
                        }
                    }
                    else -> {}

                }
                setState { copy(currentTab = event.tab) }
            }
            is MainContract.Event.HomeBookHolderClicked -> {
                launchWithLoading {
                    useCaseDownloadBook(userId = userId, publishedId = event.publishedId)
                    val currentTime = System.currentTimeMillis()
                    useCaseMakeBook.makeMetaData(
                        userId = userId,
                        mode = ReadMode.READ,
                        bookId = event.publishedId,
                        metaData = BookMetaModel(
                            status = PublishStatus.PUBLISHED,
                            publishedAt = currentTime,
                            updatedAt = currentTime,
                            version = 0
                        )
                    )
                }
            }
            MainContract.Event.OnClickLogout -> {
                setLoadState(LoadState.Loading())
                setEffect {
                    MainContract.Effect.Navigation.RequestGoogleLogout
                }
            }
            MainContract.Event.GoogleLogoutSuccess -> {
                setLoadStateIdle()
                setEffect {
                    MainContract.Effect.Navigation.NavigateLaunchScreen
                }
            }
            is MainContract.Event.GoogleLogoutFail -> {}
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
            val userInfo = useCaseGetUserInfoLocal() ?: error("UserInfo is null")
            userId = userInfo.userId
            loadHomeBookList()

            setState {
                copy(
                    isInitSuccess = true
                )
            }
        }
    }

    private suspend fun loadHomeBookList(){
        val originHomeBookList = useCaseGetHomeBookList()
        val homeBookList = originHomeBookList.map {
            HomeBookState(
                it.toWrapper(ImagePickType.Empty)
            )
        }

        val homeBookUrlList = originHomeBookList.map {
            useCaseGetBookCoverImageUrl(it.book.publishInfo.publishedId, it.book.introduce.coverList[0])
        }

        setState {
            copy(
                homeBookList = homeBookList,
                homeBookUrlList = homeBookUrlList
            )
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