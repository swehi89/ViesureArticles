package com.sp.viesurearticles.data

import androidx.lifecycle.LiveData
import com.sp.viesurearticles.data.entity.Article
import com.sp.viesurearticles.data.source.LocalDataSource
import com.sp.viesurearticles.data.source.RemoteDataSource
import com.sp.viesurearticles.util.Result

class ArticlesRepositoryImpl(private val local: LocalDataSource,
                             private val remote: RemoteDataSource) : ArticlesRepository {


    override fun observeArticles(): LiveData<Result<List<Article>>> {
        return local.observeArticles()
    }

    override suspend fun saveArticles(articles: List<Article>) {
        local.saveArticles(articles)
    }

    override suspend fun refreshArticles() : Result<List<Article>> {
        return try {
            Result.Success(remote.fetchArticles().also {
                saveArticles(it)
            })
        } catch (e : Exception){
            Result.Error(e)
        }
    }

    override fun observeArticle(articleId: Int): LiveData<Result<Article>> {
        return local.observeArticle(articleId)
    }
}