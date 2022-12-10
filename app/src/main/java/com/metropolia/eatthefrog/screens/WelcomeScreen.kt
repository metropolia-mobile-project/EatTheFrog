package com.metropolia.eatthefrog.screens

import android.app.Activity
import com.metropolia.eatthefrog.R
import android.app.Application
import android.content.Intent
import android.util.Log
import android.view.ViewTreeObserver
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import com.google.accompanist.pager.*
import com.metropolia.eatthefrog.activities.MainActivity
import com.metropolia.eatthefrog.navigation.ComposableFun
import com.metropolia.eatthefrog.navigation.HistoryTabItem
import com.metropolia.eatthefrog.viewmodels.WelcomeScreenViewModel
import kotlinx.coroutines.launch

/**
 * Welcome screen that displays when launching the application for the first time. Contains basic
 * description of the application as well as an input field to enter your name as the user. Name
 * is stored to shared preferences. After the name is added and the Save-button is pressed, relaunches
 * MainActivity which then checks if there is a name stored to the shared preferences and therefore
 * doesn't display this screen again.
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeScreen(application: Application, activity: Activity) {
    var username by remember { mutableStateOf("")}
    val welcomeScreenViewModel = WelcomeScreenViewModel(application)
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var keyboardOpen by remember { mutableStateOf(false)}
    val maxChar = 20

    val firstTab = WelcomeTabItem {
        WelcomeTextContainer {
            WelcomeText(
                text = stringResource(id = R.string.eat_the_frog_description_first),
                size = 20.sp
            )
        }
    }

    val secondTab = WelcomeTabItem {
        WelcomeTextContainer {
            WelcomeText(
                text = stringResource(id = R.string.eat_the_frog_description_two),
                size = 20.sp
            )
            WelcomeText(
                text = stringResource(id = R.string.eat_the_frog_description_three),
                size = 20.sp
            )
        }
    }

    val thirdTab = WelcomeTabItem {
        WelcomeTextContainer {
            WelcomeText(
                text = "${stringResource(id = R.string.welcome)}!\n\n${stringResource(id = R.string.please_enter_your_name)}:",
                size = 25.sp
            )

            TextField(
                value = username,
                onValueChange = {
                    if (it.length < maxChar) username = it
                },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                singleLine = true,
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .fillMaxWidth(0.8f)
                    .background(Color.Transparent),
                label = {Text(stringResource(R.string.name))},
                trailingIcon = {
                    Image(
                        painter = painterResource(R.drawable.ic_profile),
                        contentDescription = "search button") },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.DarkGray,
                )
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(horizontal = 20.dp),
                text = "Maximum length 20 characters", fontSize = 8.sp, color = Color.DarkGray, textAlign = TextAlign.Start)
            Spacer(Modifier.height(10.dp))
            TextButton(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(100.dp))
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primaryVariant,
                    disabledBackgroundColor = Color.Gray),

                enabled = username.isNotEmpty(),
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

    val tabs = listOf(
        firstTab,
        secondTab,
        thirdTab
    )
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    if (pagerState.currentPage == 2) welcomeScreenViewModel.toggleEndReached()

    // Adds listener to the software keyboard to scroll the screen to the bottom when keyboard is opened
    val view = LocalView.current
    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
            keyboardOpen = isKeyboardOpen
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


    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.secondary)
    ) {
        WelcomeTabContent(tabs, pagerState)
        Box(
            Modifier
                .padding(bottom = 50.dp)
                .align(Alignment.BottomCenter)) {
            WelcomeTabs(tabs, pagerState, welcomeScreenViewModel)
        }

        if (!keyboardOpen) {
            Box(
                Modifier
                    .padding(top = 50.dp)
                    .align(Alignment.TopCenter)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_frog),
                        contentDescription = "Frog logo",
                    )
                    WelcomeText(
                        text = stringResource(id = R.string.app_name),
                        size = 25.sp,
                        modifier = Modifier.padding(0.dp)
                    )
                }
            }
        }
    }
}

/**
 * Creates a paragraph with the given String parameter.
 */
@Composable
fun WelcomeText(text: String, size: TextUnit, modifier : Modifier = Modifier.padding(20.dp)) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = modifier,
        textAlign = TextAlign.Center,
        fontSize = size,
    )
}

/**
 * Combines all given WelcomeTabItems into the UI.
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeTabs(tabs: List<WelcomeTabItem>, pagerState: PagerState, vm: WelcomeScreenViewModel) {
    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

        if (!vm.endReached.value) {
            FloatingActionButton(
                backgroundColor = MaterialTheme.colors.primaryVariant,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_forward),
                    contentDescription = "Arrow forward",
                    colorFilter = ColorFilter.tint(Color.White)
                )
            }
        }

        Row(Modifier.fillMaxWidth().padding(top = 50.dp), horizontalArrangement = Arrangement.Center) {
            TabRow(
                divider = {},
                modifier = Modifier
                    .height(10.dp)
                    .fillMaxWidth(0.5f)
                    .clip(
                        RoundedCornerShape(10.dp)
                    ),
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.primaryVariant,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier
                            .pagerTabIndicatorOffset(pagerState, tabPositions)
                            .fillMaxHeight()
                            .width(10.dp)
                            .clip(
                                RoundedCornerShape(10.dp)
                            )
                    )
                }) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                                if (index == tabs.size - 1) {
                                    vm.toggleEndReached()
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}

/**
 * Creates a navigation tab. A Navigation tab in the WelcomeScreen is used to toggle the help texts.
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun WelcomeTabContent(tabs: List<WelcomeTabItem>, pagerState: PagerState) {
    HorizontalPager(
        state = pagerState,
        itemSpacing = 25.dp,
        count = tabs.size,
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 20.dp)) { page ->
        tabs[page].screen()
    }
}

/**
 * Container for WelcomeText composable.
 */
@Composable
fun WelcomeTextContainer(content: @Composable () -> Unit) {
    Column(
        Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        content()
    }
}

/**
 * Class used for creating the HorizontalPager tabs.
 */
class WelcomeTabItem(var screen: ComposableFun)