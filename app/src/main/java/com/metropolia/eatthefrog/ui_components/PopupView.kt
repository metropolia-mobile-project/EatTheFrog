package com.metropolia.eatthefrog

import android.annotation.SuppressLint
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
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import com.metropolia.eatthefrog.activities.MainActivity
import kotlinx.coroutines.launch

/**
 * A reusable ModalBottomSheetLayout. Can be used to display wanted UI components within the window.
 */
@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterialApi
@Composable
fun PopupView(display: Boolean, callback: () -> Unit, content: @Composable () -> Unit) {

    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val modalBottomSheetScope = rememberCoroutineScope()

    if (display) {
        modalBottomSheetScope.launch {
            modalBottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
            callback()
        }
    }

    ModalBottomSheetLayout(
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 0.dp,
        sheetState = modalBottomSheetState,
        sheetContent =  {
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.Bottom) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                        .background(MaterialTheme.colors.secondary),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    content()
                }
            }
        },
    ){}
}


/**
 * A reusable ModalBottomSheetLayout with a offset header component. Can be used to display wanted UI components within the window.
 */
@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterialApi
@Composable
fun PopupView(display: Boolean, callback: () -> Unit, headerContent: @Composable () -> Unit, content: @Composable () -> Unit) {

    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val modalBottomSheetScope = rememberCoroutineScope()

    if (display) {
        modalBottomSheetScope.launch {
            modalBottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
            callback()
        }
    }

    ModalBottomSheetLayout(
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 0.dp,
        sheetState = modalBottomSheetState,
        sheetContent =  {
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .background(Color.Transparent)) {

                Column(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.90f)
                        .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                        .background(MaterialTheme.colors.secondary)
                        .align(Alignment.BottomCenter),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    content()
                }

                Column(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.20f)
                        .background(Color.Transparent)
                        .align(Alignment.TopCenter),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    headerContent()
                }
            }
        },
    ){}
}
