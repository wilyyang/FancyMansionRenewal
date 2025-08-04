package com.fancymansion.presentation.main.tab.editor

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.ArgName.NAME_USER_ID
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.common.const.testEpisodeRef
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.core.presentation.base.CommonEvent
import com.fancymansion.domain.usecase.book.UseCaseBookList
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
    private val useCaseBookList: UseCaseBookList,
    private val useCaseGetResource: UseCaseGetResource
) : BaseViewModel<EditorTabContract.State, EditorTabContract.Event, EditorTabContract.Effect>() {

    private var isUpdateResume = false
    private var userId: String = savedStateHandle.get<String>(NAME_USER_ID) ?: testEpisodeRef.userId
    private val mode: ReadMode = ReadMode.EDIT
    private lateinit var originBookInfoList: List<EditBookWrapper>
    val bookInfoStates: SnapshotStateList<EditBookState> = mutableStateListOf()

    init {
        initializeState()
    }

    override fun setInitialState() = EditorTabContract.State()

    override fun handleEvents(event: EditorTabContract.Event) {
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
            // TODO 07.14 init Editor Tab
            if(!useCaseMakeBook.bookLogicFileExists(episodeRef = testEpisodeRef)){
                useCaseBookList.makeSampleEpisode()
            }

            updateBookStateList()
            setState {
                copy(
                    isInitSuccess = true
                )
            }
        }
    }

    // EditorTabEvent

    // CommonEvent

    // CommonFunction
    private suspend fun updateBookStateList() {
        originBookInfoList = useCaseBookList.getUserEditBookInfoList(userId = userId).map { it.toWrapper() }

        bookInfoStates.clear()
        originBookInfoList.forEach { bookInfo ->
            bookInfoStates.add(
                EditBookState(
                    bookInfo = bookInfo,
                    selected = mutableStateOf(false)
                )
            )
        }
    }

    private fun handleOnResume() {
        if (isUpdateResume) {
            isUpdateResume = false
            launchWithLoading {
                // TODO 07.14 load Editor Tab
            }
        }
    }
}