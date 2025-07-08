package com.uesar.journal.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.uesar.journal.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    navigateToAddEntry: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {
            Text("Your EchoJournal")
        }, actions = {
            IconButton(onClick = { viewModel.onIntent(HomeAction.SettingsClicked) }) {
                Icon(Icons.Outlined.Settings, contentDescription = null)
            }
        })
    }, floatingActionButton = {
        FloatingActionButton(onClick = { viewModel.onIntent(HomeAction.AddEntryClicked) }) {
            Icon(
                Icons.Filled.Add, contentDescription = null
            )
        }
    }) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(state.entryCount == 0) {
                NoEntriesScreen()
            } else {
                Text("Entries: ${state.entryCount}")
            }
        }
    }
}

@Composable
fun NoEntriesScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_entry), contentDescription = null
        )
        Text(modifier = Modifier.padding(top = 34.dp), text = "No Entries")
        Text("Start recording your first Echo")
    }
}