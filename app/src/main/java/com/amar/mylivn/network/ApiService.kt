package com.amar.mylivn.network

import com.amar.mylivn.model.CharactersResponse
import com.amar.mylivn.model.ComicsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("characters")
    fun getCharacters(@Query("limit") limit: Int, @Query("offset") offset: Int): Call<CharactersResponse>

    @GET("characters/{character_id}}/comics")
    fun getComics(@Path("character_id") charcterId: Int, @Query("limit") limit: Int, @Query("offset") offset: Int): Call<ComicsResponse>

}