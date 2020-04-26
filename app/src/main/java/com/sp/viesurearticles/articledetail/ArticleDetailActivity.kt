package com.sp.viesurearticles.articledetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.sp.viesurearticles.R

import com.sp.viesurearticles.databinding.ActivityArticleDetailBinding
import kotlinx.android.synthetic.main.activity_article_list.*

import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ArticleDetailActivity : AppCompatActivity() {
    private val articleDetailViewModel by viewModel<ArticleDetailViewModel> { parametersOf(this.applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityArticleDetailBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_article_detail)
        binding.viewmodel = articleDetailViewModel
        binding.lifecycleOwner = this
        intent.let { articleDetailViewModel.bind(it.getIntExtra("id", -1)) }
        showErrorMessage()
        supportActionBar.let { it!!.setDisplayHomeAsUpEnabled(true) }

    }

    private fun showErrorMessage(){
        articleDetailViewModel.errorMessage.observe(this, Observer { res ->
            if (res != null) {
                Toast.makeText(this, res, Toast.LENGTH_LONG).show()
                finish()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
