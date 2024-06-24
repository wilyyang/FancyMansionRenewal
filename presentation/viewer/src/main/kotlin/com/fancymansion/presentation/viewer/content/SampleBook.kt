package com.fancymansion.presentation.viewer.content

import com.fancymansion.core.common.const.BookId
import com.fancymansion.core.common.const.PageId
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.common.const.ROUTE_PAGE_ID_NOT_ASSIGNED
import com.fancymansion.core.common.const.RouteId
import com.fancymansion.core.common.const.SelectorId
import com.fancymansion.domain.model.book.Content
import com.fancymansion.domain.model.book.Logic
import com.fancymansion.domain.model.book.Page
import com.fancymansion.domain.model.book.PageLogic
import com.fancymansion.domain.model.book.Route
import com.fancymansion.domain.model.book.Selector
import com.fancymansion.domain.model.book.Source
import com.fancymansion.presentation.viewer.R

val page1 : Page = Page(
    id = PageId(1_00_00_00_00L),
    title = "잠에서 깨어난 배트",
    sources = listOf(
        Source.Image(resId = R.drawable.sample_img_01),
        Source.Text(description = "\n배트가 잠에서 깨어났답니다.\n" +"배트는 동굴 밖으로 날아갔지요.\n"),
        Source.Image(resId = R.drawable.sample_img_02),
        Source.Text("\n\"난 벌레를 원해!\"\n" + "배트가 말했어요.\n" +
                "\n" +
                "\"난 벌레를 찾을 거야.\n" + "그리고 벌레들을 먹을 거야!\"\n" +
                "\n" +
                "무엇을 하는게 좋을까요?\n\n")
    )
)

val logic1 : PageLogic = PageLogic(
    id = PageId(1_00_00_00_00L),
    type = PageType.START,
    title = "잠에서 깨어난 배트",
    selectors = listOf(
        Selector(
            id = SelectorId(1_01_00_00_00L),
            text = "물가로 간다",
            showConditions = listOf(),
            routes = listOf(
                Route(
                    id = RouteId(1_01_00_01_00L),
                    routePageId = PageId(ROUTE_PAGE_ID_NOT_ASSIGNED),
                )
            )
        ),
        Selector(
            id = SelectorId(1_02_00_00_00L),
            text = "옥수수밭으로 간다",
            showConditions = listOf(),
            routes = listOf(
                Route(
                    id = RouteId(1_02_00_01_00L),
                    routePageId = PageId(ROUTE_PAGE_ID_NOT_ASSIGNED),
                )
            )
        )
    )
)


val page2 : Page = Page(
    id = PageId(2_00_00_00_00L),
    title = "물가에서의 사냥",
    sources = listOf(
        Source.Image(resId = R.drawable.sample_img_06),
        Source.Text(description = "\n배트는 커다란 벌레 몇 마리를 찾았어요.\n" + "배트는 작은 벌레 몇 마리를 찾았어요.\n"),
        Source.Image(resId = R.drawable.sample_img_07),
        Source.Text("\n배트는 먹고 먹고 또 먹었어요.\n" + "배트는 날고 날고 또 날았어요.\n"),
        Source.Image(resId = R.drawable.sample_img_09),
        Source.Text("\n그런데 이제 아주 어두워졌어요.\n" + "\"집이 어디지?\" 배트가 말했어요.\n\n")
    )
)

val logic2 : PageLogic = PageLogic(
    id = PageId(2_00_00_00_00L),
    type = PageType.NORMAL,
    title = "물가에서의 사냥",
    selectors = listOf(
        Selector(
            id = SelectorId(2_01_00_00_00L),
            text = "달을 향해 날아오른다",
            showConditions = listOf(),
            routes = listOf(
                Route(
                    id = RouteId(2_01_00_01_00L),
                    routePageId = PageId(ROUTE_PAGE_ID_NOT_ASSIGNED),
                )
            )
        ),
        Selector(
            id = SelectorId(2_02_00_00_00L),
            text = "바위 틈으로 들어간다",
            showConditions = listOf(),
            routes = listOf(
                Route(
                    id = RouteId(2_02_00_01_00L),
                    routePageId = PageId(ROUTE_PAGE_ID_NOT_ASSIGNED),
                )
            )
        )
    )
)

