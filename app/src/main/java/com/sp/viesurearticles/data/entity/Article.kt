package com.sp.viesurearticles.data.entity

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "articles")
data class Article (
    @PrimaryKey @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "author") var author: String = "",
    @SerializedName("release_date") @ColumnInfo(name = "release_date") var releaseDate: String = "",
    @ColumnInfo(name = "date") @Nullable var date: Long?,
    @ColumnInfo(name = "image") var image: String = ""
    ){

    fun releaseDateFormatToMillis() : Long? {
        return try {
            val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(releaseDate)
            date!!.time
        }catch (e : Exception){
            null
        }

    }

    fun releaseDateFormatToString() : String {
        return try {
            val parsedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(releaseDate)
            val targetFormat = SimpleDateFormat("MMM d, ''yy", Locale.getDefault())
            val dayFormat = SimpleDateFormat("EEE, ", Locale.getDefault())
            val previousDay = Date()
            previousDay.time = parsedDate.time - 3600 * 1000
            var dateString = dayFormat.format(previousDay) + targetFormat.format(parsedDate!!)
            dateString
        }catch (e : Exception){
            ""
        }
    }
}
