package com.fancymansion.presentation.main.content

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ImagePickType
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.domain.usecase.remoteBook.UseCaseDownloadBook
import com.fancymansion.domain.usecase.remoteBook.UseCaseGetHomeBookList
import com.fancymansion.domain.usecase.user.UseCaseGetUserInfoLocal
import com.fancymansion.presentation.main.tab.editor.EditBookState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseGetHomeBookList: UseCaseGetHomeBookList,
    private val useCaseGetUserInfoLocal: UseCaseGetUserInfoLocal,
    private val useCaseDownloadBook: UseCaseDownloadBook
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
                setState { copy(currentTab = event.tab) }
            }
            is MainContract.Event.HomeBookHolderClicked -> {
                launchWithLoading {
                    useCaseDownloadBook(userId = userId, publishedId = event.publishedId)
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
            val userInfo = useCaseGetUserInfoLocal() ?: error("UserInfo is null")
            userId = userInfo.userId
            val homeBookList = useCaseGetHomeBookList().map {
                EditBookState(
                    it.toWrapper(ImagePickType.Empty),
                    mutableStateOf(false)
                )
            }
            setState {
                copy(
                    homeBookList = homeBookList,
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