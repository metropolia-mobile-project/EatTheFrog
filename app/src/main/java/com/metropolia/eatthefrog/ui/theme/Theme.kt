package com.metropolia.eatthefrog.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.patrykandpatryk.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatryk.vico.compose.style.ProvideChartStyle

private val DarkColorPalette = darkColors(
    primary = YaleBlueLight,
    primaryVariant = YaleBlue,
    secondary = GreyBlue,
    background = LightGray
)

private val LightColorPalette = lightColors(
    primary = YaleBlueLight,
    primaryVariant = YaleBlue,
    secondary = GreyBlue,
    background = LightGray
    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun EatTheFrogTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()
    val colors = if (darkTheme) {
        systemUiController.setSystemBarsColor(
            color = DarkColorPalette.primaryVariant
        )
        DarkColorPalette
    } else {
        systemUiController.setSystemBarsColor(
            color = LightColorPalette.primaryVariant
        )
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
