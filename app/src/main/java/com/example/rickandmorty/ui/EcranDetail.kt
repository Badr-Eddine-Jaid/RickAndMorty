package com.example.rickandmorty.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage

@Composable
fun EcranDetail(viewModel: PersonnageViewModel, id: Int) {
    val personnages by viewModel.personnages.collectAsStateWithLifecycle()
    val favoris by viewModel.favoris.collectAsStateWithLifecycle()

    val episodes by viewModel.episodes.collectAsStateWithLifecycle()

    val personnage = personnages.find { it.id == id }

    if (personnage == null) {
        Text("Personnage introuvable", modifier = Modifier.padding(16.dp))
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = personnage.image,
                contentDescription = personnage.name,
                modifier = Modifier.size(260.dp).clip(RoundedCornerShape(24.dp))
            )
            Text(personnage.name, style = MaterialTheme.typography.headlineMedium)
            LigneStatut(personnage)

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Info("Espèce", personnage.species)
                    Info("Genre", personnage.gender)
                    Info("Origine", personnage.origin.name)
                    Info("Dernier lieu", personnage.location.name)
                }
            }

            val estFavori = personnage.id in favoris
            if (estFavori) {
                OutlinedButton(onClick = { viewModel.changerFavori(personnage.id) }, modifier = Modifier.fillMaxWidth()) {
                    Text("❤ Retirer des favoris")
                }
            } else {
                Button(onClick = { viewModel.changerFavori(personnage.id) }, modifier = Modifier.fillMaxWidth()) {
                    Text("🤍 Ajouter aux favoris")
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        "Épisodes (${personnage.episode.size})",
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (episodes.isEmpty()) {
                        Text("Chargement des épisodes…")
                    } else {
                        episodes.forEach { episode ->
                            Text("${episode.episode} · ${episode.name}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Info(titre: String, valeur: String) {
    Text(
        "$titre : $valeur",
        style = MaterialTheme.typography.bodyLarge
    )
}
