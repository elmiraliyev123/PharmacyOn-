package com.example.pharmacyon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.pharmacyon.ui.screens.*
import com.example.pharmacyon.ui.theme.PharmacyOnTheme
import com.example.pharmacyon.viewmodel.AppViewModel

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PharmacyOnTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val appViewModel: AppViewModel = viewModel()
                    val navController = rememberNavController()

                    val motionDurationMs = remember { 350 }

                    @OptIn(ExperimentalAnimationApi::class)
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(durationMillis = motionDurationMs)
                            ) + fadeIn(animationSpec = tween(durationMillis = 220))
                        },
                        exitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> -fullWidth },
                                animationSpec = tween(durationMillis = motionDurationMs)
                            ) + fadeOut(animationSpec = tween(durationMillis = 220))
                        },
                        popEnterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { fullWidth -> -fullWidth },
                                animationSpec = tween(durationMillis = motionDurationMs)
                            ) + fadeIn(animationSpec = tween(durationMillis = 220))
                        },
                        popExitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> fullWidth },
                                animationSpec = tween(durationMillis = motionDurationMs)
                            ) + fadeOut(animationSpec = tween(durationMillis = 220))
                        }
                    ) {
                        composable("home") { HomeScreen(navController, appViewModel) }
                        composable("search") { SearchScreen(navController, appViewModel) }
                        composable("cart") { CartScreen(navController, appViewModel) }
                        composable("profile") { ProfileScreen(navController, appViewModel) }
                        composable("tracking") { LiveTrackingScreen(navController, appViewModel) }
                        composable("country") { CountrySelectionScreen(navController) }
                        composable("login") { LoginScreen(navController) }
                        composable("signup") { LoginScreen(navController) } // Placeholder for SignUpScreen
                        composable("placing_order") { PlacingOrderScreen(navController) }
                        // Note: HomeScreen navigates to "product_detail"; add the destination so it animates too.
                        composable("product_detail") { ProductDetailScreen(navController, appViewModel) }
                    }
                }
            }
        }
    }
}

