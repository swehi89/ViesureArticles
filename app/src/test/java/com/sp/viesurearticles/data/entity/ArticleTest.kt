package com.sp.viesurearticles.data.entity

import org.junit.Test

class ArticleTest {
    val article = Article(1,"Title", "Desc","Author", "25/4/2021",null,"image")
    val articleForError = Article(1,"Title", "Desc","Author", "12321",null,"image")


    @Test
    fun testReleaseDateFormatToMillis(){
        assert(article.releaseDateFormatToMillis() != null && article.releaseDateFormatToMillis() is Long)
    }

    @Test
    fun testReleaseDateFormatToMillisError(){
        assert(articleForError.releaseDateFormatToMillis() == null)
    }

    @Test
    fun testReleaseDateFormatToString(){
        assert(article.releaseDateFormatToString() == "Sat, Apr 25, '21")
    }

    @Test
    fun testReleaseDateFormatToStringError(){
        assert(articleForError.releaseDateFormatToString() == "")
    }


}