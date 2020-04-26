package com.sp.viesurearticles.articles

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.sp.viesurearticles.R
import com.sp.viesurearticles.articledetail.ArticleDetailActivity
import com.sp.viesurearticles.databinding.ActivityArticleListBinding
import com.sp.viesurearticles.util.EventObserver
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber
import androidx.core.app.ActivityOptionsCompat

import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_article_list.*
import kotlinx.android.synthetic.main.article_item.*


class ArticleListActivity : AppCompatActivity() {
    private val articlesViewModel by viewModel<ArticlesViewModel> { parametersOf(this.applicationContext) }

    private lateinit var binding: ActivityArticleListBinding
    private lateinit var listAdapter: ArticlesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_article_list)

        binding.viewmodel = articlesViewModel
        binding.lifecycleOwner = this
        articlesViewModel.bind()
        setupNavigation()
        setupListAdapter()
        showErrorMessage()
    }

    private fun setupNavigation() {
        articlesViewModel.openArticleEvent.observe(this, EventObserver {
            openListDetail(it)
        })
    }

    private fun setupListAdapter() {
        val viewModel = binding.viewmodel
        if (viewModel != null) {
            listAdapter = ArticlesAdapter(viewModel)
            binding.articleList.adapter = listAdapter
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }


    private fun openListDetail(id : Int){
        val intent = Intent(this, ArticleDetailActivity::class.java).apply {
            putExtra("id", id)
        }
        this.startActivity(intent)
    }

    private fun showErrorMessage(){
        articlesViewModel.errorMessage.observe(this, Observer { res ->
            if (res != null) {
                val snackbar = Snackbar.make(mainLayout, res, Snackbar.LENGTH_INDEFINITE)
                snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                snackbar.setActionTextColor(ContextCompat.getColor(this, android.R.color.white))
                snackbar.setAction("retry") {
                    articlesViewModel.bind()
                }.show()
            }
        })
    }
}
