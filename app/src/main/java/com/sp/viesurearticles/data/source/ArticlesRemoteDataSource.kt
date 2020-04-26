package com.sp.viesurearticles.data.source

import com.sp.viesurearticles.data.network.ArticleApi

class ArticlesRemoteDataSource(
    private val articleApi : ArticleApi
) : RemoteDataSource{

    override suspend fun fetchArticles() = articleApi.fetchArticles()

}