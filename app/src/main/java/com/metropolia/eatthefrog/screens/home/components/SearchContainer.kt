package com.metropolia.eatthefrog.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.viewmodels.HomeScreenViewModel

@Composable
fun SearchContainer(vm: HomeScreenViewModel) {

    val focusManager = LocalFocusManager.current

    TextField(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(30.dp))
            .background(Color.Transparent),
        value = vm.searchInput.value,
        onValueChange = { vm.updateSearchInput(it) },
        singleLine = true,
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        leadingIcon = {

            Image(
                painter = painterResource(R.drawable.ic_baseline_close_24),
                modifier = Modifier
                    .clickable { vm.closeSearch() },
                contentDescription = "search button") },

        trailingIcon = {
            Image(
            painter = painterResource(R.drawable.ic_baseline_search_36),
            modifier = Modifier
                .clickable { focusManager.clearFocus() },
            contentDescription = "search button") },
    )
}

