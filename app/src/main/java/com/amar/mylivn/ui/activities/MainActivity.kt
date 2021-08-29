package com.amar.mylivn.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.amar.mylivn.R
import com.amar.mylivn.listeners.OnItemClickListener
import com.amar.mylivn.model.Character
import com.amar.mylivn.model.Comic
import com.amar.mylivn.ui.adapters.CharacterListAdapter
import com.amar.mylivn.ui.adapters.ComicListAdapter
import com.amar.mylivn.ui.viewmodels.CharactersViewModel
import com.amar.mylivn.util.Helper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val characterList : ArrayList<Character> = arrayListOf()
    private val comicList : ArrayList<Comic> = arrayListOf()
    val charactersViewModel: CharactersViewModel by viewModels()
    lateinit var adapter: CharacterListAdapter
    lateinit var comicAdapter: ComicListAdapter
    private var pageCharacters = 1
    private var pageComics = 1
    private val limit = 10
    lateinit var listener: OnItemClickListener
    lateinit var selectedCharacter: Character
    var initialLoadDone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        listener = object : OnItemClickListener {
            override fun onCharacterSelected(item: Character) {

                if (initialLoadDone && selectedCharacter.characterId.equals(item.characterId)) {
                    return
                }
                selectedCharacter = item
                pageComics = 1

                showProgressBar(true)
                charactersViewModel.getComicsData(item.characterId)
                if (Helper.isInternetAvailable(applicationContext)) {
                    charactersViewModel.getComicsFromAPIAndStore(item.characterId, limit, 0)
                }
                else {
                    charactersViewModel.getComicsFromLocalDatabase(item.characterId)
                }
            }
        }

        setupRecyclerViews()
        getData()

        horizontalRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollHorizontally(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (Helper.isInternetAvailable(applicationContext)) {
                        pageCharacters ++
                        loadMoreCharachters()
                    }
                }
            }
        })

        verticalRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (Helper.isInternetAvailable(applicationContext)) {
                        pageComics ++
                        loadMoreComics()
                    }
                }
            }
        })

    }

    private fun getData() {

        showProgressBar(true)

        if (Helper.isInternetAvailable(applicationContext)) {
            charactersViewModel.getCharactersFromAPIAndStore(limit, limit*pageCharacters)
        }

        else {
            charactersViewModel.getCaractersFromLocalDatabase()
        }

        charactersViewModel.getCharactersData()

        charactersViewModel.characterLiveData.observe(this, Observer {
            characterList.clear()
            characterList.addAll(it)
            adapter.setItems(characterList)


            if (characterList.size > 0) {
                if (!initialLoadDone) {
                    initialLoadDone = true
                    charactersViewModel.getComicsData(characterList.get(0).characterId)
                    selectedCharacter = characterList.get(0)
                    listener.onCharacterSelected(selectedCharacter)
                }
            }
            showProgressBar(false)
            adapter.notifyDataSetChanged()
        })

        charactersViewModel.comicsLiveData.observe(this, Observer {
            comicList.clear()
            comicList.addAll(it)

            comicAdapter.setItems(comicList)
            comicAdapter.notifyDataSetChanged()
            showProgressBar(false)
            if (comicList.size == 0) {
                infoTV.visibility = View.VISIBLE
                verticalRecyclerView.visibility = View.GONE
            }
            else {
                infoTV.visibility = View.GONE
                verticalRecyclerView.visibility = View.VISIBLE
            }
        })
    }

    private fun loadMoreCharachters() {
        showProgressBar(true)
        charactersViewModel.getCharactersFromAPIAndStore(limit, limit*pageCharacters)
    }

    private fun loadMoreComics() {
        showProgressBar(true)
        charactersViewModel.getComicsFromAPIAndStore(selectedCharacter.characterId, limit, limit*pageComics)
    }


    private fun setupRecyclerViews() {
        adapter =  CharacterListAdapter(characterList, listener)
        comicAdapter =  ComicListAdapter(comicList)
        horizontalRecyclerView.adapter = adapter
        verticalRecyclerView.adapter = comicAdapter
    }

    private fun showProgressBar(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
        }
        else {
            progressBar.visibility = View.GONE
        }
    }

}