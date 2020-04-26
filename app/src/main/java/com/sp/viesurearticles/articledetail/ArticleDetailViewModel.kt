package com.sp.viesurearticles.articledetail

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.sp.viesurearticles.data.ArticlesRepository
import com.sp.viesurearticles.data.entity.Article
import com.sp.viesurearticles.util.Result
import com.sp.viesurearticles.R

class ArticleDetailViewModel(private val repository : ArticlesRepository) : ViewModel(){

    private val _articleId = MutableLiveData<Int>()

    private var _article = _articleId.switchMap { articleId ->
        repository.observeArticle(articleId).map { mapResult(it) }
    }
    val article: LiveData<Article?> = _article

    private var _errorMessage = MutableLiveData<Int>()
    val errorMessage = _errorMessage

    fun bind(articleId : Int){
        _articleId.value = articleId
    }

    private fun mapResult(articleResult: Result<Article>): Article? {
        return if (articleResult is Result.Success) {
            articleResult.data
        } else {
            setErrorMessage(R.string.error_detail_not_found)
            null
        }
    }

    private fun setErrorMessage(@StringRes message : Int){
        _errorMessage.value = message
    }
}