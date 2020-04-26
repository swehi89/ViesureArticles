package com.sp.viesurearticles.data.di

import androidx.room.Room
import com.sp.viesurearticles.data.ArticlesRepository
import com.sp.viesurearticles.data.ArticlesRepositoryImpl
import com.sp.viesurearticles.data.db.ArticleDataBase
import com.sp.viesurearticles.data.network.ArticleApi
import com.sp.viesurearticles.data.source.ArticlesLocalDataSource
import com.sp.viesurearticles.data.source.ArticlesRemoteDataSource
import com.sp.viesurearticles.data.source.LocalDataSource
import com.sp.viesurearticles.data.source.RemoteDataSource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val articleModule = module {
    factory {
        val okHttp = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor()
                .apply { level = HttpLoggingInterceptor.Level.BASIC }
            )

        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(ArticleApi.BASE_URL)
            .client(okHttp.build())
            .build()
            .create(ArticleApi::class.java)
    }
    single { Room.databaseBuilder(get(), ArticleDataBase::class.java, "article").build() }
    factory<LocalDataSource> { ArticlesLocalDataSource(get()) }
    factory<RemoteDataSource> { ArticlesRemoteDataSource(get()) }
    factory<ArticlesRepository> { ArticlesRepositoryImpl(get(), get()) }

}
