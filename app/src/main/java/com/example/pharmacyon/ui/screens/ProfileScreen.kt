package com.example.pharmacyon.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import com.example.pharmacyon.ui.animation.bouncyClick
import com.example.pharmacyon.ui.theme.BgLight
import com.example.pharmacyon.ui.theme.MediRed
import com.example.pharmacyon.viewmodel.AppViewModel

private data class ProfileCard(
    val title: String,
    val subtitle: String,
    val badge: String? = null,
    val badgeTint: Color = MediRed.copy(alpha = 0.14f),
    val onClick: (() -> Unit)? = null
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: AppViewModel) {
    val user by viewModel.user.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val activeOrder by viewModel.activeOrder.collectAsState()
    val orders = remember(activeOrder) {
        buildList {
            if (activeOrder.status.isNotBlank()) {
                add(
                    ProfileCard(
                        title = "${activeOrder.orderId.takeIf { it.isNotBlank() } ?: "Active order"}",
                        subtitle = activeOrder.status,
                        badge = "Track",
                        onClick = { navController.navigate("tracking") }
                    )
                )
            }
            addAll(
                listOf(
                    ProfileCard(
                        title = "Metformin 500mg",
                        subtitle = "Delivered yesterday",
                        badge = "Repeat"
                    ),
                    ProfileCard(
                        title = "Aspirin 81mg",
                        subtitle = "Delivered Oct 24",
                        badge = "Repeat",
                        badgeTint = Color(0xFFFFE4E4)
                    )
                )
            )
        }
    }
    val paymentMethods = remember {
        listOf(
            ProfileCard(
                title = "Visa •••• 4242",
                subtitle = "Expires 09/26",
                badge = "Default",
                onClick = {}
            ),
            ProfileCard(
                title = "Add new card",
                subtitle = "Link another payment",
                onClick = {}
            )
        )
    }
    val prescriptions = remember {
        listOf(
            ProfileCard(
                title = "2 Active Refills",
                subtitle = "Ready for pickup",
                badge = "View",
                onClick = {}
            ),
            ProfileCard(
                title = "Scan Prescription",
                subtitle = "Quick Action",
                badge = "Scan",
                onClick = {}
            )
        )
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController, "profile", cartItems.size) },
        containerColor = BgLight
    ) { padding ->
        val scrollState = rememberScrollState()
        val parallaxShift = remember { mutableStateOf(0f) }

        LaunchedEffect(scrollState.value) {
            parallaxShift.value = scrollState.value * 0.2f
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .graphicsLayer { translationY = -parallaxShift.value }
                    .background(BgLight)
            ) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(128.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape),
                    tonalElevation = 6.dp,
                ) {
                    if (user.profilePicUrl.isNotBlank()) {
                        Image(
                            painter = rememberAsyncImagePainter(user.profilePicUrl),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MediRed,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(user.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(user.email, color = Color.Gray)
                Spacer(Modifier.height(16.dp))
                Surface(color = Color(0xFFFEF2F2), shape = CircleShape) {
                    Text(
                        "${user.points} MediPoints",
                        color = MediRed,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            Section(
                title = "Recent Orders",
                items = orders
            )

            Section(
                title = "Payment Methods",
                items = paymentMethods
            )

            Section(
                title = "Prescriptions",
                items = prescriptions
            )

            Spacer(Modifier.height(80.dp))
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun Section(title: String, items: List<ProfileCard>) {
    Column(Modifier.padding(horizontal = 20.dp)) {
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        items.forEachIndexed { index, card ->
            val delay = index * 50
            AnimatedCard(card = card, animationDelay = delay)
            Spacer(Modifier.height(12.dp))
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AnimatedCard(card: ProfileCard, animationDelay: Int) {
    val springSpec = remember { spring<Float>(dampingRatio = Spring.DampingRatioLowBouncy) }
    val appearTransition = remember(animationDelay) {
        fadeIn(animationSpec = tween(durationMillis = 220, delayMillis = animationDelay)) +
            slideInVertically(
                animationSpec = tween(durationMillis = 300, delayMillis = animationDelay)
            ) { it / 6 }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .bouncyClick { card.onClick?.invoke() }
            .graphicsLayer {
                shadowElevation = 12f
                shape = RoundedCornerShape(24.dp)
                clip = true
            },
        color = Color.White,
        tonalElevation = 3.dp,
        shape = RoundedCornerShape(24.dp)
    ) {
        AnimatedContent(
            targetState = card.subtitle,
            transitionSpec = {
                appearTransition with fadeOut(animationSpec = tween(120))
            },
            label = "cardSubtitle"
        ) { subtitle ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(card.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(subtitle, color = Color.Gray, fontSize = 13.sp)
                }
                card.badge?.let {
                    Surface(
                        color = card.badgeTint,
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Text(
                            text = it,
                            color = MediRed,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
