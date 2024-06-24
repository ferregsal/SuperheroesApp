package com.example.superheroesapp.data
import com.google.gson.annotations.SerializedName
data class SuperheroResponse (
    @SerializedName("response") val response: String,
    @SerializedName("results-for") val resultsFor: String,
    @SerializedName("results") val results: List<Superhero>){


}
data class Superhero(
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: Image,
    @SerializedName("id") val id : String,
    @SerializedName("biography") val biography: Biography

){
    }

data class Biography(
    @SerializedName("full-name") val realName: String,
) {

}

data class Image(
    @SerializedName("url") val url: String){

}

