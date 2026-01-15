package com.example.pharmacyon.ui.screens

import androidx.compose.foundation.background // <--- THIS WAS MISSING
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pharmacyon.ui.animation.bouncyClick
import com.example.pharmacyon.ui.theme.BgLight
import com.example.pharmacyon.ui.theme.MediRed
import com.example.pharmacyon.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, viewModel: AppViewModel) {
    val searchResults by viewModel.products.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val cartItems by viewModel.cartItems.collectAsState()

    Scaffold(
        bottomBar = { BottomNavBar(navController, "search", cartItems.size) },
        containerColor = BgLight
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            // Real Search Field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    viewModel.searchProducts(it)
                },
                placeholder = { Text("Search medicines...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text("Products", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = searchResults,
                    key = { it.id }
                ) { product ->
                    Card(
                        modifier = Modifier
                            .bouncyClick {
                                viewModel.selectProduct(product)
                                navController.navigate("product_detail")
                            },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Box(modifier = Modifier.fillMaxWidth().height(80.dp).background(Color.Gray.copy(0.1f))) // Placeholder Image
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(product.name, fontWeight = FontWeight.Bold, maxLines = 1)
                            Text(product.category, fontSize = 10.sp, color = Color.Gray)

                            Row(
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(viewModel.formatMoney(product.price), fontWeight = FontWeight.Bold, color = MediRed)
                                IconButton(
                                    onClick = { viewModel.addToCart(product) },
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(MediRed, RoundedCornerShape(4.dp))
                                ) {
                                    Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}