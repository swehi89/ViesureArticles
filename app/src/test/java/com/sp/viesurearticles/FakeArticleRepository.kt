package com.sp.viesurearticles

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sp.viesurearticles.data.ArticlesRepository
import com.sp.viesurearticles.data.entity.Article
import com.sp.viesurearticles.data.exception.NoResultException
import com.sp.viesurearticles.util.Result
import com.sp.viesurearticles.util.Result.Success
import java.util.LinkedHashMap

class FakeArticleRepository : ArticlesRepository {

    var savedArticles: LinkedHashMap<Int, Article> = LinkedHashMap()

    var shouldReturnError = false

    private val observableArticles = MutableLiveData<Result<List<Article>>>()
    private val observableArticle = MutableLiveData<Result<Article>>()

    override suspend fun refreshArticles(): Result<List<Article>> {
        if (shouldReturnError) {
            return Result.Error(NoResultException("Test exception"))
        }
        return Success(savedArticles.values.toList())
    }

    override fun observeArticles(): LiveData<Result<List<Article>>> {
        return observableArticles
    }

    override fun observeArticle(articleId: Int): LiveData<Result<Article>> {
        if (savedArticles[articleId] == null){
            observableArticle.value = Result.Error(NoResultException("Test exception"))
            return observableArticle
        }

        observableArticle.value = Success(savedArticles[articleId]!!)
        return observableArticle
    }

    override suspend fun saveArticles(articles: List<Article>) {
        articles.forEach{
            savedArticles[it.id] = it
        }
    }

    @VisibleForTesting
    fun addArticles(articles: List<Article>) {
        articles.forEach {
            savedArticles[it.id] = it
        }
        observableArticles.value = Success(savedArticles.values.toList())

    }

    @VisibleForTesting
    fun addArticle(articles: Article) {
        savedArticles[articles.id] = articles
        if (shouldReturnError) {
            observableArticle.value = Result.Error(NoResultException("Test exception"))
        }else{
            observableArticle.value = Success(articles)
        }
    }
}