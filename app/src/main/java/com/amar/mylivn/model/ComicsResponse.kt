package com.amar.mylivn.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class ComicsResponse(

    @SerializedName("data")
    var data: ComicsResponseData

)

data class ComicsResponseData (

    @SerializedName("offset")
    var offset: Int,

    @SerializedName("limit")
    var limit: Int,

    @SerializedName("total")
    var total: Int,

    @SerializedName("count")
    var count: Int,

    @SerializedName("results")
    var results: ArrayList<Comic>
)

@Entity(tableName = "comics")
data class Comic(

    @PrimaryKey(autoGenerate = true)
    var databaseId: Int,

    @SerializedName("id")
    @ColumnInfo(name = "comic_id")
    var comicId: Int,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    var title: String,

    @Embedded
    @SerializedName("thumbnail")
    var thumbnail: Thumbnail,

    @ColumnInfo(name = "character_id")
    @SerializedName("character_id")
    var characterId: Int?,

) : Serializable
