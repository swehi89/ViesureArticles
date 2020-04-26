package com.sp.viesurearticles.data

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import com.sp.viesurearticles.R
import com.sp.viesurearticles.data.entity.Article
import com.sp.viesurearticles.data.source.*
import com.sp.viesurearticles.data.source.mock.MockLocalDataSource
import com.sp.viesurearticles.data.source.mock.MockRemoteDataSource
import com.sp.viesurearticles.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(Build.VERSION_CODES.P))
class ArticlesRepositoryImplTest {
    private val article = Article(1,"Title", "Desc","Author", "25/4/2021",1,"image")
    private val article2 = Article(2,"Title2", "Desc2","Author2", "25/4/2021",2,"image")
    private val article3 = Article(3,"Title2", "Desc3","Author3", "25/4/2021",32,"image3")
    private val remoteArticles = listOf(article, article2, article3)
    private val localArticles = listOf(article)
    private lateinit var articleRemoteDataSource : RemoteDataSource
    private lateinit var articleLocalDataSource : LocalDataSource

    private lateinit var articlesRepository: ArticlesRepositoryImpl

    // Set the main coroutines dispatcher for unit testing.
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createRepository() {
        articleRemoteDataSource =
            MockRemoteDataSource(remoteArticles)
        articleLocalDataSource =
            MockLocalDataSource(localArticles.toMutableList())
        // Get a reference to the class under test
        articlesRepository = ArticlesRepositoryImpl(
            articleLocalDataSource, articleRemoteDataSource
        )
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun testObserveArticles(){
        val liveDataArticles = articlesRepository.observeArticles()
        liveDataArticles.test().awaitValue().assertValue{
            (it as Result.Success).data.size == 1 && it.data[0].id == article.id
        }
    }

    @Test
    fun testFetchArticles(){
        runBlocking{
            var articles = articlesRepository.refreshArticles()
            assert((articles as Result.Success).data.size == 3)
            //check if
            val liveDataArticlesUpdated = articlesRepository.observeArticles()
            liveDataArticlesUpdated.test().awaitValue().assertValue{
                (it as Result.Success).data.size == 4
            }
        }
    }

    @Test
    fun testObserveArticleById(){
        val liveDataArticles = articlesRepository.observeArticle(1)
        liveDataArticles.test().awaitValue().assertValue{
            (it as Result.Success).data.id == 1
        }
    }

    @Test
    fun testSaveArticles(){
        val liveDataArticles = articlesRepository.observeArticles()
        liveDataArticles.test().awaitValue().assertValue{
            (it as Result.Success).data.size == 1
        }

        runBlocking {
            articlesRepository.saveArticles(remoteArticles)
        }

        val liveDataArticlesUpdated = articlesRepository.observeArticles()
        liveDataArticlesUpdated.test().awaitValue().assertValue{
            (it as Result.Success).data.size == 4
        }
    }

}