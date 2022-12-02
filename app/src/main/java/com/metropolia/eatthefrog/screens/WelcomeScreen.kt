package com.metropolia.eatthefrog.screens

import android.app.Activity
import com.metropolia.eatthefrog.R
import android.app.Application
import android.content.Intent
import android.view.ViewTreeObserver
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.metropolia.eatthefrog.activities.MainActivity
import com.metropolia.eatthefrog.viewmodels.WelcomeScreenViewModel
import kotlinx.coroutines.launch

/**
 * Welcome screen that displays when launching the application for the first time. Contains basic
 * description of the application as well as an input field to enter your name as the user. Name
 * is stored to shared preferences. After the name is added and the Save-button is pressed, relaunches
 * MainActivity which then checks if there is a name stored to the shared preferences and therefore
 * doesn't display this screen again.
 */
@Composable
fun WelcomeScreen(application: Application, activity: Activity) {
    var username by remember { mutableStateOf("")}
    val welcomeScreenViewModel = WelcomeScreenViewModel(application)
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    // Adds listener to the software keyboard to scroll the screen to the bottom when keyboard is opened
    val view = LocalView.current
    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
            if (isKeyboardOpen) {
                coroutineScope.launch {
                    scrollState.animateScrollBy(1000F)
                }
            }
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .background(MaterialTheme.colors.surface)
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_frog),
            contentDescription = "Frog logo",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        WelcomeText(
            text = stringResource(id = R.string.app_name),
            size = 25.sp
        )
        WelcomeText(
            text = stringResource(id = R.string.eat_the_frog_description_first),
            size = 20.sp
        )
        WelcomeText(
            text = stringResource(id = R.string.eat_the_frog_description_two),
            size = 20.sp
        )
        WelcomeText(
            text = stringResource(id = R.string.eat_the_frog_description_three),
            size = 20.sp
        )
        WelcomeText(
            text = "${stringResource(id = R.string.welcome)}!\n${stringResource(id = R.string.please_enter_your_name)}:",
            size = 25.sp
        )

        TextField(
            value = username,
            onValueChange = { username = it },
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            singleLine = true,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                welcomeScreenViewModel.saveInitialTypes()
                welcomeScreenViewModel.savePreferences(username)
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(activity, intent, null)
            }
        ) {
            Text(text = stringResource(R.string.save))
        }
    }
}

@Composable
fun WelcomeText(text: String, size: TextUnit) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier
            .padding(20.dp),
        textAlign = TextAlign.Center,
        fontSize = size,
    )
}