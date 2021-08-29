package com.amar.mylivn.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amar.mylivn.model.Character
import com.amar.mylivn.model.Comic
import com.amar.mylivn.repository.CharactersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val charactersRepository: CharactersRepository
) : ViewModel() {

    var characterLiveData: LiveData<List<Character>> = MutableLiveData()
    var comicsLiveData: MutableLiveData<List<Comic>> = MutableLiveData()


    init {
        comicsLiveData = charactersRepository.comicList
    }

    fun getCharactersData()
    {
        characterLiveData = charactersRepository.getCharacters()
    }

    fun getComicsData(characterId: Int)
    {
        charactersRepository.getComics(characterId)
    }


    fun getCharactersFromAPIAndStore(limit: Int, offset: Int) {
        charactersRepository.getCharactersFromApi(limit, offset)
    }

    fun getComicsFromAPIAndStore(characterId: Int, limit: Int, offset: Int) {
        charactersRepository.getComicsFromApi(characterId, limit, offset)
        getComicsData(characterId)
    }

    fun getComicsFromLocalDatabase(characterId: Int) {
        comicsLiveData = charactersRepository.comicList
    }

    fun getCaractersFromLocalDatabase() {
        characterLiveData = charactersRepository.getCharacters()
    }


}