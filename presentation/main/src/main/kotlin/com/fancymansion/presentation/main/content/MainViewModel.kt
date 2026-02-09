package com.fancymansion.presentation.main.content

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.EpisodeRef
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.getEpisodeId
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.core.presentation.base.LoadState
import com.fancymansion.domain.usecase.book.UseCaseBookList
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.remoteBook.UseCaseDownloadBook
import com.fancymansion.domain.usecase.remoteBook.UseCaseGetBookCoverImageUrl
import com.fancymansion.domain.usecase.remoteBook.UseCaseGetHomeBookList
import com.fancymansion.domain.usecase.user.UseCaseGetUserInfoLocal
import com.fancymansion.presentation.main.common.MainScreenTab
import com.fancymansion.presentation.main.tab.editor.EditBookState
import com.fancymansion.presentation.main.tab.editor.toWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseGetUserInfoLocal: UseCaseGetUserInfoLocal,
    private val useCaseGetHomeBookList: UseCaseGetHomeBookList,
    private val useCaseGetBookCoverImageUrl: UseCaseGetBookCoverImageUrl,
    private val useCaseDownloadBook: UseCaseDownloadBook,
    private val useCaseBookList: UseCaseBookList,
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
                    MainScreenTab.Study -> {
                        launchWithLoading {
                            loadStudyBookList()
                        }
                    }
                    else -> {}

                }
                setState { copy(currentTab = event.tab) }
            }
            is MainContract.Event.HomeBookHolderClicked -> {
                launchWithLoading {
                    useCaseDownloadBook(userId = userId, publishedId = event.publishedId)
                }
            }
            is MainContract.Event.DownloadBookHolderClicked -> {
                setEffect {
                    MainContract.Effect.Navigation.NavigateOverviewScreen(
                        episodeRef = EpisodeRef(
                            userId = userId,
                            mode = ReadMode.READ,
                            bookId = event.bookId,
                            episodeId = getEpisodeId(event.bookId)
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
            loadStudyBookList()

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

    private suspend fun loadStudyBookList(){
        val studyBookList = useCaseBookList.getLocalBookInfoList(userId = userId, readMode = ReadMode.READ).map{
            val bookInfo = it.book
            val episodeInfo = it.episode

            val bookCoverFile: File? =
                if (bookInfo.introduce.coverList.isNotEmpty()) useCaseLoadBook.loadCoverImage(
                    EpisodeRef(userId = userId, mode = ReadMode.READ, bookId = bookInfo.id, episodeId = episodeInfo.id),
                    bookInfo.introduce.coverList[0]
                ) else null

            val savedPickType = if (bookCoverFile != null) ImagePickType.SavedImage(
                bookCoverFile
            ) else ImagePickType.Empty

            EditBookState(
                it.toWrapper(savedPickType),
                mutableStateOf(false)
            )
        }

        setState {
            copy(
                studyBookList = studyBookList
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