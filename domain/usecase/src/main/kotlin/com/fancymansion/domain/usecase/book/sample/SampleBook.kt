package com.fancymansion.domain.usecase.book.sample

import com.fancymansion.core.common.const.LogicalOp
import com.fancymansion.core.common.const.PageType
import com.fancymansion.core.common.const.RelationOp
import com.fancymansion.core.common.const.testEpisodeRef
import com.fancymansion.domain.model.book.*
import com.fancymansion.domain.usecase.R


val page1 : PageModel = PageModel(
    id = 1,
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
    pageId = 1,
    type = PageType.START,
    title = "잠에서 깨어난 배트",
    selectors = listOf(
        SelectorModel(
            pageId = 1,
            selectorId = 1,
            text = "물가로 간다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    pageId = 1,
                    selectorId = 1,
                    routeId = 1,
                    routeTargetPageId = 2,
                )
            )
        ),
        SelectorModel(
            pageId = 1,
            selectorId = 2,
            text = "옥수수밭으로 간다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    pageId = 1,
                    selectorId = 2,
                    routeId = 1,
                    routeTargetPageId = 3,
                )
            )
        ),
        SelectorModel(
            pageId = 1,
            selectorId = 3,
            text = "숨겨진 행선지?",
            showConditions = listOf(
                ConditionModel.ShowSelectorConditionModel(
                    pageId = 1,
                    selectorId = 3,
                    conditionId = 1,
                    conditionRule = ConditionRuleModel.CountConditionRuleModel(
                        selfActionId = ActionIdModel(
                            pageId = 1
                        ),
                        relationOp = RelationOp.LESS_THAN,
                        logicalOp = LogicalOp.AND,
                        count = 7
                    )
                ),
                ConditionModel.ShowSelectorConditionModel(
                    pageId = 1,
                    selectorId = 3,
                    conditionId = 2,
                    conditionRule = ConditionRuleModel.CountConditionRuleModel(
                        selfActionId = ActionIdModel(
                            pageId = 2
                        ),
                        relationOp = RelationOp.GREATER_THAN,
                        logicalOp = LogicalOp.AND,
                        count = 1
                    )
                ),
                ConditionModel.ShowSelectorConditionModel(
                    pageId = 1,
                    selectorId = 3,
                    conditionId = 3,
                    conditionRule = ConditionRuleModel.TargetConditionRuleModel(
                        selfActionId = ActionIdModel(
                            pageId = 2
                        ),
                        relationOp = RelationOp.LESS_THAN,
                        logicalOp = LogicalOp.OR,
                        targetActionId = ActionIdModel(
                            pageId = 3
                        )
                    )
                )
            ),
            routes = listOf(
                RouteModel(
                    pageId = 1,
                    selectorId = 3,
                    routeId = 1,
                    routeTargetPageId = 11
                )
            )
        )
    )
)


val page2 : PageModel = PageModel(
    id = 2,
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
    pageId = 2,
    type = PageType.NORMAL,
    title = "물가에서의 사냥",
    selectors = listOf(
        SelectorModel(
            pageId = 2,
            selectorId = 1,
            text = "달을 향해 날아오른다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    pageId = 2,
                    selectorId = 1,
                    routeId = 1,
                    routeTargetPageId = 5
                )
            )
        ),
        SelectorModel(
            pageId = 2,
            selectorId = 2,
            text = "바위 틈으로 들어간다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    pageId = 2,
                    selectorId = 2,
                    routeId = 1,
                    routeTargetPageId = 4
                )
            )
        )
    )
)

val page3 : PageModel = PageModel(
    id = 3,
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
    pageId = 3,
    type = PageType.NORMAL,
    title = "옥수수밭에 가다",
    selectors = listOf(
        SelectorModel(
            pageId = 3,
            selectorId = 1,
            text = "헛간을 향해 날아오른다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    pageId = 3,
                    selectorId = 1,
                    routeId = 1,
                    routeTargetPageId = 8,
                )
            )
        ),
        SelectorModel(
            pageId = 3,
            selectorId = 2,
            text = "바위 틈으로 들어간다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    pageId = 3,
                    selectorId = 2,
                    routeId = 1,
                    routeTargetPageId = 4,
                )
            )
        )
    )
)

val page4 : PageModel = PageModel(
    id = 4,
    title = "바위틈에서",
    sources = listOf(
        SourceModel.ImageModel(imageName = "test_book_id_img_25.png"),
        SourceModel.TextModel(description = "\n비를 피해 바위 틈으로 들어갔습니다. 그리고 배트는 겁에 질린채 꼼짝도 못했습니다.")
    )
)

val logic4 : PageLogicModel = PageLogicModel(
    pageId = 4,
    type = PageType.NORMAL,
    title = "바위틈에서",
    selectors = listOf(
        SelectorModel(
            pageId = 4,
            selectorId = 1,
            text = "다시금 돌아간다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    pageId = 4,
                    selectorId = 1,
                    routeId = 1,
                    routeTargetPageId = 1,
                )
            )
        )
    )
)

