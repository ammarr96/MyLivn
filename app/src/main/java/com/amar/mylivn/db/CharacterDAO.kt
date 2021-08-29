package com.amar.mylivn.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amar.mylivn.model.Character
import com.amar.mylivn.model.Comic

@Dao
interface CharacterDAO {

    @Query("SELECT * from characters ORDER BY databaseId")
    fun getAllCharacters(): LiveData<List<Character>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacters(list: List<Character>)

    @Query("DELETE from characters WHERE character_id = :characterId")
    fun deleteCharacterById(characterId: Int)

}