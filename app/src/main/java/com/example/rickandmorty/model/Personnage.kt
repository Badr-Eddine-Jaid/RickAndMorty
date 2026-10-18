package com.example.rickandmorty.model

import kotlinx.serialization.Serializable

@Serializable
data class ReponseApi(val results: List<Personnage>)

@Serializable
data class Personnage(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String,
    val origin: Origine,
    val location: Origine,
    val episode: List<String>
)

@Serializable
data class Origine(val name: String)
