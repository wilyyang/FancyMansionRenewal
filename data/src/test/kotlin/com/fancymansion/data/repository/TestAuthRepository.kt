package com.fancymansion.data.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.fancymansion.core.common.di.HiltCommon
import com.fancymansion.core.common.resource.StringValue
import com.fancymansion.core.common.wrapper.Success
import com.fancymansion.data.datasource.database.base.di.HiltRoomDatabaseHelper
import com.fancymansion.data.datasource.datastore.di.HiltDatastore
import com.fancymansion.data.datasource.network.source.di.HiltRetrofitDao
import com.fancymansion.data.datasource.network.source.model.BaseResponse
import com.fancymansion.data.datasource.network.source.model.response.LoginData
import com.fancymansion.domain.interfaceRepository.AuthRepository
import com.fancymansion.test.common.FileReader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.lang.reflect.Type
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class, sdk = [33])
@UninstallModules(HiltCommon::class, HiltDatastore::class, HiltRetrofitDao::class, HiltRoomDatabaseHelper::class)
class TestAuthRepository {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository : AuthRepository

    @Before
    fun setUp() = runTest {
        hiltRule.inject()
    }

    @Test
    fun api_userLogin_check_data() = runTest {
        val result = repository.userLogin(userId = "user1", password = "password")
        assertThat(result is Success).isTrue()
    }

    @Test
    fun api_autoLogin_check_data() = runTest {
        val result = repository.autoLogin()
        assertThat(result is Success).isTrue()
    }

    @Test
    fun pref_isAutoLogin_check() = runTest {
        assertThat(repository.getIsAutoLogin()).isTrue()
        repository.setIsAutoLogin(true)
        assertThat(repository.getIsAutoLogin()).isTrue()
    }

    @Test
    fun test_enum_inline_data() = runTest {
        val testContext: Context = ApplicationProvider.getApplicationContext()

        val test = FileReader.readRaw(testContext, com.fancymansion.test.R.raw.test_enum_inline)
        println("111:\n"+test)
        val type: Type = object : TypeToken<Condition>() {}.type
        val result = Gson().fromJson<Condition>(test, type)
        println("222:\n"+result)
        println("2221:\n"+result.logicalOp)
        println("2223:\n"+result.relationOp)
        val json = Gson().toJson(result)
        println("333:\n"+json)

        val test2 = Condition(
            id = ConditionId(4592324324),
            targetId = ConditionId(-30000),
            compareId = ConditionId(-30000),
            count = 443,
            relationOp = RelationOp.LESS_THAN,
            logicalOp = LogicalOp.OR
        )

        println("444:"+result.equals(test2))


    }
}

@JvmInline
value class ConditionId(val value: Long)
val CONDITION_TARGET_ID_NOT_ASSIGNED = ConditionId(-4000L)

enum class RelationOp(
    val localizedName: StringValue,
    val check: (Boolean, Boolean) -> Boolean
) {
    EQUAL(
        localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.relation_equal),
        check = { n1, n2 -> n1 == n2 }),
    NOT_EQUAL(
        localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.relation_not_equal),
        check = { n1, n2 -> n1 != n2 }),
    LESS_THAN(
        localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.relation_less_than),
        check = { n1, n2 -> n1 < n2 }),
    GREATER_THAN(
        localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.relation_greater_than),
        check = { n1, n2 -> n1 > n2 });


    companion object {
        fun from(name: String): RelationOp = entries.find { it.name == name } ?: EQUAL
    }
}

enum class LogicalOp(
    val localizedName: StringValue,
    val check: (Boolean, Boolean) -> Boolean
) {
    AND(
        localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.logical_and),
        check = { n1, n2 -> n1 && n2 }),
    OR(
        localizedName = StringValue.StringResource(com.fancymansion.core.common.R.string.logical_or),
        check = { n1, n2 -> n1 || n2 });

    companion object {
        fun from(name: String): LogicalOp = entries.find { it.name == name } ?: AND
    }
}

data class Condition(
    val id: ConditionId,
    val targetId: ConditionId = CONDITION_TARGET_ID_NOT_ASSIGNED,
    val compareId: ConditionId = CONDITION_TARGET_ID_NOT_ASSIGNED,
    val count: Int = 0,
    val relationOp: RelationOp = RelationOp.EQUAL,
    val logicalOp: LogicalOp = LogicalOp.AND
)
