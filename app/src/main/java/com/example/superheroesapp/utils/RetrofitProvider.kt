package com.example.superheroesapp.utils

import com.example.superheroesapp.activities.SuperheroApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitProvider {
    companion object{
           private fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://superheroapi.com/api/22a7e363d48b752f5cfc40207bcabf6e/")
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        fun getSuperheroApiService():SuperheroApiService{
            return getRetrofit().create(SuperheroApiService::class.java)
        }
    }
}