package com.example.superheroesapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.superheroesapp.R
import com.example.superheroesapp.adapters.SuperheroAdapter
import com.example.superheroesapp.data.Superhero
import com.example.superheroesapp.data.SuperheroResponse
import com.example.superheroesapp.databinding.ActivityMainBinding
import com.example.superheroesapp.utils.RetrofitProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class MainActivity : AppCompatActivity() {
private lateinit var binding: ActivityMainBinding
private lateinit var adapter: SuperheroAdapter
private lateinit var superheroList:List<Superhero>
  //  var showFavorites = false
  //  lateinit var favoritesMenu: MenuItem


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)

        // favoritesMenu = menu.findItem(R.id.menu_favorites)

        val searchViewItem = menu.findItem(R.id.superheroSearchView)
        val searchView = searchViewItem.actionView as SearchView


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {


            override fun onQueryTextSubmit(query: String): Boolean {
                searchByName(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {

                return false
            }

        })

        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        superheroList = emptyList()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter= SuperheroAdapter(superheroList){ position ->
            navigateToDetail(superheroList[position])
        }

        binding.superHeroRecyclerView.adapter = adapter
        binding.superHeroRecyclerView.layoutManager=GridLayoutManager(this, 2)
        searchByName("a")



    }
    private fun navigateToDetail(superhero: Superhero) {
        Toast.makeText(this, superhero.name, Toast.LENGTH_SHORT).show()
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_SUPERHERO_ID, superhero.id)
        startActivity(intent)
    }
    private fun searchByName (query: String){
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val SuperheroApiService = RetrofitProvider.getSuperheroApiService()
                val result = SuperheroApiService.findSuperheroesByName(query)
                runOnUiThread {
                    if (result.response == "success") {
                        superheroList = result.results
                        adapter.updateData(superheroList)
                    } else {
                        adapter.updateData(emptyList())

                    }
                }
            }
            catch (e:Exception){

                e.printStackTrace()
            }
        }
    }



}

/* override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
        R.id.menu_favorites -> {
            showFavorites = !showFavorites
            if (showFavorites) {
                favoritesMenu.setIcon(R.drawable.ic_favorite)
            } else {
                favoritesMenu.setIcon(R.drawable.ic_favorite_empty)
            }
            applyFilters()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}*/

/*private fun applyFilters() {

   /* if (showFavorites) {
        val favoriteIds = session.getAllFavoriteHoroscopes()
        horoscopeList = horoscopeList.filter { favoriteIds.contains(it.id) }
    }*/
    if (searchText != null) {
         = horoscopeList.filter {
            getString(it.name).contains(searchText!!, true) ||
                    getString(it.description).contains(searchText!!, true)
        }
    }
    adapter.updateData(horoscopeList)
}*/


