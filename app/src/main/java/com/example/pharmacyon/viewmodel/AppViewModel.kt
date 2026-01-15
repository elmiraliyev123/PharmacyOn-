package com.example.pharmacyon.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pharmacyon.model.ActiveOrder
import com.example.pharmacyon.model.CartItem
import com.example.pharmacyon.model.Product
import com.example.pharmacyon.model.UserProfile
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {

    private val allProducts = listOf(
        Product(
            id = "1",
            name = "CardioFlex 50mg",
            description = "Hypertension treatment. Prescription only.",
            price = 24.99,
            imageUrl = "",
            category = "Heart Health"
        ),
        Product(
            id = "2",
            name = "Panadol Extra",
            description = "Effective for headaches and fever.",
            price = 5.50,
            imageUrl = "",
            category = "Pain Relief"
        ),
        Product(
            id = "3",
            name = "Vitamin C 1000mg",
            description = "Immunity booster effervescent tablets.",
            price = 12.00,
            imageUrl = "",
            category = "Vitamins"
        ),
        Product(
            id = "4",
            name = "N95 Surgical Masks",
            description = "Pack of 10 protective masks.",
            price = 8.99,
            imageUrl = "",
            category = "Medical Gear"
        ),
        Product(
            id = "5",
            name = "Omeprazole 20mg",
            description = "Relief for heartburn and acid reflux.",
            price = 15.40,
            imageUrl = "",
            category = "Digestion"
        )
    )

    private val _products = MutableStateFlow(allProducts)
    val products = _products.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems = _cartItems.asStateFlow()

    private val _user = MutableStateFlow(UserProfile("Elmir Aliyev", "elmir@pharmacyon.az", 150, true, ""))
    val user = _user.asStateFlow()

    private val _activeOrder = MutableStateFlow(ActiveOrder())
    val activeOrder = _activeOrder.asStateFlow()
    private var orderProgressJob: Job? = null

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct = _selectedProduct.asStateFlow()

    fun addToCart(product: Product) {
        val currentCart = _cartItems.value.toMutableList()
        val existingItem = currentCart.find { it.product.id == product.id }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            currentCart.add(CartItem(product, 1))
        }
        _cartItems.value = currentCart
    }

    fun removeFromCart(productId: String) {
        val currentCart = _cartItems.value.toMutableList()
        val existingItem = currentCart.find { it.product.id == productId }

        if (existingItem != null) {
            if (existingItem.quantity > 1) {
                existingItem.quantity--
            } else {
                currentCart.remove(existingItem)
            }
        }
        _cartItems.value = currentCart
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun selectProduct(product: Product) {
        _selectedProduct.value = product
    }

    fun searchProducts(query: String) {
        if (query.isBlank()) {
            _products.value = allProducts
        } else {
            _products.value = allProducts.filter { it.name.contains(query, ignoreCase = true) }
        }
    }

    fun placeOrder() {
        _activeOrder.value = ActiveOrder(
            orderId = "#AZ-${(1000..9999).random()}",
            status = "Preparing",
            courierName = "Finding Courier...",
            courierVehicle = "Motorcycle",
            estimatedMinutes = 25,
            progress = 0.1f
        )
        startOrderProgress()
        clearCart()
    }

    private fun startOrderProgress() {
        orderProgressJob?.cancel()
        orderProgressJob = viewModelScope.launch {
            var phase = 0
            while (true) {
                delay(900)
                _activeOrder.update { current ->
                    if (current.status.isBlank()) return@update current

                    val nextProgress = when (phase) {
                        0 -> (current.progress + 0.08f).coerceAtMost(0.4f)
                        1 -> (current.progress + 0.12f).coerceAtMost(0.75f)
                        else -> (current.progress + 0.05f).coerceAtMost(1f)
                    }

                    val nextStatus = when {
                        nextProgress >= 1f -> "Delivered"
                        nextProgress >= 0.75f -> "On the way"
                        nextProgress >= 0.4f -> "Packing"
                        else -> "Preparing"
                    }

                    phase = (phase + 1) % 3
                    current.copy(progress = nextProgress, status = nextStatus)
                }

                if (_activeOrder.value.progress >= 1f) {
                    orderProgressJob?.cancel()
                    break
                }
            }
        }
    }

    val cartTotal: Double
        get() = _cartItems.value.sumOf { it.product.price * it.quantity }

    fun formatMoney(amount: Double): String {
        return "$${String.format("%.2f", amount)}"
    }
}