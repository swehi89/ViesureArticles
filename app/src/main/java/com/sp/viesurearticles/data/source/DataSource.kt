package com.sp.viesurearticles.data.source

import androidx.lifecycle.LiveData
import com.sp.viesurearticles.data.entity.Article
import com.sp.viesurearticles.util.Result

interface LocalDataSource {

    fun observeArticles(): LiveData<Result<List<Article>>>

    fun observeArticle(articleId: Int): LiveData<Result<Article>>

    suspend fun saveArticles(articles: List<Article>)
}

interface RemoteDataSource {

    suspend fun fetchArticles() : List<Article>

}