val page3 : Page = Page(
    id = PageId(3_00_00_00_00L),
    title = "옥수수밭에 가다",
    sources = listOf(
        Source.Image(resId = R.drawable.sample_img_08),
        Source.Text(description = "\n배트는 옥수수밭에 도착했어요. 그러나 곧\n" +
                "\n" +
                "\"우르르 쾅쾅!\"\n"),
        Source.Image(resId = R.drawable.sample_img_14),
        Source.Text("\n폭우가 쏟아지기 시작했어요.\n" +
                "배트는 비를 피하기 위해 옥수수밭으로 들어갔습니다.\n"),
        Source.Image(resId = R.drawable.sample_img_17),
        Source.Text("\n옥수수밭에서 비를 피할만한 헛간이 보이는군요?\n")
    )
)

val logic3 : PageLogic = PageLogic(
    id = PageId(3_00_00_00_00L),
    type = PageType.NORMAL,
    title = "옥수수밭에 가다",
    selectors = listOf(
        Selector(
            id = SelectorId(3_01_00_00_00L),
            text = "헛간을 향해 날아오른다",
            showConditions = listOf(),
            routes = listOf(
                Route(
                    id = RouteId(3_01_00_01_00L),
                    routePageId = PageId(ROUTE_PAGE_ID_NOT_ASSIGNED),
                )
            )
        ),
        Selector(
            id = SelectorId(3_02_00_00_00L),
            text = "바위 틈으로 들어간다",
            showConditions = listOf(),
            routes = listOf(
                Route(
                    id = RouteId(3_02_00_01_00L),
                    routePageId = PageId(ROUTE_PAGE_ID_NOT_ASSIGNED),
                )
            )
        )
    )
)

val page4 : Page = Page(
    id = PageId(4_00_00_00_00L),
    title = "바위틈에서",
    sources = listOf(
        Source.Image(resId = R.drawable.sample_img_25),
        Source.Text(description = "\n비를 피해 바위 틈으로 들어갔습니다. 그리고 배트는 겁에 질린채 꼼짝도 못했습니다.")
    )
)

val logic4 : PageLogic = PageLogic(
    id = PageId(4_00_00_00_00L),
    type = PageType.ENDING,
    title = "바위틈에서",
    selectors = listOf()
)

val page5 : Page = Page(
    id = PageId(5_00_00_00_00L),
    title = "플라이 투더 문",
    sources = listOf(
        Source.Text(description = "\n달을 향해 날아오르는 배트!\n" +
                "하늘로 날아오르니 기분이 아주 좋아요!\n"),
        Source.Image(resId = R.drawable.sample_img_04),
        Source.Text(description = "\n그런데 그 뿐이었습니다.\n" +
                "밝은 달 아래에서 바라봐도 집이 어딘지 보이지 않아요.\n\n"),
        Source.Image(resId = R.drawable.sample_img_09)
    )
)

val logic5 : PageLogic = PageLogic(
    id = PageId(5_00_00_00_00L),
    type = PageType.NORMAL,
    title = "플라이 투더 문",
    selectors = listOf(
        Selector(
            id = SelectorId(5_01_00_00_00L),
            text = "달로 다시 날아오른다",
            showConditions = listOf(),
            routes = listOf(
                Route(
                    id = RouteId(5_01_00_01_00L),
                    routePageId = PageId(ROUTE_PAGE_ID_NOT_ASSIGNED),
                )
            )
        ),
        Selector(
            id = SelectorId(5_02_00_00_00L),
            text = "옥수수밭으로 간다",
            showConditions = listOf(),
            routes = listOf(
                Route(
                    id = RouteId(5_02_00_01_00L),
                    routePageId = PageId(ROUTE_PAGE_ID_NOT_ASSIGNED),
                )
            )
        ),
        Selector(
            id = SelectorId(5_03_00_00_00L),
            text = "비가 오기 시작한다",
            showConditions = listOf(),
            routes = listOf(
                Route(
                    id = RouteId(5_03_00_01_00L),
                    routePageId = PageId(ROUTE_PAGE_ID_NOT_ASSIGNED),
                )
            )
        )
    )
)

