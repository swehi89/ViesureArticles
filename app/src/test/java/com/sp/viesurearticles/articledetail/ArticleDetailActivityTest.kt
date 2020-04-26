package com.sp.viesurearticles.articledetail

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
import com.sp.viesurearticles.articles.ArticleListActivity
import com.sp.viesurearticles.data.entity.Article
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
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed

@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(Build.VERSION_CODES.P))
class ArticleDetailActivityTest{

    private lateinit var articleRepository: FakeArticleRepository

    val article = Article(1,"Title", "Desc","Author", "25/4/2021",null,"image")

    @Rule
    @JvmField
    val listRule = ActivityTestRule(ArticleListActivity::class.java, true, true)

    @Rule
    @JvmField
    val rule = ActivityTestRule(ArticleDetailActivity::class.java, true, false)

    lateinit var articleDetailViewModel: ArticleDetailViewModel

    @Before
    fun setup(){
        articleRepository = FakeArticleRepository()
        articleRepository.addArticle(article)
        articleDetailViewModel = ArticleDetailViewModel(articleRepository)

        loadKoinModules(module(override = true) {
            viewModel {
                articleDetailViewModel
            }
        })
    }

    @After
    fun tearDown(){
        stopKoin()
    }

    @Test
    fun testDetailActivityFields() {
        val intent = Intent(listRule.activity, ArticleDetailActivity::class.java).apply {
            putExtra("id", 1)
        }
        rule.launchActivity(intent)

        onView(withId(R.id.authorTextView)).check(matches(withText(articleDetailViewModel.article.value!!.author)))
        onView(withId(R.id.titleTextView)).check(matches(withText(articleDetailViewModel.article.value!!.title)))
        onView(withId(R.id.descriptionTextView)).check(matches(withText(articleDetailViewModel.article.value!!.description)))
        onView(withId(R.id.dateTextView)).check(matches(withText(articleDetailViewModel.article.value!!.releaseDateFormatToString())))
        onView(withId(R.id.imageView)).check(matches(isDisplayed()))

    }

    @Test
    fun testDetailActivityFieldsWhenWrongId() {
        val intent = Intent(listRule.activity, ArticleDetailActivity::class.java).apply {
            putExtra("id", -1)
        }
        rule.launchActivity(intent)
        assert(rule.activity.isFinishing)
    }

}