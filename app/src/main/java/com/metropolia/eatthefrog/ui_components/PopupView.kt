package com.metropolia.eatthefrog.ui_components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch

/**
 * A reusable ModalBottomSheetLayout. Can be used to display wanted UI components within the window.
 */
@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterialApi
@Composable
fun PopupView(display: MutableLiveData<Boolean>, callback: () -> Unit, content: @Composable () -> Unit) {

    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val modalBottomSheetScope = rememberCoroutineScope()
    val state = display.observeAsState()

    if (state.value == true){
        modalBottomSheetScope.launch {
            modalBottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
        }
    } else {

        modalBottomSheetScope.launch {
            modalBottomSheetState.animateTo(ModalBottomSheetValue.Hidden)
        }
    }

    if (modalBottomSheetState.currentValue != ModalBottomSheetValue.Hidden) {
        DisposableEffect(Unit) {
            onDispose {
                callback()
            }
        }
    }

    ModalBottomSheetLayout(
        modifier = if (state.value == false) Modifier.alpha(0f) else Modifier,
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 0.dp,
        sheetState = modalBottomSheetState,
        sheetContent =  {
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.Bottom) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                        .background(MaterialTheme.colors.surface),
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
fun PopupView(display: MutableLiveData<Boolean>, callback: () -> Unit, headerContent: @Composable () -> Unit, content: @Composable () -> Unit) {

    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val modalBottomSheetScope = rememberCoroutineScope()

    val state = display.observeAsState()

    if (state.value == true){
        modalBottomSheetScope.launch {
            modalBottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
        }
    } else {

        modalBottomSheetScope.launch {
            modalBottomSheetState.animateTo(ModalBottomSheetValue.Hidden)
        }
    }

    if (modalBottomSheetState.currentValue != ModalBottomSheetValue.Hidden) {
        DisposableEffect(Unit) {
            onDispose {
                callback()
            }
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