val page6 : Page = Page(
    id = PageId(6_00_00_00_00L),
    title = "여긴 어디지?",
    sources = listOf(
        Source.Image(resId = R.drawable.sample_img_35),
        Source.Text(description = "\n어느새 비가 그치고 날이 밝았습니다. 그런데 여긴 어디죠? 박쥐는 낮에 잘 보이지 않습니다. 길을 잃은 배트는 그렇게 정처없이 날았답니다..")
    )
)

val logic6 : PageLogic = PageLogic(
    id = PageId(6_00_00_00_00L),
    type = PageType.ENDING,
    title = "여긴 어디지?",
    selectors = listOf()
)

val page7 : Page = Page(
    id = PageId(7_00_00_00_00L),
    title = "어두운 밤에 비가 축축",
    sources = listOf(
        Source.Image(resId = R.drawable.sample_img_14),
        Source.Text(description = "\n갑자기 비가 오기 시작하네요.\n" +
                "배트는 주위를 모두 살펴보았어요.\n" +
                "\n" +
                "저기 비를 피할만한 나무가 보이는군요?\n"),
        Source.Image(resId = R.drawable.sample_img_11),
        Source.Text(description = "\n어떻게든 나무를 붙잡았습니다.\n" +
                "\"허억 허억. 겨우 살았네... 비만은 피할수 있겠어!\"\n" +
                "그러나 심상치 않은 바람이 몰려듭니다.\n\n"),
        Source.Image(resId = R.drawable.sample_img_10),
        Source.Text(description = "\n\"으악 으아아아악!\"\n" +
                "비만 오는게 아니라 강풍이 몰아치는군요.\n" +
                "\n" +
                "배트는 단련된 팔근육으로 나무 위로 올라서는데 성공했어요.\n"),
        Source.Image(resId = R.drawable.sample_img_13),
        Source.Text(description = "\n나무 위에서 주변을 둘러봅니다.\n" +
                "저 멀리 헛간이 보이는군요?\n"),

    )
)

val logic7 : PageLogic = PageLogic(
    id = PageId(7_00_00_00_00L),
    type = PageType.NORMAL,
    title = "어두운 밤에 비가 축축",
    selectors = listOf(
        Selector(
            id = SelectorId(7_01_00_00_00L),
            text = "헛간을 향해 날아오른다",
            showConditions = listOf(),
            routes = listOf(
                Route(
                    id = RouteId(7_01_00_01_00L),
                    routePageId = PageId(ROUTE_PAGE_ID_NOT_ASSIGNED),
                )
            )
        )
    )
)

val page8 : Page = Page(
    id = PageId(8_00_00_00_00L),
    title = "헛간을 향해 힘찬 날개짓!",
    sources = listOf(
        Source.Text(description = "배트는 힘내어 헛간을 향해 날개짓합니다.\n"),
        Source.Image(resId = R.drawable.sample_img_37),
        Source.Text(description = "\n\"하나! 둘! 좀만 더 힘을 내면 된다!\"\n" +
                "\"휘우우우우웅!\"\n" +
                "\n" +
                "강풍이 몰아칩니다.\n"),
        Source.Image(resId = R.drawable.sample_img_15),
        Source.Text(description = "\n배트는 강풍에 그만 땅에 떨어졌습니다.\n"),
        Source.Image(resId = R.drawable.sample_img_16),
        Source.Text(description = "\n땅에 박혀버린 배트… 이제 날 힘이 거의 없는걸까요?\n")
        )
)

val logic8 : PageLogic = PageLogic(
    id = PageId(8_00_00_00_00L),
    type = PageType.NORMAL,
    title = "헛간을 향해 힘찬 날개짓!",
    selectors = listOf(
        Selector(
            id = SelectorId(8_01_00_00_00L),
            text = "다시한 번 날개짓한다.",
            showConditions = listOf(),
            routes = listOf(
                Route(
                    id = RouteId(8_01_00_01_00L),
                    routePageId = PageId(ROUTE_PAGE_ID_NOT_ASSIGNED),
                )
            )
        )
    )
)

val content : Content = Content(pages = listOf(page1, page2, page3, page4, page5, page6, page7, page8))
val logic : Logic = Logic(id = BookId(1L), logics = listOf(logic1, logic2, logic3, logic4, logic5, logic6, logic7, logic8))