val page5 : PageModel = PageModel(
    id = 5,
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
    pageId = 5,
    type = PageType.NORMAL,
    title = "플라이 투더 문",
    selectors = listOf(
        SelectorModel(
            pageId = 5,
            selectorId = 1,
            text = "달로 다시 날아오른다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    pageId = 5,
                    selectorId = 1,
                    routeId = 1,
                    routeTargetPageId = 6,
                    routeConditions = listOf(
                        ConditionModel.RouteConditionModel(
                            pageId = 5,
                            selectorId = 1,
                            routeId = 1,
                            conditionId = 1,
                            conditionRule = ConditionRuleModel.CountConditionRuleModel(
                                selfActionId = ActionIdModel(
                                    pageId = 5,
                                    selectorId = 1
                                ),
                                count = 2,
                                relationOp = RelationOp.GREATER_THAN
                            )
                        )
                    )
                ),
                RouteModel(
                    pageId = 5,
                    selectorId = 1,
                    routeId = 2,
                    routeTargetPageId = 5,
                )
            )
        ),
        SelectorModel(
            pageId = 5,
            selectorId = 2,
            text = "옥수수밭으로 간다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    pageId = 5,
                    selectorId = 2,
                    routeId = 1,
                    routeTargetPageId = 3,
                )
            )
        ),
        SelectorModel(
            pageId = 5,
            selectorId = 3,
            text = "비가 오기 시작한다",
            showConditions = listOf(
                ConditionModel.ShowSelectorConditionModel(
                    pageId = 5,
                    selectorId = 3,
                    conditionId = 1,
                    conditionRule = ConditionRuleModel.CountConditionRuleModel(
                        selfActionId = ActionIdModel(
                            pageId = 5
                        ),
                        relationOp = RelationOp.EQUAL,
                        count = 3
                    )
                )
            ),
            routes = listOf(
                RouteModel(
                    pageId = 5,
                    selectorId = 3,
                    routeId = 1,
                    routeTargetPageId = 7,
                )
            )
        )
    )
)

val page6 : PageModel = PageModel(
    id = 6,
    title = "여긴 어디지?",
    sources = listOf(
        SourceModel.ImageModel(imageName = "test_book_id_img_35.png"),
        SourceModel.TextModel(description = "\n어느새 비가 그치고 날이 밝았습니다. 그런데 여긴 어디죠? 박쥐는 낮에 잘 보이지 않습니다. 길을 잃은 배트는 그렇게 정처없이 날았답니다..")
    )
)

val logic6 : PageLogicModel = PageLogicModel(
    pageId = 6,
    type = PageType.ENDING,
    title = "여긴 어디지?",
    selectors = listOf()
)

val page7 : PageModel = PageModel(
    id = 7,
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
    pageId = 7,
    type = PageType.NORMAL,
    title = "어두운 밤에 비가 축축",
    selectors = listOf(
        SelectorModel(
            pageId = 7,
            selectorId = 1,
            text = "헛간을 향해 날아오른다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    pageId = 7,
                    selectorId = 1,
                    routeId = 1,
                    routeTargetPageId = 8,
                )
            )
        )
    )
)

val page8 : PageModel = PageModel(
    id = 8,
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
    pageId = 8,
    type = PageType.NORMAL,
    title = "헛간을 향해 힘찬 날개짓!",
    selectors = listOf(
        SelectorModel(
            pageId = 8,
            selectorId = 1,
            text = "다시한 번 날개짓한다.",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    pageId = 8,
                    selectorId = 1,
                    routeId = 1,
                    routeTargetPageId = 9,
                    routeConditions = listOf(
                        ConditionModel.RouteConditionModel(
                            pageId = 8,
                            selectorId = 1,
                            routeId = 1,
                            conditionId = 1,
                            conditionRule = ConditionRuleModel.CountConditionRuleModel(
                                selfActionId = ActionIdModel(
                                    pageId = 7
                                ),
                                count = 1,
                                relationOp = RelationOp.EQUAL
                            )
                        )
                    )
                ),
                RouteModel(
                    pageId = 8,
                    selectorId = 1,
                    routeId = 2,
                    routeTargetPageId = 6,
                    routeConditions = listOf(
                        ConditionModel.RouteConditionModel(
                            pageId = 8,
                            selectorId = 1,
                            routeId = 2,
                            conditionId = 1,
                            conditionRule = ConditionRuleModel.CountConditionRuleModel(
                                selfActionId = ActionIdModel(
                                    pageId = 8
                                ),
                                count = 3,
                                relationOp = RelationOp.GREATER_THAN
                            )
                        )
                    )
                ),
                RouteModel(
                    pageId = 8,
                    selectorId = 1,
                    routeId = 3,
                    routeTargetPageId = 8,
                )
            )
        )
    )
)

val page9 : PageModel = PageModel(
    id = 9,
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
    pageId = 9,
    type = PageType.NORMAL,
    title = "지붕에 도착한 배트",
    selectors = listOf(
        SelectorModel(
            pageId = 9,
            selectorId = 1,
            text = "가지 않고 비가 그치길 기다린다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    pageId = 9,
                    selectorId = 1,
                    routeId = 1,
                    routeTargetPageId = 11,
                )
            )
        ),
        SelectorModel(
            pageId = 9,
            selectorId = 2,
            text = "구멍으로 들어간다",
            showConditions = listOf(),
            routes = listOf(
                RouteModel(
                    pageId = 9,
                    selectorId = 2,
                    routeId = 1,
                    routeTargetPageId = 10,
                )
            )
        )
    )
)

val page10 : PageModel = PageModel(
    id = 10,
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
    pageId = 10,
    type = PageType.ENDING,
    title = "조용히 어둠 속으로…",
    selectors = listOf()
)

val page11 : PageModel = PageModel(
    id = 11,
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
    pageId = 11,
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