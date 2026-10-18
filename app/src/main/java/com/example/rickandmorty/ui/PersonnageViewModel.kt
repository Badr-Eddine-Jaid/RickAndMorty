package com.example.rickandmorty.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.PersonnageRepository
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.model.Personnage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch

class PersonnageViewModel : ViewModel() {

    val repository = PersonnageRepository()

    val personnages = MutableStateFlow(listOf<Personnage>())
    val chargement = MutableStateFlow(true)
    val erreur = MutableStateFlow(false)
    val favoris = MutableStateFlow(listOf<Int>())
    val recherche = MutableStateFlow("")
    val episodes = MutableStateFlow(listOf<Episode>())

    val personnagesFiltres: StateFlow<List<Personnage>> =
        combine(personnages, recherche) { liste, texte ->
            liste.filter { it.name.contains(texte, ignoreCase = true) }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = listOf()
        )

    init {
        charger()
    }

    fun charger() {
        viewModelScope.launch {
            chargement.value = true
            erreur.value = false
            try {
                personnages.value = repository.getPersonnages()
            } catch (e: Exception) {
                erreur.value = true
            }
            chargement.value = false
        }
    }

    var dernierClic = 0

    fun chargerEpisodes(id: Int) {
        dernierClic = dernierClic + 1
        val ceClic = dernierClic
        episodes.value = listOf()
        val personnage = personnages.value.find { it.id == id }
        if (personnage != null) {
            viewModelScope.launch {
                try {
                    repository.getEpisodes(personnage.episode)
                        .takeWhile { ceClic == dernierClic }
                        .collect { episodes.value = episodes.value + it }
                } catch (e: Exception) {
                }
            }
        }
    }

    fun changerRecherche(texte: String) {
        recherche.value = texte
    }

    fun changerFavori(id: Int) {
        if (id in favoris.value) {
            favoris.value = favoris.value - id
        } else {
            favoris.value = favoris.value + id
        }
    }
}
