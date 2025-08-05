package com.uesar.journal

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.uesar.journal.ui.Route
import com.uesar.journal.ui.home.HomeScreenRoot
import com.uesar.journal.ui.home.HomeViewModel
import com.uesar.journal.ui.newentry.NewEntryScreenRoot
import com.uesar.journal.ui.newentry.NewEntryViewModel
import com.uesar.journal.ui.theme.JournalTheme
import org.koin.androidx.compose.koinViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            JournalTheme {
                AppNavigation()
            }
        }
        checkAndRequestPermissions()
    }

    private fun checkAndRequestPermissions() {
        val permissions = arrayOf(Manifest.permission.RECORD_AUDIO)
        val missing = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (missing.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, missing.toTypedArray(), 101)
        }
    }
}

@Composable
private fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(modifier = Modifier, navController = navController, startDestination = Route.Home) {
        composable<Route.Home> {
            val homeViewModel = koinViewModel<HomeViewModel>()
            HomeScreenRoot(
                viewModel = homeViewModel,
                navigateToNewEntry = { audioPath -> navController.navigate(Route.NewEntry(audioPath)) },
                navigateToSettings = {})
        }
        composable<Route.NewEntry> { backStackEntry ->
            val newEntryRoute = backStackEntry.toRoute<Route.NewEntry>()
            val audioPath = newEntryRoute.audioPath
            val newEntryViewModel = koinViewModel<NewEntryViewModel>()
            NewEntryScreenRoot(
                viewModel = newEntryViewModel,
                audioPath = audioPath,
                navigateBack = { navController.popBackStack() })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppScaffoldPreview() {
    JournalTheme {
        AppNavigation()
    }
}