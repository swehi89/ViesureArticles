package com.sp.viesurearticles.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.sp.viesurearticles.data.db.ArticleDataBase
import com.sp.viesurearticles.data.entity.Article
import com.sp.viesurearticles.data.exception.NoResultException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.sp.viesurearticles.util.Result
import com.sp.viesurearticles.util.Result.Success

class ArticlesLocalDataSource(
    private val articleDataBase: ArticleDataBase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): LocalDataSource{

    //result
    override fun observeArticles(): LiveData<Result<List<Article>>> {
        return articleDataBase.articleDao().observeArticles().map {
            Success(it)
        }
    }

    override fun observeArticle(articleId: Int): LiveData<Result<Article>> {
        return articleDataBase.articleDao().observeArticleById(articleId).map {
            if (it != null){
                Success(it)
            }else{
                Result.Error(NoResultException("Can't find article"))
            }
        }
    }

    override suspend fun saveArticles(articles: List<Article>) = withContext(ioDispatcher) {
        articles.forEach{article ->
            article.date = article.releaseDateFormatToMillis()
        }
        articleDataBase.articleDao().insertAll(articles)
    }

}