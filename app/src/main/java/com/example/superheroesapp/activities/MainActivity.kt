package com.example.superheroesapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.superheroesapp.R
import com.example.superheroesapp.adapters.SuperheroAdapter
import com.example.superheroesapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
lateinit var binding: ActivityMainBinding
lateinit var adapter: SuperheroAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter= SuperheroAdapter()

        binding.superHeroRecyclerView.adapter = adapter
        binding.superHeroRecyclerView.layoutManager=GridLayoutManager(this, 2)
        searchByName("super")
    }

    private fun searchByName (query: String){
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val SuperheroApiService = getRetrofit().create(SuperheroApiService::class.java)
                val result = SuperheroApiService.findSuperheroesByName(query)
                runOnUiThread {
                    adapter.updateData(result.results)
                }
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
    private fun getRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://superheroapi.com/api/7252591128153666/")
        //.client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    }
}


