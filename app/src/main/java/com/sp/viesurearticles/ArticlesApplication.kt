package com.sp.viesurearticles

import android.app.Application
import android.content.Context
import com.sp.viesurearticles.articledetail.di.articleDetailViewModelModule
import com.sp.viesurearticles.articles.di.articleViewModelModule
import com.sp.viesurearticles.data.di.articleModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class ArticlesApplication : Application() {


    companion object {
        lateinit  var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        startKoin {
            androidLogger()
            androidContext(this@ArticlesApplication)
            modules(listOf(
                articleModule,
                articleViewModelModule,
                articleDetailViewModelModule
            ))
        }
    }

    fun getContext() : Context = this.getContext()
}