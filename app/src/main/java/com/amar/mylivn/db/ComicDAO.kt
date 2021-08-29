package com.amar.mylivn.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amar.mylivn.model.Character
import com.amar.mylivn.model.Comic

@Dao
interface ComicDAO {

    @Query("SELECT * from comics")
    fun getAllComics(): LiveData<List<Comic>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComics(list: List<Comic>)

    @Query("SELECT * from comics WHERE character_id = :characterId ORDER BY databaseId")
    fun getComicsByCharacterId(characterId: Int): List<Comic>

    @Query("DELETE from comics WHERE comic_id = :comicId")
    fun deleteComiscById(comicId: Int)

}