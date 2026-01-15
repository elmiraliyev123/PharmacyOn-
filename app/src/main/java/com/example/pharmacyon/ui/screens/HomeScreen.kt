package com.example.pharmacyon.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun HomeScreen(navController: NavController, viewModel: AppViewModel) {
    val user by viewModel.user.collectAsState()
    val products by viewModel.products.collectAsState() // REAL DB DATA
    val activeOrder by viewModel.activeOrder.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()

    Scaffold(
        bottomBar = { BottomNavBar(navController, "home", cartItems.size) },
        containerColor = BgLight
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            // Header
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Hello, ${user.name}", fontSize = 14.sp, color = Color.Gray)
                    Row(modifier = Modifier.clickable { navController.navigate("country") }) {
                        Text("123 Nizami St", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.ExpandMore, contentDescription = null)
                    }
                }
                // Notification Icon... (Keep as is)
            }

            // Active Order Card (Clickable)
            if (activeOrder.status != "Pending") {
                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.bouncyClick { navController.navigate("tracking") }
                ) {
                    // ... (Keep your existing Order Card UI here) ...
                    // Just make sure it uses 'activeOrder'
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Active Order: ${activeOrder.status}", fontWeight = FontWeight.Bold)
                        Text("Tap to track", color = MediRed)
                    }
                }
            }

            // REAL PRODUCTS LIST
            Spacer(modifier = Modifier.height(24.dp))
            Text("Popular Medicines", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            // Display rows of products
            Column {
                products.forEach { product ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .bouncyClick {
                                viewModel.selectProduct(product) // Save selection
                                navController.navigate("product_detail") // Go to detail
                            }
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(60.dp).background(Color.Gray.copy(0.1f), RoundedCornerShape(8.dp)))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(product.name, fontWeight = FontWeight.Bold)
                                Text(product.category, fontSize = 12.sp, color = Color.Gray)
                                Text(viewModel.formatMoney(product.price), fontWeight = FontWeight.Bold, color = MediRed)
                            }
                            IconButton(onClick = { viewModel.addToCart(product) }) {
                                Icon(Icons.Default.AddCircle, null, tint = MediRed)
                            }
                        }
                    }
                }
            }
        }
    }
}
