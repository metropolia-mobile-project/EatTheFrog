package com.metropolia.eatthefrog.screens.history.components

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.viewmodels.HistoryScreenViewModel

@Composable
fun HistorySearchContainer(vm: HistoryScreenViewModel) {

    val focusManager = LocalFocusManager.current

    TextField(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color.Transparent),
        value = vm.searchInput.value,
        label = { Text(stringResource(R.string.search)) },
        onValueChange = { vm.updateSearchInput(it) },
        singleLine = true,
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        leadingIcon = {
            Image(
                painter = painterResource(R.drawable.ic_baseline_close_24),
                modifier = Modifier
                    .clickable {
                        focusManager.clearFocus()
                        vm.clearInput()
                   },
                contentDescription = "search button")
        },
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

