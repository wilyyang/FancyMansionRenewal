package com.fancymansion.domain.usecase.book.sample

import com.fancymansion.core.common.const.ConditionType
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.common.const.RelationOp
import com.fancymansion.core.common.const.testEpisodeRef
import com.fancymansion.domain.model.book.*
import com.fancymansion.domain.usecase.R


val page1 : PageModel = PageModel(
    id = 1_00_00_00_00L,
    title = "잠에서 깨어난 배트",
    sources = listOf(
        SourceModel.ImageModel(imageName = "test_book_id_img_1.png"),
        SourceModel.TextModel(description = "\n배트가 잠에서 깨어났답니다.\n" +"배트는 동굴 밖으로 날아갔지요.\n"),
        SourceModel.ImageModel(imageName = "test_book_id_img_2.png"),
        SourceModel.TextModel("\n\"난 벌레를 원해!\"\n" + "배트가 말했어요.\n" +
                "\n" +
                "\"난 벌레를 찾을 거야.\n" + "그리고 벌레들을 먹을 거야!\"\n" +
                "\n" +
                "무엇을 하는게 좋을까요?\n")
    )
)

val logic1 : PageLogicModel = PageLogicModel(
    id = 1_00_00_00_00L,
    type = PageType.START,
    title = "잠에서 깨어난 배트",
    selectors = listOf(
        SelectorModel(
            id = 1_01_00_00_00L,
            text = "물가로 간다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    id = 1_01_00_01_00L,
                    routePageId = 2_00_00_00_00L,
                )
            )
        ),
        SelectorModel(
            id = 1_02_00_00_00L,
            text = "옥수수밭으로 간다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    id = 1_02_00_01_00L,
                    routePageId = 3_00_00_00_00L,
                )
            )
        )
    )
)


val page2 : PageModel = PageModel(
    id = 2_00_00_00_00L,
    title = "물가에서의 사냥",
    sources = listOf(
        SourceModel.ImageModel(imageName = "test_book_id_img_6.png"),
        SourceModel.TextModel(description = "\n배트는 커다란 벌레 몇 마리를 찾았어요.\n" + "배트는 작은 벌레 몇 마리를 찾았어요.\n"),
        SourceModel.ImageModel(imageName = "test_book_id_img_7.png"),
        SourceModel.TextModel("\n배트는 먹고 먹고 또 먹었어요.\n" + "배트는 날고 날고 또 날았어요.\n"),
        SourceModel.ImageModel(imageName = "test_book_id_img_9.png"),
        SourceModel.TextModel("\n그런데 이제 아주 어두워졌어요.\n" + "\"집이 어디지?\" 배트가 말했어요.\n")
    )
)

val logic2 : PageLogicModel = PageLogicModel(
    id = 2_00_00_00_00L,
    type = PageType.NORMAL,
    title = "물가에서의 사냥",
    selectors = listOf(
        SelectorModel(
            id = 2_01_00_00_00L,
            text = "달을 향해 날아오른다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    id = 2_01_00_01_00L,
                    routePageId = 5_00_00_00_00L
                )
            )
        ),
        SelectorModel(
            id = 2_02_00_00_00L,
            text = "바위 틈으로 들어간다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    id = 2_02_00_01_00L,
                    routePageId = 4_00_00_00_00L
                )
            )
        )
    )
)

val page3 : PageModel = PageModel(
    id = 3_00_00_00_00L,
    title = "옥수수밭에 가다",
    sources = listOf(
        SourceModel.ImageModel(imageName = "test_book_id_img_8.png"),
        SourceModel.TextModel(description = "\n배트는 옥수수밭에 도착했어요. 그러나 곧\n" +
                "\n" +
                "\"우르르 쾅쾅!\"\n"),
        SourceModel.ImageModel(imageName = "test_book_id_img_14.png"),
        SourceModel.TextModel("\n폭우가 쏟아지기 시작했어요.\n" +
                "배트는 비를 피하기 위해 옥수수밭으로 들어갔습니다.\n"),
        SourceModel.ImageModel(imageName = "test_book_id_img_17.png"),
        SourceModel.TextModel("\n옥수수밭에서 비를 피할만한 헛간이 보이는군요?\n")
    )
)

