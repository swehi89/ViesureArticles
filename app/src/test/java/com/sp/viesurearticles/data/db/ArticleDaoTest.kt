package com.sp.viesurearticles.data.db

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.jraska.livedata.test
import com.sp.viesurearticles.data.entity.Article
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(Build.VERSION_CODES.P))
class ArticleDaoTest {
    private lateinit var database: ArticleDataBase

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            ArticleDataBase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        database.close()
        stopKoin()
    }


    @Test
    fun testInsertTaskAndGetById() = runBlockingTest {
        //add articles to list
        val article = Article(1,"Title", "Desc","Author", "25/4/2021",null,"image")
        val article2 = Article(2,"Title2", "Desc2","Author2", "26/4/2021",null,"image2")
        val list = ArrayList<Article>()
        list.add(article)
        list.add(article2)
        database.articleDao().insertAll(list)

        val loaded = database.articleDao().getArticleById(article.id)

        MatcherAssert.assertThat<Article>(loaded as Article, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(loaded.id, CoreMatchers.`is`(article.id))
        MatcherAssert.assertThat(loaded.title, CoreMatchers.`is`(article.title))
        MatcherAssert.assertThat(loaded.description, CoreMatchers.`is`(article.description))
        MatcherAssert.assertThat(loaded.author, CoreMatchers.`is`(article.author))
        MatcherAssert.assertThat(loaded.releaseDate, CoreMatchers.`is`(article.releaseDate))
        MatcherAssert.assertThat(loaded.image, CoreMatchers.`is`(article.image))
    }

    @Test
    fun testObserArticleById() = runBlockingTest {
        val article = Article(1,"Title", "Desc","Author", "25/4/2021",1,"image")
        val article2 = Article(2,"Title2", "Desc2","Author2", "26/4/2021",null,"image2")
        val list = ArrayList<Article>()
        list.add(article)
        list.add(article2)
        database.articleDao().insertAll(list)

        val liveData = database.articleDao().observeArticleById(1)
        liveData.test().awaitValue().assertValue{
            it!!.id == 1
        }

    }

    @Test
    fun testInsertAndGetAllLiveData() = runBlockingTest {
        val article = Article(1,"Title", "Desc","Author", "25/4/2021",1,"image")
        val article2 = Article(2,"Title2", "Desc2","Author2", "26/4/2021",null,"image2")
        val article3 = Article(3,"Title2", "Desc2","Author2", "26/4/2021",3,"image2")

        val list = ArrayList<Article>()
        list.add(article)
        list.add(article2)
        list.add(article3)
        database.articleDao().insertAll(list)

        val liveData = database.articleDao().observeArticles()
        liveData.test().awaitValue().assertValue{
            it.size == 2 && it[0].date!! > it[1].date!!
        }

    }
}