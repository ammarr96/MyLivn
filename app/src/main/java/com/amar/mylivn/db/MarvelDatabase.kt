package com.amar.mylivn.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amar.mylivn.model.Character
import com.amar.mylivn.model.Comic

@Database(entities = [Character::class, Comic::class], version = 4)
abstract class MarvelDatabase: RoomDatabase() {

    abstract fun characterDao(): CharacterDAO
    abstract fun comicDao(): ComicDAO

}