package com.metropolia.eatthefrog.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Dark mode colors.
 */
private val DarkColorPalette = darkColors(
    primary = YaleBlueLight,
    primaryVariant = YaleBlue,
    secondary = GreyBlue,
    background = DarkGray,
    onBackground = Color.White
)

/**
 * Light mode colors.
 */
private val LightColorPalette = lightColors(
    primary = YaleBlueLight,
    primaryVariant = YaleBlue,
    secondary = GreyBlue,
    background = LightGray,
    onBackground = Color.Black
)

/**
 * Customized theme colors used in the application.
 * @param darkTheme: status of the device's darkmode setting.
 * @param settingsDarkMode: status of the darkmode in the application's settings.
 * @param content: Composables to be displayed under the theme.
 */
@Composable
fun EatTheFrogTheme(darkTheme: Boolean = isSystemInDarkTheme(), settingsDarkMode: Boolean, content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()
    val colors = if (darkTheme || settingsDarkMode) {
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
