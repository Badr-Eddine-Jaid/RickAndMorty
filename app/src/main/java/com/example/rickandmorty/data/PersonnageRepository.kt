package com.example.rickandmorty.data

import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.model.Personnage
import com.example.rickandmorty.model.ReponseApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.request
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class PersonnageRepository {

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun getPersonnages(): List<Personnage> {
        val tous = mutableListOf<Personnage>()
        for (page in 1..10) {
            val url = "https://rickandmortyapi.com/api/character?page=$page"
            val reponse: ReponseApi = client.request(url).body()
            tous.addAll(reponse.results)
        }
        return tous
    }

    fun getEpisodes(urls: List<String>): Flow<Episode> = flow {
        for (url in urls) {
            val episode: Episode = client.request(url).body()
            emit(episode)
        }
    }
}
