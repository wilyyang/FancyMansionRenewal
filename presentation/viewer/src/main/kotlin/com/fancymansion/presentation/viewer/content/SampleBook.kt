package com.fancymansion.presentation.viewer.content

import com.fancymansion.domain.model.book.Content
import com.fancymansion.domain.model.book.Page
import com.fancymansion.domain.model.book.Source
import com.fancymansion.presentation.viewer.R

val page1 : Page = Page(
    id = 100000000L,
    title = "잠에서 깨어난 배트",
    sources = listOf(
        Source.Image(R.drawable.sample_img_01),
        Source.Text("배트가 잠에서 깨어났답니다.\n" +
                "배트는 동굴 밖으로 날아갔지요.\n\n" +
                "\"난 벌레를 원해!\"\n" +
                "배트가 말했어요.\n\n" +
                "\"난 벌레를 찾을 거야.\n" +
                "그리고 벌레들을 먹을 거야!\""),
        Source.Image(R.drawable.sample_img_02),
        Source.Text("무엇을 하는게 좋을까요?")
    )
)

val content : Content = Content(pages = listOf(page1))