val logic3 : PageLogicModel = PageLogicModel(
    id = 3_00_00_00_00L,
    type = PageType.NORMAL,
    title = "옥수수밭에 가다",
    selectors = listOf(
        SelectorModel(
            id = 3_01_00_00_00L,
            text = "헛간을 향해 날아오른다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    id = 3_01_00_01_00L,
                    routePageId = 8_00_00_00_00L,
                )
            )
        ),
        SelectorModel(
            id = 3_02_00_00_00L,
            text = "바위 틈으로 들어간다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    id = 3_02_00_01_00L,
                    routePageId = 4_00_00_00_00L,
                )
            )
        )
    )
)

val page4 : PageModel = PageModel(
    id = 4_00_00_00_00L,
    title = "바위틈에서",
    sources = listOf(
        SourceModel.ImageModel(imageName = "test_book_id_img_25.png"),
        SourceModel.TextModel(description = "\n비를 피해 바위 틈으로 들어갔습니다. 그리고 배트는 겁에 질린채 꼼짝도 못했습니다.")
    )
)

val logic4 : PageLogicModel = PageLogicModel(
    id = 4_00_00_00_00L,
    type = PageType.ENDING,
    title = "바위틈에서",
    selectors = listOf()
)

val page5 : PageModel = PageModel(
    id = 5_00_00_00_00L,
    title = "플라이 투더 문",
    sources = listOf(
        SourceModel.TextModel(description = "\n달을 향해 날아오르는 배트!\n" +
                "하늘로 날아오르니 기분이 아주 좋아요!\n"),
        SourceModel.ImageModel(imageName = "test_book_id_img_4.png"),
        SourceModel.TextModel(description = "\n그런데 그 뿐이었습니다.\n"),
        SourceModel.ImageModel(imageName = "test_book_id_img_9.png"),
        SourceModel.TextModel(description = "밝은 달 아래에서 바라봐도 집이 어딘지 보이지 않아요.\n"),
    )
)

val logic5 : PageLogicModel = PageLogicModel(
    id = 5_00_00_00_00L,
    type = PageType.NORMAL,
    title = "플라이 투더 문",
    selectors = listOf(
        SelectorModel(
            id = 5_01_00_00_00L,
            text = "달로 다시 날아오른다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    id = 5_01_00_01_00L,
                    routePageId = 6_00_00_00_00L,
                    routeConditions = listOf(
                        ConditionModel(
                            id = 5_01_00_01_01L,
                            type = ConditionType.COUNT,
                            selfActionId = 5_01_00_00_00L,
                            count = 2,
                            relationOp = RelationOp.GREATER_THAN
                        )
                    )
                ),
                RouteModel(
                    id = 5_01_00_02_00L,
                    routePageId = 5_00_00_00_00L,
                )
            )
        ),
        SelectorModel(
            id = 5_02_00_00_00L,
            text = "옥수수밭으로 간다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    id = 5_02_00_01_00L,
                    routePageId = 3_00_00_00_00L,
                )
            )
        ),
        SelectorModel(
            id = 5_03_00_00_00L,
            text = "비가 오기 시작한다",
            showConditions = listOf(
                ConditionModel(
                    id = 5_03_01_00_00L,
                    type = ConditionType.COUNT,
                    selfActionId = 5_00_00_00_00L,
                    relationOp = RelationOp.EQUAL,
                    count = 3
                )
            ),
            routes = listOf(
                RouteModel(
                    id = 5_03_00_01_00L,
                    routePageId = 7_00_00_00_00L,
                )
            )
        )
    )
)

val page6 : PageModel = PageModel(
    id = 6_00_00_00_00L,
    title = "여긴 어디지?",
    sources = listOf(
        SourceModel.ImageModel(imageName = "test_book_id_img_35.png"),
        SourceModel.TextModel(description = "\n어느새 비가 그치고 날이 밝았습니다. 그런데 여긴 어디죠? 박쥐는 낮에 잘 보이지 않습니다. 길을 잃은 배트는 그렇게 정처없이 날았답니다..")
    )
)

