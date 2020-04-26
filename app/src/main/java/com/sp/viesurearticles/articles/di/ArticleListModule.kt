package com.sp.viesurearticles.articles.di

import com.sp.viesurearticles.articles.ArticlesViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val articleViewModelModule = module {
    viewModel { ArticlesViewModel(get()) }
}
