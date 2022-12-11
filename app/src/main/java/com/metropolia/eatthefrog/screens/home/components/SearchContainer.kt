package com.metropolia.eatthefrog.screens.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
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

/**
 * Container for the search portion of the home screen. Allows user to search for tasks by name.
 */

@Composable
fun SearchContainer(vm: HomeScreenViewModel) {

    val focusManager = LocalFocusManager.current

    TextField(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(30.dp))
            .background(Color.Transparent),
        value = vm.searchInput.value,
        label = {Text("Search")},
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
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = MaterialTheme.colors.surface,
        )
    )
}

