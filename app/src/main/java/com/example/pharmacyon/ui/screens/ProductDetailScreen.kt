package com.example.pharmacyon.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pharmacyon.ui.theme.MediRed
import com.example.pharmacyon.viewmodel.AppViewModel

@Composable
fun ProductDetailScreen(navController: NavController, viewModel: AppViewModel) {
    val product by viewModel.selectedProduct.collectAsState() // GET SELECTED DATA
    var quantity by remember { mutableIntStateOf(1) }

    // Safety check if product is null
    if (product == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF3F4F6))) {
        // ... (Keep Image and Header UI) ...

        // CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 300.dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            // ... (Keep Handle) ...

            Column(modifier = Modifier.padding(24.dp)) {
                // DYNAMIC TEXT
                Text(product!!.name, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text(product!!.category, color = Color.Gray, fontSize = 14.sp)

                // ... (Keep Tabs) ...

                // DYNAMIC PRICE
                Row( /* ... */ ) {
                    Text(viewModel.formatMoney(product!!.price), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MediRed)
                    // ... Quantity Buttons ...
                }

                // DYNAMIC DESCRIPTION
                InfoCard(
                    title = "Description",
                    icon = Icons.Outlined.Info,
                    iconColor = Color.Blue,
                    bgColor = Color.Blue.copy(0.1f),
                    content = product!!.description
                )
            }
        }

        // BOTTOM BAR (Add to Basket)
        Surface(modifier = Modifier.align(Alignment.BottomCenter) /* ... */) {
            Row( /* ... */ ) {
                // Calculate Total
                Text(viewModel.formatMoney(product!!.price * quantity), fontSize = 24.sp, fontWeight = FontWeight.Bold)

                Button(
                    onClick = {
                        // Add product X times
                        repeat(quantity) { viewModel.addToCart(product!!) }
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MediRed),
                    // ...
                ) {
                    Text("Add to Basket")
                }
            }
        }
    }
}

@Composable
fun InfoCard(title: String, icon: ImageVector, iconColor: Color, bgColor: Color, content: String) {
    TODO("Not yet implemented")
}