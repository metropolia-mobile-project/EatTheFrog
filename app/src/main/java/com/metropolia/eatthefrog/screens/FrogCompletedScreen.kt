package com.metropolia.eatthefrog.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.viewmodels.HomeScreenViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FrogCompletedScreen(vm: HomeScreenViewModel) {

    val visible = vm.showFrogCompletedScreen.observeAsState()

    AnimatedVisibility(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        enter = fadeIn(animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)),
        exit = fadeOut(animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)),
        visible = visible.value ?: false) {

        AlertDialog(
            modifier = Modifier.clip(RoundedCornerShape(30.dp)),
            onDismissRequest = {vm.closeFrogCompletedScreen()},

            title = {

                Column(
                    Modifier.fillMaxWidth().wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(painter = painterResource(id = R.drawable.ic_baseline_task_alt_24),
                        modifier = Modifier
                            .size(150.dp)
                            .animateEnterExit(
                                enter = fadeIn(
                                    animationSpec = tween(
                                        durationMillis = 1000,
                                        easing = LinearOutSlowInEasing
                                    )
                                ),
                                exit = fadeOut(
                                    animationSpec = tween(
                                        durationMillis = 200,
                                        easing = FastOutLinearInEasing
                                    )
                                )
                            ),

                        contentDescription = "completed sign")
                }
           },

            text = {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .animateEnterExit(
                            enter = fadeIn(
                                animationSpec = tween(
                                    durationMillis = 1800,
                                    easing = LinearOutSlowInEasing
                                )
                            ),
                            exit = fadeOut(
                                animationSpec = tween(
                                    durationMillis = 200,
                                    easing = FastOutLinearInEasing
                                )
                            )
                        ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = stringResource(R.string.good_job),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary,
                        fontSize = 20.sp
                    )
                    Spacer(Modifier.height(10.dp))
                    Text("\"${vm.quote.q}\"", textAlign = TextAlign.Center)
                    Spacer(Modifier.height(10.dp))
                    Text("-${vm.quote.a}", textAlign = TextAlign.Center)
                }
            },

            confirmButton = {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                        .animateEnterExit(
                            enter = fadeIn(
                                animationSpec = tween(
                                    durationMillis = 3800,
                                    easing = LinearOutSlowInEasing
                                )
                            ),
                            exit = fadeOut(
                                animationSpec = tween(
                                    durationMillis = 200,
                                    easing = FastOutLinearInEasing
                                )
                            )
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .height(40.dp),
                        onClick = {vm.closeFrogCompletedScreen()}){
                        Image(
                            painterResource(id = R.drawable.ic_baseline_chevron_right_24),
                            contentDescription = ""
                        )
                    }
                }

            })


    }
}

