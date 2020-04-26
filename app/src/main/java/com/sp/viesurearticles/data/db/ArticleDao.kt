package com.sp.viesurearticles.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sp.viesurearticles.data.entity.Article


@Dao
interface ArticleDao {

    @Query("SELECT * FROM articles WHERE date NOT NULL ORDER BY date DESC")
    fun observeArticles(): LiveData<List<Article>>

    @Query("SELECT * FROM articles WHERE id = :articleId")
    fun observeArticleById(articleId: Int): LiveData<Article?>

    @Query("SELECT * FROM articles WHERE id = :articleId")
    suspend fun getArticleById(articleId: Int): Article?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<Article>)
}
