package com.sp.viesurearticles.articledetail.di

import com.sp.viesurearticles.articledetail.ArticleDetailViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val articleDetailViewModelModule = module {
    viewModel { ArticleDetailViewModel(get()) }
}