val logic6 : PageLogicModel = PageLogicModel(
    id = 6_00_00_00_00L,
    type = PageType.ENDING,
    title = "여긴 어디지?",
    selectors = listOf()
)

val page7 : PageModel = PageModel(
    id = 7_00_00_00_00L,
    title = "어두운 밤에 비가 축축",
    sources = listOf(
        SourceModel.ImageModel(imageName = "test_book_id_img_14.png"),
        SourceModel.TextModel(description = "\n갑자기 비가 오기 시작하네요.\n" +
                "배트는 주위를 모두 살펴보았어요.\n" +
                "\n" +
                "저기 비를 피할만한 나무가 보이는군요?\n"),
        SourceModel.ImageModel(imageName = "test_book_id_img_11.png"),
        SourceModel.TextModel(description = "\n어떻게든 나무를 붙잡았습니다.\n" +
                "\"허억 허억. 겨우 살았네... 비만은 피할수 있겠어!\"\n" +
                "그러나 심상치 않은 바람이 몰려듭니다.\n\n"),
        SourceModel.ImageModel(imageName = "test_book_id_img_10.png"),
        SourceModel.TextModel(description = "\n\"으악 으아아아악!\"\n" +
                "비만 오는게 아니라 강풍이 몰아치는군요.\n" +
                "\n" +
                "배트는 단련된 팔근육으로 나무 위로 올라서는데 성공했어요.\n"),
        SourceModel.ImageModel(imageName = "test_book_id_img_13.png"),
        SourceModel.TextModel(description = "\n나무 위에서 주변을 둘러봅니다.\n" +
                "저 멀리 헛간이 보이는군요?\n"),

    )
)

val logic7 : PageLogicModel = PageLogicModel(
    id = 7_00_00_00_00L,
    type = PageType.NORMAL,
    title = "어두운 밤에 비가 축축",
    selectors = listOf(
        SelectorModel(
            id = 7_01_00_00_00L,
            text = "헛간을 향해 날아오른다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    id = 7_01_00_01_00L,
                    routePageId = 8_00_00_00_00L,
                )
            )
        )
    )
)

val page8 : PageModel = PageModel(
    id = 8_00_00_00_00L,
    title = "헛간을 향해 힘찬 날개짓!",
    sources = listOf(
        SourceModel.TextModel(description = "배트는 힘내어 헛간을 향해 날개짓합니다.\n"),
        SourceModel.ImageModel(imageName = "test_book_id_img_37.png"),
        SourceModel.TextModel(description = "\n\"하나! 둘! 좀만 더 힘을 내면 된다!\"\n" +
                "\"휘우우우우웅!\"\n" +
                "\n" +
                "강풍이 몰아칩니다.\n"),
        SourceModel.ImageModel(imageName = "test_book_id_img_15.png"),
        SourceModel.TextModel(description = "\n배트는 강풍에 그만 땅에 떨어졌습니다.\n"),
        SourceModel.ImageModel(imageName = "test_book_id_img_16.png"),
        SourceModel.TextModel(description = "\n땅에 박혀버린 배트… 이제 날 힘이 거의 없는걸까요?\n")
        )
)

val logic8 : PageLogicModel = PageLogicModel(
    id = 8_00_00_00_00L,
    type = PageType.NORMAL,
    title = "헛간을 향해 힘찬 날개짓!",
    selectors = listOf(
        SelectorModel(
            id = 8_01_00_00_00L,
            text = "다시한 번 날개짓한다.",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    id = 8_01_00_01_00L,
                    routePageId = 9_00_00_00_00L,
                    routeConditions = listOf(
                        ConditionModel(
                            id = 8_01_00_01_01L,
                            type = ConditionType.COUNT,
                            selfActionId = 7_00_00_00_00L,
                            count = 1,
                            relationOp = RelationOp.EQUAL
                        )
                    )
                ),
                RouteModel(
                    id = 8_01_00_02_00L,
                    routePageId = 6_00_00_00_00L,
                    routeConditions = listOf(
                        ConditionModel(
                            id = 8_01_00_02_01L,
                            type = ConditionType.COUNT,
                            selfActionId = 8_00_00_00_00L,
                            count = 3,
                            relationOp = RelationOp.GREATER_THAN
                        )
                    )
                ),
                RouteModel(
                    id = 8_01_00_03_00L,
                    routePageId = 8_00_00_00_00L,
                )
            )
        )
    )
)

