package com.fancymansion.presentation.viewer.content

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.domain.model.book.Logic
import com.fancymansion.domain.usecase.book.UseCaseBook
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ViewerContentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseBook: UseCaseBook
) : BaseViewModel<ViewerContentContract.State, ViewerContentContract.Event, ViewerContentContract.Effect>() {
    private lateinit var userId : String
    private lateinit var mode : ReadMode
    private lateinit var bookId : String
    private lateinit var logic : Logic
    private var currentPageId : Long = 0L

    override fun setInitialState() = ViewerContentContract.State()

    override fun handleEvents(event: ViewerContentContract.Event) {
        when (event) {
            is ViewerContentContract.Event.OnClickSelector -> {
                useCaseBook.incrementCount(userId, mode, bookId, event.selectorId)
                currentPageId = useCaseBook.getNextPageId(
                    userId,
                    mode,
                    bookId,
                    logic.logics.first { it.id == currentPageId }.selectors.first { it.id == event.selectorId }.routes
                )

                val page = useCaseBook.loadPage(userId, mode, bookId, currentPageId)
                val selectors =
                    useCaseBook.getVisibleSelectors(userId, mode, bookId, logic, currentPageId)

                setState {
                    copy(
                        page = page,
                        selectors = selectors
                    )
                }
            }
        }
    }

    init {
        launchWithLoading {
            userId = "TEST_USER_ID"
            mode = ReadMode.EDIT
            bookId = "TEST_BOOK_ID"

            logic = useCaseBook.loadLogic(userId, mode, bookId)
            useCaseBook.resetCount(userId, mode, bookId)
            currentPageId = logic.logics.first { it.type == PageType.START }.id
            val page = useCaseBook.loadPage(userId, mode, bookId, currentPageId)
            val selectors = useCaseBook.getVisibleSelectors(userId, mode, bookId, logic, currentPageId)

            useCaseBook.incrementCount(userId, mode, bookId, page.id)

            setState {
                copy(
                    page = page,
                    selectors = selectors
                )
            }
        }
    }
}