package com.example.rickandmorty.model

import kotlinx.serialization.Serializable

@Serializable
data class Episode(val id: Int, val name: String, val episode: String)
