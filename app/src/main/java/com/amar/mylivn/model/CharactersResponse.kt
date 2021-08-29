package com.amar.mylivn.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CharactersResponse(

    @SerializedName("data")
    var data: CharactersResponseData

)

data class CharactersResponseData (

    @SerializedName("offset")
    var offset: Int,

    @SerializedName("limit")
    var limit: Int,

    @SerializedName("total")
    var total: Int,

    @SerializedName("count")
    var count: Int,

    @SerializedName("results")
    var results: ArrayList<Character>
)

@Entity(tableName = "characters")
data class Character(

    @PrimaryKey(autoGenerate = true)
    var databaseId: Int,

    @SerializedName("id")
    @ColumnInfo(name = "character_id")
    var characterId: Int,

    @SerializedName("name")
    @ColumnInfo(name = "name")
    var title: String,

    @Embedded
    @SerializedName("thumbnail")
    var thumbnail: Thumbnail

) : Serializable


data class Thumbnail(

    @ColumnInfo(name = "path", defaultValue = "")
    @SerializedName("path")
    var path: String,

    @ColumnInfo(name = "extension", defaultValue = "")
    @SerializedName("extension")
    var extension: String,

    ) : Serializable
