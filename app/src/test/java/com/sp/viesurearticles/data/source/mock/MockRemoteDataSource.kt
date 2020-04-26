package com.sp.viesurearticles.data.source.mock

import com.sp.viesurearticles.data.entity.Article
import com.sp.viesurearticles.data.source.RemoteDataSource

class MockRemoteDataSource(var articles: List<Article>) :
    RemoteDataSource {
    override suspend fun fetchArticles(): List<Article> {
        return articles
    }

}