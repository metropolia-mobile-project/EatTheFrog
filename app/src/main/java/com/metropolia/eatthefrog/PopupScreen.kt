package com.metropolia.eatthefrog

import android.renderscript.RenderScript
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/**
 * A Popup window which has a slide in animation. Can be used to display wanted UI components within the window.
 * @param slideAnimationSpeed: determines how fast the PopupView opens up when opened.
 * @param openedPopup: The opened state of the PopupView.
 * @param content: The content displayed within the PopupView.
 */
@Composable
fun PopupView(slideAnimationSpeed : Int = 200, openedPopup: MutableState<Boolean>, content: @Composable () -> Unit) {

    var popupStatus by remember { openedPopup }

    fun closePopup() {
        popupStatus = false
    }

    Column(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        AnimatedVisibility(visible = popupStatus,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(
                    durationMillis = slideAnimationSpeed,
                    easing = LinearEasing
                )
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(
                    durationMillis = slideAnimationSpeed - 100,
                    easing = LinearEasing
                )
            )
        ){
            Column(
                Modifier
                    .fillMaxSize()
                    .alpha(0.6f)
                    .background(Color.Black),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Column(
                    Modifier
                        .fillMaxHeight(0.2f)
                        .fillMaxWidth()
                        .clickable(interactionSource = remember(::MutableInteractionSource), indication = null) { closePopup() },
                ) {}
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                        .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                        .background(MaterialTheme.colors.secondary),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    content()
                }
            }
        }
    }
}
