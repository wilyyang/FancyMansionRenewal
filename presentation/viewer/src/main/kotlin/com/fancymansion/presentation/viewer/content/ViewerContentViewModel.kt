package com.fancymansion.presentation.viewer.content

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.BookRef
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.common.const.ReadMode
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.usecase.book.UseCaseBookLogic
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewerContentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseBookLogic: UseCaseBookLogic,
) : BaseViewModel<ViewerContentContract.State, ViewerContentContract.Event, ViewerContentContract.Effect>() {
    private lateinit var bookRef : BookRef
    private lateinit var logic : LogicModel

    override fun setInitialState() = ViewerContentContract.State()

    override fun handleEvents(event: ViewerContentContract.Event) {
        when (event) {
            is ViewerContentContract.Event.OnClickSelector -> {
                launchWithLoading {
                    useCaseBookLogic.incrementCount(bookRef, countId = event.selectorId)

                    useCaseBookLogic.getNextRoutePageId(
                        bookRef,
                        routes = logic.logics.first { it.id == event.pageId }.selectors.first { it.id == event.selectorId }.routes
                    ).let { nextPageId ->
                        useCaseBookLogic.incrementCount(bookRef, countId = nextPageId)
                        loadPageContent(nextPageId)
                    }
                }
            }
        }
    }

    init {
        launchWithLoading {
            bookRef = BookRef(
                userId = "TEST_USER_ID",
                mode = ReadMode.EDIT,
                bookId = "TEST_BOOK_ID"
            )

            logic = useCaseLoadBook.loadLogic(bookRef)
            useCaseBookLogic.resetBookCount(bookRef)

            logic.logics.first { it.type == PageType.START }.id.let { pageId ->
                useCaseBookLogic.incrementCount(bookRef, countId = pageId)
                loadPageContent(pageId)
            }
        }
    }

    private suspend fun loadPageContent(pageId : Long){
        val page = useCaseLoadBook.loadPage(bookRef, pageId = pageId)
        val selectors = useCaseBookLogic.getVisibleSelectors(bookRef, logic = logic, pageId = pageId)

        setState {
            copy(
                pageState = PageState(page = page),
                selectors = selectors
            )
        }
    }
}