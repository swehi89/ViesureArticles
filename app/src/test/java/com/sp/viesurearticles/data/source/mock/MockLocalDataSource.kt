package com.sp.viesurearticles.data.source.mock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.sp.viesurearticles.data.entity.Article
import com.sp.viesurearticles.data.source.LocalDataSource
import com.sp.viesurearticles.util.Result

class MockLocalDataSource(var articles: MutableList<Article> = mutableListOf()) :
    LocalDataSource {

    private var items = MutableLiveData<List<Article>>(articles)
    private var item = MutableLiveData<Article>(articles[0])
    //val articles : LiveData<List<Article>> = _items

    override fun observeArticles(): LiveData<Result<List<Article>>> {
        return items.map {
            Result.Success(it)
        }
    }

    override fun observeArticle(articleId: Int): LiveData<Result<Article>> {
        return item.map {
            Result.Success(it)
        }
    }

    override suspend fun saveArticles(articleList: List<Article>) {
        articles.addAll(articleList)
        items.value = articles
    }

}