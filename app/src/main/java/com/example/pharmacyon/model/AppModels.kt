package com.example.pharmacyon.model

// 1. Represents a Product in the shop
data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "", // In a real app, this is a URL
    val category: String = "",
    val isDiscounted: Boolean = false,
    val discountAmount: Double = 0.0
)

// 2. Represents an Item inside the Cart
data class CartItem(
    val product: Product = Product(),
    var quantity: Int = 0
) {
    val totalPrice: Double get() = (product.price * quantity)
}

// 3. Represents the User's Profile
data class UserProfile(
    val name: String = "",
    val email: String = "",
    val points: Int = 0,
    val isVerified: Boolean = false,
    val profilePicUrl: String = ""
)

// 4. Represents a Live Order
data class ActiveOrder(
    val orderId: String = "",
    val courierName: String = "",
    val courierVehicle: String = "",
    val estimatedMinutes: Int = 0,
    val status: String = "", // "On the way", "Delivered"
    val progress: Float = 0f // 0.0 to 1.0
)