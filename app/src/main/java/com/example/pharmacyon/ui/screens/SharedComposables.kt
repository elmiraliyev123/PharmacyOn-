package com.example.pharmacyon.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.pharmacyon.ui.theme.MediRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavBar(navController: NavController, currentRoute: String, cartCount: Int) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") },
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = MediRed, indicatorColor = Color.Transparent, selectedTextColor = MediRed)
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, null) },
            label = { Text("Search") },
            selected = currentRoute == "search",
            onClick = { navController.navigate("search") }
        )
        NavigationBarItem(
            icon = {
                BadgedBox(
                    badge = {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = cartCount > 0,
                            enter = scaleIn() + fadeIn(),
                            exit = scaleOut() + fadeOut()
                        ) {
                            Badge { Text(cartCount.toString()) }
                        }
                    }
                ) {
                    Icon(Icons.Default.ShoppingCart, null)
                }
            },
            label = { Text("Cart") },
            selected = currentRoute == "cart",
            onClick = { navController.navigate("cart") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Profile") },
            selected = currentRoute == "profile",
            onClick = { navController.navigate("profile") }
        )
    }
}