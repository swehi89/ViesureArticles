package com.sp.viesurearticles.articles

import android.os.Build
import com.jraska.livedata.test
import com.sp.viesurearticles.FakeArticleRepository
import com.sp.viesurearticles.R
import com.sp.viesurearticles.data.entity.Article
import com.sp.viesurearticles.util.Event
import com.sp.viesurearticles.util.EventObserver
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.math.absoluteValue

@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(Build.VERSION_CODES.P))
class ArticlesViewModelTest {
    private lateinit var articlesViewModel: ArticlesViewModel
    private lateinit var articleRepository: FakeArticleRepository

    val article = Article(1,"Title", "Desc","Author", "25/4/2021",null,"image")
    val article2 = Article(2,"Title2", "Desc2","Author2", "23/4/2021",null,"image2")
    val article3 = Article(3,"Title3", "Desc3","Author3", "25/5/2021",null,"image3")
    val article4 = Article(4,"Title4", "Desc4","Author4", "21/4/2021",null,"image4")
    private val remoteArticles = listOf(article, article2, article3, article4)


    @Before
    fun setupViewModel() {
        articleRepository = FakeArticleRepository()

        articlesViewModel = ArticlesViewModel(articleRepository)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun testBindViewModelTestSuccess() {
        articleRepository.addArticles(remoteArticles)

        runBlocking {
            articlesViewModel.bind()
        }
        val liveDataArticles = articlesViewModel.articles
        liveDataArticles.test().awaitValue().assertValue{
            it.size == 4
        }
    }

    @Test
    fun testBindViewModelTestErrorNoCached() {
        articleRepository.shouldReturnError = true

        runBlocking {
            articlesViewModel.bind()
        }
        val liveDateError = articlesViewModel.errorMessage
        liveDateError.test().awaitValue().assertValue{
            it == R.string.error_netwrok_no_cache
        }
    }

    @Test
    fun testBindViewModelTestErrorCached() {
        articleRepository.addArticles(remoteArticles)

        runBlocking {
            articlesViewModel.bind()
        }

        val liveDataArticles = articlesViewModel.articles
        liveDataArticles.test().awaitValue().assertValue{
            it.size == 4
        }

        articleRepository.shouldReturnError = true

        runBlocking {
            articlesViewModel.bind()
        }
        val liveDateError = articlesViewModel.errorMessage
        liveDateError.test().awaitValue().assertValue{
            it == R.string.error_network_cached
        }
    }

    @Test
    fun testBindViewModelOpenArticle() {
        articlesViewModel.openArticle(1)
        val liveDataOpenEvent = articlesViewModel.openArticleEvent
        liveDataOpenEvent.test().awaitValue().assertValue{
            it.peekContent() == 1
        }
    }


}