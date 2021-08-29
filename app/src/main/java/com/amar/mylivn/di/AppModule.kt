package com.amar.mylivn.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amar.mylivn.db.CharacterDAO
import com.amar.mylivn.db.ComicDAO
import com.amar.mylivn.db.MarvelDatabase
import com.amar.mylivn.network.ApiService
import com.amar.mylivn.repository.CharactersRepository
import com.amar.mylivn.util.Constants
import com.amar.mylivn.util.Helper
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, MarvelDatabase::class.java, "marvel.db")
            .allowMainThreadQueries()
            .build()

    @Singleton
    @Provides
    fun provideCharacterDao(database: MarvelDatabase) =
        database.characterDao()

    @Singleton
    @Provides
    fun provideComicDao(database: MarvelDatabase) =
        database.comicDao()

    @Singleton
    @Provides
    fun provideApiService() : ApiService {

        val httpClient = OkHttpClient.Builder().addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {

                val calendar: Calendar = Calendar.getInstance()

                val stringToHash: String = calendar.getTimeInMillis().toString() + Constants.PRIVATE_KEY + Constants.PUBLIC_KEY

                val original: Request = chain.request()
                val originalHttpUrl: HttpUrl = original.url
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("apikey", Constants.PUBLIC_KEY)
                    .addQueryParameter("hash", Helper.md5Java(stringToHash))
                    .addQueryParameter("ts", calendar.getTimeInMillis().toString())
                    .build()

                val requestBuilder: Request.Builder = original.newBuilder()
                    .url(url)
                val request: Request = requestBuilder.build()
                return chain.proceed(request)

            }

        })
        httpClient.readTimeout(240, TimeUnit.SECONDS)
        httpClient.connectTimeout(240, TimeUnit.SECONDS)
        httpClient.writeTimeout(240, TimeUnit.SECONDS)


        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(logging)

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideRepository(apiService: ApiService, characterDao: CharacterDAO, comicDao: ComicDAO
    ) = CharactersRepository(apiService, characterDao, comicDao)

}