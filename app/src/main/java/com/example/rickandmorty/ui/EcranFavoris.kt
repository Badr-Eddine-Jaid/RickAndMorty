package com.example.rickandmorty.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun EcranFavoris(viewModel: PersonnageViewModel, onClicPersonnage: (Int) -> Unit) {
    val personnages by viewModel.personnages.collectAsStateWithLifecycle()
    val favoris by viewModel.favoris.collectAsStateWithLifecycle()

    val mesFavoris = personnages.filter { it.id in favoris }

    if (mesFavoris.isEmpty()) {
        Column(
            Modifier.fillMaxSize().padding(24.dp),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            Text("Aucun favori pour l'instant.", style = MaterialTheme.typography.titleMedium)
            Text("Ouvre un personnage et ajoute-le à tes favoris !")
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
            items(mesFavoris) { personnage ->
                CartePersonnage(personnage, onClick = { onClicPersonnage(personnage.id) })
            }
        }
    }
}
