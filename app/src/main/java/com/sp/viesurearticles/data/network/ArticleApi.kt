package com.sp.viesurearticles.data.network

import com.sp.viesurearticles.data.entity.Article
import retrofit2.http.GET

interface ArticleApi {
    companion object {
        const val BASE_URL = "https://viesure.free.beeceptor.com"
    }

    @GET("/articles")
    suspend fun fetchArticles(): List<Article>

}