package com.example.pharmacyon.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pharmacyon.ui.theme.MediRed
import com.example.pharmacyon.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveTrackingScreen(navController: NavController, viewModel: AppViewModel) {
    val activeOrder by viewModel.activeOrder.collectAsState()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val animatedProgress by animateFloatAsState(
        targetValue = activeOrder.progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 550, easing = FastOutSlowInEasing),
        label = "orderProgress"
    )
    val statusText = if (activeOrder.status.isNotBlank()) activeOrder.status else "Awaiting order"

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 140.dp, // How much is visible when closed
        sheetContainerColor = Color.White,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetShadowElevation = 20.dp,
        // --- DRAGGABLE PART (THE SHEET) ---
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .heightIn(min = 300.dp) // Minimum height when expanded
            ) {
                // 1. The Grey Handle Bar
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .background(Color.LightGray, RoundedCornerShape(50))
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 2. Order Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Order ${activeOrder.orderId}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        AnimatedContent(
                            targetState = "Est. Time: ${activeOrder.estimatedMinutes} mins",
                            label = "eta"
                        ) { eta ->
                            Text(eta, color = MediRed, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    Surface(
                        color = Color(0xFFF3F4F6),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        AnimatedContent(targetState = statusText, label = "status") { status ->
                            Text(
                                status,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 3. Progress Steps (Visual)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val segments = listOf(0.25f, 0.6f, 1f)
                    segments.forEachIndexed { index, threshold ->
                        StepDot(
                            isActive = animatedProgress >= threshold || index == 0,
                            isCompleted = animatedProgress >= threshold
                        )
                        if (index != segments.lastIndex) {
                            StepLine(
                                fillFraction = ((animatedProgress - threshold).coerceIn(0f, 0.35f)) / 0.35f
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Request", fontSize = 10.sp, color = MediRed)
                    Text("Confirm", fontSize = 10.sp, color = Color.Black)
                    Text("Deliver", fontSize = 10.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(
                    progress = animatedProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(999.dp)),
                    color = MediRed,
                    trackColor = Color.LightGray.copy(alpha = 0.3f)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 4. Cancel Button
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Icon(Icons.Default.Close, null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cancel Request", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { padding ->
        // --- BACKGROUND PART (THE MAP) ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFE5E7EB)) // Grey "Map" Background
        ) {
            // Fake Map Elements
            Icon(
                Icons.Default.LocationOn,
                contentDescription = null,
                tint = MediRed,
                modifier = Modifier.align(Alignment.Center).offset(y = (-40).dp).size(48.dp)
            )

            // Back Button
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(top = 48.dp, start = 16.dp)
                    .background(Color.White, CircleShape)
                    .shadow(elevation = 4.dp, shape = CircleShape)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }

            // "Searching Area" Pill
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 48.dp),
                shape = RoundedCornerShape(50),
                shadowElevation = 4.dp,
                color = Color.White
            ) {
                Text(
                    "Searching area...",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Helper Composable for the dots
@Composable
fun StepDot(isActive: Boolean, isCompleted: Boolean) {
    val color = if (isActive || isCompleted) MediRed else Color.LightGray
    Box(
        modifier = Modifier
            .size(16.dp)
            .background(color, CircleShape)
            .border(2.dp, Color.White, CircleShape)
    )
}

@Composable
fun RowScope.StepLine(fillFraction: Float) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(2.dp)
            .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(999.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fillFraction.coerceIn(0f, 1f))
                .background(MediRed, RoundedCornerShape(999.dp))
        )
    }
}
