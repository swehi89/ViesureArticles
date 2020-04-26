package com.sp.viesurearticles.articledetail

import android.os.Build
import androidx.annotation.StringRes
import com.jraska.livedata.test
import com.sp.viesurearticles.FakeArticleRepository
import com.sp.viesurearticles.R
import com.sp.viesurearticles.data.entity.Article
import com.sp.viesurearticles.util.Result
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(Build.VERSION_CODES.P))
class ArticleDetailViewModelTest {
    // Subject under test
    private lateinit var articleDetailViewModel: ArticleDetailViewModel
    private lateinit var articleRepository: FakeArticleRepository

    val article = Article(1,"Title", "Desc","Author", "25/4/2021",null,"image")


    @Before
    fun setupViewModel() {
        articleRepository = FakeArticleRepository()
        articleRepository.savedArticles[article.id] = article

        articleDetailViewModel = ArticleDetailViewModel(articleRepository)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun testGetArticleFromRepository() {
        articleDetailViewModel.bind(article.id)

        val liveData = articleRepository.observeArticle(1)
        liveData.test().awaitValue().assertValue{
            (it as Result.Success).data.id == article.id
        }

        //test article livedata mapResult
        val liveDataArticle = articleDetailViewModel.article
        liveDataArticle.test().awaitValue().assertValue{
            it!!.id == article.id
        }
    }

    @Test
    fun testGetNonExistingArticleFromRepository() {
        articleDetailViewModel.bind(2)

        val liveData = articleRepository.observeArticle(2)
        liveData.test().awaitValue().assertValue{
            it is Result.Error
        }

        //test article livedata mapResult
        val liveDataArticle = articleDetailViewModel.article
        liveDataArticle.test().awaitValue().assertValue{
            it == null
        }

        //test setErrorMessage
        val liveDataError = articleDetailViewModel.errorMessage
        liveDataError.test().awaitValue().assertValue{
            it == R.string.error_detail_not_found
        }
    }

}