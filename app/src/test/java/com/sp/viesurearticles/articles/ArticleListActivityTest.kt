package com.sp.viesurearticles.articles

import android.content.Intent
import android.os.Build
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.sp.viesurearticles.FakeArticleRepository
import com.sp.viesurearticles.R
import com.sp.viesurearticles.articledetail.ArticleDetailActivity
import com.sp.viesurearticles.articledetail.ArticleDetailViewModel
import com.sp.viesurearticles.data.entity.Article
import kotlinx.android.synthetic.main.activity_article_list.*
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(Build.VERSION_CODES.P))
@LooperMode(LooperMode.Mode.PAUSED)
class ArticleListActivityTest{

    private lateinit var articleRepository: FakeArticleRepository

    val article = Article(1,"Title", "Desc","Author", "25/4/2021",1,"image")
    val article2 = Article(2,"Title", "Desc","Author", "25/4/2021",1,"image")
    val article3 = Article(3,"Title", "Desc","Author", "25/4/2021",1,"image")
    val article4 = Article(4,"Title", "Desc","Author", "25/4/2021",1,"image")

    @Rule
    @JvmField
    val listRule = ActivityTestRule(ArticleListActivity::class.java, true, false)

    @Rule
    @JvmField
    val rule = ActivityTestRule(ArticleDetailActivity::class.java, true, false)

    lateinit var articlesViewModel: ArticlesViewModel

    @Before
    fun setup(){

        articleRepository = FakeArticleRepository()
        articlesViewModel = ArticlesViewModel(articleRepository)

        loadKoinModules(module(override = true) {
            viewModel {
                articlesViewModel
            }
        })
    }

    @After
    fun tearDown(){
        stopKoin()
    }

    @Test
    fun testDetailActivityListView() {
        articleRepository.addArticles(listOf(article, article2, article3, article4))
        listRule.launchActivity(null)

        onView(withId(R.id.articleList)).check(matches(isDisplayed()))
        assert(listRule.activity.articleList.adapter!!.itemCount == 4)

    }

    @Test
    fun testDetailActivityListItemPress() {
        articleRepository.shouldReturnError = true
        listRule.launchActivity(null)

        onView(withId(R.id.articleList)).check(matches(isDisplayed()))
        assert(listRule.activity.articleList.adapter!!.itemCount == 0)
        //test if error message shown
        onView(withId(R.id.snackbar_text)).check(matches(isDisplayed()))

    }


}