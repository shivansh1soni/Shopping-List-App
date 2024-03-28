package com.example.test

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.test.ui.theme.TestTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}


@Composable
fun Navigation() {
    val navHostController = rememberNavController()
    val viewModel: LocationViewModel = viewModel()
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)

    NavHost(navHostController, startDestination = "shoppingListScreen") {
        composable("shoppingListScreen") {
            ShoppingListApp(
                locationUtils = locationUtils,
                viewModel = viewModel ,
                navController = navHostController ,
                context = context,
                address = viewModel.address.value.firstOrNull()?.formattedAddress ?: "No Address"
            )
        }

        dialog("locationScreen") { _ ->
            viewModel.location.value?.let {
                LocationSelectionScreen(location = it, onLocationSelected = { locationData ->
                    viewModel.fetchAddress("${locationData.latitude}, ${locationData.longitude}")
                    navHostController.popBackStack()
                })
            }
        }
    }
}