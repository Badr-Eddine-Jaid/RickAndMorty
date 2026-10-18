package com.example.rickandmorty.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.rickandmorty.model.Personnage
import com.example.rickandmorty.ui.theme.Inconnu
import com.example.rickandmorty.ui.theme.Mort
import com.example.rickandmorty.ui.theme.Vivant

@Composable
fun EcranListe(viewModel: PersonnageViewModel, onClicPersonnage: (Int) -> Unit) {
    val personnages by viewModel.personnagesFiltres.collectAsStateWithLifecycle()
    val recherche by viewModel.recherche.collectAsStateWithLifecycle()
    val chargement by viewModel.chargement.collectAsStateWithLifecycle()
    val erreur by viewModel.erreur.collectAsStateWithLifecycle()

    if (chargement) {
        Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
            CircularProgressIndicator()
        }
    } else if (erreur) {
        Column(Modifier.fillMaxSize().padding(24.dp), Arrangement.Center, Alignment.CenterHorizontally) {
            Text("Erreur de chargement. Vérifie ta connexion Internet.")
            Spacer(Modifier.size(12.dp))
            Button(onClick = { viewModel.charger() }) { Text("Réessayer") }
        }
    } else {
        Column {
            OutlinedTextField(
                value = recherche,
                onValueChange = { viewModel.changerRecherche(it) },
                label = { Text("Rechercher un personnage") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )

            if (personnages.isEmpty()) {
                Text("Aucun personnage trouvé.", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn(contentPadding = PaddingValues(bottom = 8.dp)) {
                    items(personnages) { personnage ->
                        CartePersonnage(personnage, onClick = { onClicPersonnage(personnage.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun CartePersonnage(personnage: Personnage, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = personnage.image,
                contentDescription = personnage.name,
                modifier = Modifier.size(64.dp).clip(CircleShape)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(personnage.name, style = MaterialTheme.typography.titleMedium)
                LigneStatut(personnage)
            }
        }
    }
}

@Composable
fun LigneStatut(personnage: Personnage) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(couleurStatut(personnage.status))
        )
        Spacer(Modifier.width(6.dp))
        Text("${personnage.species} - ${personnage.status}")
    }
}

fun couleurStatut(statut: String): Color {
    return when (statut) {
        "Alive" -> Vivant
        "Dead" -> Mort
        else -> Inconnu
    }
}
