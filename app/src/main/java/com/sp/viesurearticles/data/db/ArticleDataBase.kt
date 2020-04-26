package com.sp.viesurearticles.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sp.viesurearticles.data.entity.Article

@Database(
    entities = [
        Article::class
    ],
    version = 1
)
abstract class ArticleDataBase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao

}