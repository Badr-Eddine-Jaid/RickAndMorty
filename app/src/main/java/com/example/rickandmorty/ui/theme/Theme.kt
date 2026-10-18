package com.example.rickandmorty.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = VertFonce,
    onPrimary = Color.White,
    primaryContainer = VertPale,
    onPrimaryContainer = VertNuit,
    secondary = BleuMorty,
    background = FondClair,
    surface = FondClair,
    surfaceVariant = CarteClaire,
    surfaceContainerHighest = CarteClaire
)

private val DarkColorScheme = darkColorScheme(
    primary = VertPortal,
    onPrimary = VertNuit,
    primaryContainer = VertSombre,
    onPrimaryContainer = VertPale,
    secondary = BleuClair,
    background = FondSombre,
    surface = FondSombre,
    surfaceVariant = CarteSombre,
    surfaceContainerHighest = CarteSombre
)

@Composable
fun RickAndMortyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
