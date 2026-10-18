package com.example.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.rickandmorty.ui.EcranDetail
import com.example.rickandmorty.ui.EcranFavoris
import com.example.rickandmorty.ui.EcranListe
import com.example.rickandmorty.ui.PersonnageViewModel
import com.example.rickandmorty.ui.theme.Mort
import com.example.rickandmorty.ui.theme.RickAndMortyTheme

class Liste
data class Detail(val id: Int)
class Favoris

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyTheme {
                val viewModel = viewModel<PersonnageViewModel>()
                val favoris by viewModel.favoris.collectAsStateWithLifecycle()

                val backStack = remember { mutableStateListOf<Any>(Liste()) }

                val titre = when (backStack.lastOrNull()) {
                    is Liste -> "Rick and Morty"
                    is Detail -> "Détail"
                    is Favoris -> "Mes favoris"
                    else -> ""
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(titre) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            navigationIcon = {
                                if (backStack.size > 1) {
                                    IconButton(onClick = { backStack.removeLastOrNull() }) {
                                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                                    }
                                }
                            },
                            actions = {
                                if (backStack.lastOrNull() !is Favoris) {
                                    TextButton(onClick = { backStack.add(Favoris()) }) {
                                        Icon(Icons.Default.Favorite, contentDescription = null, tint = Mort)
                                        Spacer(Modifier.width(6.dp))
                                        Text("Favoris (${favoris.size})")
                                    }
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    NavDisplay(
                        modifier = Modifier.padding(innerPadding),
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull() },
                        entryProvider = entryProvider {
                            entry<Liste> {
                                EcranListe(viewModel, onClicPersonnage = { id ->
                                    viewModel.chargerEpisodes(id)
                                    backStack.add(Detail(id))
                                })
                            }
                            entry<Detail> { cle ->
                                EcranDetail(viewModel, cle.id)
                            }
                            entry<Favoris> {
                                EcranFavoris(viewModel, onClicPersonnage = { id ->
                                    viewModel.chargerEpisodes(id)
                                    backStack.add(Detail(id))
                                })
                            }
                        }
                    )
                }
            }
        }
    }
}
