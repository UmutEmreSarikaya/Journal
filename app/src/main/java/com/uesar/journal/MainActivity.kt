package com.uesar.journal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.uesar.journal.ui.theme.JournalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            JournalTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
                    FloatingActionButton(onClick = {}) {
                        Icon(
                            Icons.Filled.Add, contentDescription = null
                        )
                    }
                }) { innerPadding ->
                    NoEntriesScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun NoEntriesScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.no_entry),
            contentDescription = null
        )
        Text(modifier = Modifier.padding(top = 34.dp), text = "No Entries")
        Text("Start recording your first Echo")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JournalTheme {
        NoEntriesScreen()
    }
}