val page9 : PageModel = PageModel(
    id = 9_00_00_00_00L,
    title = "지붕에 도착한 배트",
    sources = listOf(
        SourceModel.ImageModel(imageName = "test_book_id_img_19.png"),
        SourceModel.TextModel(description = "\n드디어 가까워진 헛간!\n" + "벌레를 먹었던 보람이 있군요?\n"),
        SourceModel.ImageModel(imageName = "test_book_id_img_20.png"),
        SourceModel.TextModel(description = "\n겨우 헛간 지붕에 도착했습니다.\n"),
        SourceModel.ImageModel(imageName = "test_book_id_img_21.png"),
        SourceModel.TextModel(description = "\n헛간 지붕에 왠 구멍이 보이네요.\n"),
        SourceModel.ImageModel(imageName = "test_book_id_img_22.png"),
        SourceModel.TextModel(description = "\n구멍은 배트가 들어가기엔 조금 좁아보이네요\n" + "저길로 가볼까요?\n\n")
    )
)

val logic9 : PageLogicModel = PageLogicModel(
    id = 9_00_00_00_00L,
    type = PageType.NORMAL,
    title = "지붕에 도착한 배트",
    selectors = listOf(
        SelectorModel(
            id = 9_01_00_00_00L,
            text = "가지 않고 비가 그치길 기다린다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    id = 9_01_00_01_00L,
                    routePageId = 11_00_00_00_00L,
                )
            )
        ),
        SelectorModel(
            id = 9_02_00_00_00L,
            text = "구멍으로 들어간다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    id = 9_02_00_01_00L,
                    routePageId = 10_00_00_00_00L,
                )
            )
        )
    )
)

val page10 : PageModel = PageModel(
    id = 10_00_00_00_00L,
    title = "조용히 어둠 속으로…",
    sources = listOf(
        SourceModel.ImageModel(imageName = "test_book_id_img_23.png"),
        SourceModel.TextModel(description = "\n구멍에 들어간 배트!\n"),
        SourceModel.ImageModel(imageName = "test_book_id_img_26.png"),
        SourceModel.TextModel(description = "\n여긴 어디죠? 어둡고 깜깜한 헛간에 배트는 갇히고 말았습니다.\n"),
        SourceModel.ImageModel(imageName = "test_book_id_img_28.png"),
        SourceModel.TextModel(description = "\n그러나 배트는 박쥐입니다. 거꾸로 메달린 배트는 조용히 천천히.. 잠에 듭니다.\n")

    )
)

val logic10 : PageLogicModel = PageLogicModel(
    id = 10_00_00_00_00L,
    type = PageType.ENDING,
    title = "조용히 어둠 속으로…",
    selectors = listOf()
)

val page11 : PageModel = PageModel(
    id = 11_00_00_00_00L,
    title = "다시 집으로!",
    sources = listOf(
        SourceModel.ImageModel(imageName = "test_book_id_img_29.png"),
        SourceModel.TextModel(description = "\n지붕에서 시간을 얼마나 보냈을까요? 어느덧 아침이 되어 배트는 기지개를 켭니다.\n" +
                "\n" +
                "\"후아아아앙! 이제 집으로 돌아가볼까?\"\n\n"),
        SourceModel.ImageModel(imageName = "test_book_id_img_3.png"),
        SourceModel.TextModel(description = "\n지붕 꼭대기에서 주변을 둘러봅니다. 새벽공기가 시원하군요? 저 멀리 옥수수밭이 보입니다. 저 곳을 지나면 분명 집이 있어요!\n"),
        SourceModel.ImageModel(imageName = "test_book_id_img_36.png"),
        SourceModel.TextModel(description = "\n배트는 열심히 옥수수밭을 가로질러 나아갑니다.\n" +
                "\n" +
                "언젠가 도착할 집을 향해 힘차게 날개짓을 합니다!\n")

    )
)

