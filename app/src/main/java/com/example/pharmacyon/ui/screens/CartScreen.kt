package com.example.pharmacyon.ui.screens

import androidx.compose.foundation.background
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.RemoveCircle
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
import com.example.pharmacyon.ui.animation.bouncyPress
import com.example.pharmacyon.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, viewModel: AppViewModel) {
    val cartItems by viewModel.cartItems.collectAsState()

    // If these are defined in HomeScreen.kt, you can remove these two lines.
    // If not, keep them here to fix the red error immediately.
    val MediRed = Color(0xFFEF4444)
    val BgLight = Color(0xFFF6F8F7)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "My Basket (${cartItems.sumOf { it.quantity }})",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.clearCart() }) {
                        Text(text = "Clear All", color = MediRed)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.placeOrder() // <--- WRITES TO FIREBASE
                        navController.navigate("tracking") // Go to tracking
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .bouncyPress(),
                    colors = ButtonDefaults.buttonColors(containerColor = MediRed),
                    shape = RoundedCornerShape(12.dp),
                    enabled = cartItems.isNotEmpty() // Disable checkout if cart is empty
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Checkout",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = viewModel.formatMoney(viewModel.cartTotal),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },
        containerColor = BgLight
    ) { padding ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Your basket is empty", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(BgLight)
            ) {
                items(
                    items = cartItems,
                    key = { it.product.id }
                ) { item ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            // Smooth height changes when quantity text changes.
                            .animateContentSize()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Product Info
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.product.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = viewModel.formatMoney(item.product.price),
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }

                            // Quantity Controls
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { viewModel.removeFromCart(item.product.id) }) {
                                    Icon(
                                        imageVector = Icons.Default.RemoveCircle,
                                        contentDescription = "Remove",
                                        tint = Color.Gray
                                    )
                                }
                                Text(
                                    text = item.quantity.toString(),
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(onClick = { viewModel.addToCart(item.product) }) {
                                    Icon(
                                        imageVector = Icons.Default.AddCircle,
                                        contentDescription = "Add",
                                        tint = MediRed
                                    )
                                }
                            }
                        }
                    }
                }
                // Extra space at bottom so list isn't hidden by bottom bar
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}