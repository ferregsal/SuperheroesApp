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
    @SerializedName("id") val id : Int,
    @SerializedName("biography") val biography: Biography,
    @SerializedName("powerstats") val stats: Powerstats
){
    }
data class Powerstats(
    @SerializedName("intelligence") val intelligence: Int,
    @SerializedName("strength") val strength: Int,
    @SerializedName("speed") val speed: Int,
    @SerializedName("durability") val durability: Int,
    @SerializedName("power") val power: Int,
    @SerializedName("combat") val combat: Int,

) {

}
data class Biography(
    @SerializedName("full-name") val realName: String,
    @SerializedName("alter-egos") val alterEgos: String,
    @SerializedName("aliases") val aliases: List<String>,
    @SerializedName("place-of-birth") val placeOfBirth: String,
    @SerializedName("first-appearance") val firstAppearance: String,
    @SerializedName("publisher") val publisher: String,
    @SerializedName("alignment") val alignment: String,
) {

}

data class Image(
    @SerializedName("url") val url: String){

}

