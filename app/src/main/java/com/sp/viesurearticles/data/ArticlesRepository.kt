package com.sp.viesurearticles.data

import androidx.lifecycle.LiveData
import com.sp.viesurearticles.data.entity.Article
import com.sp.viesurearticles.util.Result
interface ArticlesRepository{

    suspend fun refreshArticles() : Result<List<Article>>

    fun observeArticles(): LiveData<Result<List<Article>>>

    fun observeArticle(articleId: Int): LiveData<Result<Article>>

    suspend fun saveArticles(articles: List<Article>)

}