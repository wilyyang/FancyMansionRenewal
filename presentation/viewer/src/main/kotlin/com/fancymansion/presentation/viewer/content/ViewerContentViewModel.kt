package com.fancymansion.presentation.viewer.content

import androidx.lifecycle.SavedStateHandle
import com.fancymansion.core.common.const.BookRef
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.common.const.testBookRef
import com.fancymansion.core.presentation.base.BaseViewModel
import com.fancymansion.domain.model.book.LogicModel
import com.fancymansion.domain.model.book.SourceModel
import com.fancymansion.domain.usecase.book.UseCaseBookLogic
import com.fancymansion.domain.usecase.book.UseCaseLoadBook
import com.fancymansion.domain.usecase.book.UseCaseMakeBook
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewerContentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCaseLoadBook: UseCaseLoadBook,
    private val useCaseBookLogic: UseCaseBookLogic,
    private val useCaseMakeBook: UseCaseMakeBook
) : BaseViewModel<ViewerContentContract.State, ViewerContentContract.Event, ViewerContentContract.Effect>() {
    private lateinit var bookRef : BookRef
    private lateinit var logic : LogicModel

    override fun setInitialState() = ViewerContentContract.State()

    override fun handleEvents(event: ViewerContentContract.Event) {
        when (event) {
            is ViewerContentContract.Event.OnClickSelector -> {
                launchWithLoading {
                    useCaseBookLogic.incrementActionCount(bookRef, countActionId = event.selectorId)

                    useCaseBookLogic.getNextRoutePageId(
                        bookRef,
                        routes = logic.logics.first { it.id == event.pageId }.selectors.first { it.id == event.selectorId }.routes
                    ).let { nextPageId ->
                        useCaseBookLogic.incrementActionCount(bookRef, countActionId = nextPageId)
                        loadPageContent(nextPageId)
                    }
                }
            }
        }
    }

    init {
        launchWithLoading {
            bookRef = testBookRef

            useCaseMakeBook.makeSampleBook()

            logic = useCaseLoadBook.loadLogic(bookRef)
            useCaseBookLogic.deleteBookActionCount(bookRef)

            logic.logics.first { it.type == PageType.START }.id.let { pageId ->
                useCaseBookLogic.incrementActionCount(bookRef, countActionId = pageId)
                loadPageContent(pageId)
            }
        }
    }

    private suspend fun loadPageContent(pageId : Long){
        val page = useCaseLoadBook.loadPage(bookRef, pageId = pageId)
        val selectors = useCaseBookLogic.getVisibleSelectors(bookRef, logic = logic, pageId = pageId)

        val pageWrapper = PageWrapper(
            id = page.id,
            title = page.title,
            sources = page.sources.map {
                when(it){
                    is SourceModel.TextModel -> {
                        SourceWrapper.TextWrapper(it.description)
                    }
                    is SourceModel.ImageModel -> {
                        SourceWrapper.ImageWrapper(useCaseLoadBook.loadImage(bookRef, it.imageName))
                    }
                }
            }
        )

        setState {
            copy(
                pageWrapper = pageWrapper,
                selectors = selectors
            )
        }
    }
}