val logic11 : PageLogicModel = PageLogicModel(
    id = 11_00_00_00_00L,
    type = PageType.ENDING,
    title = "다시 집으로!",
    selectors = listOf()
)

val bookInfo : BookInfoModel = BookInfoModel(
    id = testEpisodeRef.bookId,

    introduce = IntroduceModel(),
    editor = EditorModel()
)

val episodeInfo : EpisodeInfoModel = EpisodeInfoModel(
    id = testEpisodeRef.episodeId,
    bookId = testEpisodeRef.bookId,
    title = "제 1화"
)

val sampleImageList = listOf(
    "test_book_id_img_1.png" to R.drawable.test_book_id_img_1,
    "test_book_id_img_2.png" to R.drawable.test_book_id_img_2,
    "test_book_id_img_3.png" to R.drawable.test_book_id_img_3,
    "test_book_id_img_4.png" to R.drawable.test_book_id_img_4,
    "test_book_id_img_5.png" to R.drawable.test_book_id_img_5,
    "test_book_id_img_6.png" to R.drawable.test_book_id_img_6,
    "test_book_id_img_7.png" to R.drawable.test_book_id_img_7,
    "test_book_id_img_8.png" to R.drawable.test_book_id_img_8,
    "test_book_id_img_9.png" to R.drawable.test_book_id_img_9,
    "test_book_id_img_10.png" to R.drawable.test_book_id_img_10,
    "test_book_id_img_11.png" to R.drawable.test_book_id_img_11,
    "test_book_id_img_12.png" to R.drawable.test_book_id_img_12,
    "test_book_id_img_13.png" to R.drawable.test_book_id_img_13,
    "test_book_id_img_14.png" to R.drawable.test_book_id_img_14,
    "test_book_id_img_15.png" to R.drawable.test_book_id_img_15,
    "test_book_id_img_16.png" to R.drawable.test_book_id_img_16,
    "test_book_id_img_17.png" to R.drawable.test_book_id_img_17,
    "test_book_id_img_18.png" to R.drawable.test_book_id_img_18,
    "test_book_id_img_19.png" to R.drawable.test_book_id_img_19,
    "test_book_id_img_20.png" to R.drawable.test_book_id_img_20,
    "test_book_id_img_21.png" to R.drawable.test_book_id_img_21,
    "test_book_id_img_22.png" to R.drawable.test_book_id_img_22,
    "test_book_id_img_23.png" to R.drawable.test_book_id_img_23,
    "test_book_id_img_24.png" to R.drawable.test_book_id_img_24,
    "test_book_id_img_25.png" to R.drawable.test_book_id_img_25,
    "test_book_id_img_26.png" to R.drawable.test_book_id_img_26,
    "test_book_id_img_27.png" to R.drawable.test_book_id_img_27,
    "test_book_id_img_28.png" to R.drawable.test_book_id_img_28,
    "test_book_id_img_29.png" to R.drawable.test_book_id_img_29,
    "test_book_id_img_30.png" to R.drawable.test_book_id_img_30,
    "test_book_id_img_31.png" to R.drawable.test_book_id_img_31,
    "test_book_id_img_32.png" to R.drawable.test_book_id_img_32,
    "test_book_id_img_33.png" to R.drawable.test_book_id_img_33,
    "test_book_id_img_34.png" to R.drawable.test_book_id_img_34,
    "test_book_id_img_35.png" to R.drawable.test_book_id_img_35,
    "test_book_id_img_36.png" to R.drawable.test_book_id_img_36,
    "test_book_id_img_37.png" to R.drawable.test_book_id_img_37,
)

val content : ContentModel = ContentModel(pages = listOf(page1, page2, page3, page4, page5, page6, page7, page8, page9, page10, page11))
val logic : LogicModel = LogicModel(id = 1L, logics = listOf(logic1, logic2, logic3, logic4, logic5, logic6, logic7, logic8, logic9, logic10, logic11))