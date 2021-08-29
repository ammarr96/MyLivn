package com.amar.mylivn.repository

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amar.mylivn.db.CharacterDAO
import com.amar.mylivn.db.ComicDAO
import com.amar.mylivn.model.Character
import com.amar.mylivn.model.CharactersResponse
import com.amar.mylivn.model.Comic
import com.amar.mylivn.model.ComicsResponse
import com.amar.mylivn.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.collections.ArrayList

class CharactersRepository @Inject constructor(
    private val apiService: ApiService,
    private val characterDao: CharacterDAO,
    private val comicDao: ComicDAO
) {

    //var characterList : MutableLiveData<ArrayList<Character>>
    var comicList : MutableLiveData<List<Comic>>
    //private val list: ArrayList<Character> = ArrayList()
    private var list2: ArrayList<Comic> = ArrayList()

    init {
        //characterList = MutableLiveData()
        comicList = MutableLiveData()
    }

    fun getCharacters() : LiveData<List<Character>> {

        return characterDao.getAllCharacters()
    }

    fun getComics(characterId: Int) {

        comicList.value = comicDao.getComicsByCharacterId(characterId)

        //return comicList;
    }

    fun getCharactersFromApi(limit: Int, offset: Int)  {

        val call = apiService.getCharacters(limit, offset)

        call.enqueue(object : Callback<CharactersResponse?> {
            override fun onResponse(call: Call<CharactersResponse?>, response: Response<CharactersResponse?>) {

                when(response.code())
                {
                    200 ->{


                        Thread(Runnable {

                            //list.addAll(response.body()?.data?.results!!)
                            //characterList.value = list

                            for (c in response.body()?.data?.results!!) {
                                characterDao.deleteCharacterById(c.characterId)
                            }
                            characterDao.insertCharacters(response.body()?.data?.results!!)


                        }).start()
                    }
                }

            }

            override fun onFailure(call: Call<CharactersResponse?>, t: Throwable) {

            }

        })

    }

    fun getComicsFromApi(characteId: Int, limit: Int, offset: Int)  {

        val call = apiService.getComics(characteId, limit, offset)

        call.enqueue(object : Callback<ComicsResponse?> {
            override fun onResponse(call: Call<ComicsResponse?>, response: Response<ComicsResponse?>) {

                when(response.code())
                {
                    200 ->{

                       list2.addAll(response.body()?.data?.results!!)
                       comicList.value = list2

                        Thread(Runnable {


                            for (comic in response.body()?.data?.results!!) {
                                comic.characterId = characteId
                                comicDao.deleteComiscById(comic.comicId)
                            }
                            comicDao.insertComics(response.body()?.data?.results!!)
                            System.out.println("AMAR UPDATE COMICS FROM API STORED IN DB")


                            Handler(Looper.getMainLooper()).post {
                                //code that runs in main
                                getComics(characteId)
                                //comicList.value = list2
                            }

                        }).start()

                    }
                }

            }

            override fun onFailure(call: Call<ComicsResponse?>, t: Throwable) {

            }

        })

    }

}