package com.sp.viesurearticles.articles

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.sp.viesurearticles.ArticlesApplication
import com.sp.viesurearticles.R

import com.sp.viesurearticles.data.ArticlesRepository
import com.sp.viesurearticles.data.entity.Article
import com.sp.viesurearticles.util.Event
import com.sp.viesurearticles.util.Result
import com.sp.viesurearticles.util.isInternetAvailable
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ArticlesViewModel(private val repository : ArticlesRepository) : ViewModel(){

    private var _loading = MutableLiveData<Boolean>(false)
    val loading = _loading

    private var _articles = repository.observeArticles().map {
        mapLocalResult(it)
    }
    val articles : LiveData<List<Article>> = _articles

    private var _errorMessage = MutableLiveData<Int>()
    val errorMessage = _errorMessage

    private var _openArticleEvent = MutableLiveData<Event<Int>>()
    val openArticleEvent: LiveData<Event<Int>> = _openArticleEvent

    fun bind(){
        viewModelScope.launch {
            mapRemoteResult(refreshArticles())
        }
    }

    private suspend fun refreshArticles() = repository.refreshArticles()

    fun openArticle(articleId: Int) {
        _openArticleEvent.value = Event(articleId)
    }

    private fun mapRemoteResult(articleResult: Result<List<Article>>): List<Article> {
        _loading.value = false

        return if (articleResult is Result.Success) {
            articleResult.data
        } else {
            checkError(articleResult as Result.Error)
            ArrayList()
        }
    }

    private fun mapLocalResult(articleResult: Result<List<Article>>): List<Article> {
        return if (articleResult is Result.Success) {
            if (articleResult.data.isNotEmpty()){
                _loading.value = false
            }
            articleResult.data
        } else {
            ArrayList()
        }
    }

    private fun checkError(error : Result.Error){
        if (!isInternetAvailable(ArticlesApplication.appContext)){
            if (articles.value != null && articles.value!!.isNotEmpty()){
                setErrorMessage(R.string.error_network_cached)
            }else{
                setErrorMessage(R.string.error_netwrok_no_cache)
            }
        }else if (error.exception is HttpException){
                when(error.exception.code()){
                    401 -> setErrorMessage(R.string.error_http_401)
                    404 -> setErrorMessage(R.string.error_http_404)
                    else -> setErrorMessage(R.string.error_http_generic)
                }
            } else {
            setErrorMessage(R.string.error_http_generic)
        }
    }

    private fun setErrorMessage(@StringRes message : Int){
        _errorMessage.value = message
    }
}