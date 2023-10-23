package uz.ictschool.shop.models

data class ProductNumber(
    val discountPercentage: Double,
    val discountedPrice: Int,
    val id: Int,
    val price: Int,
    val quantity: Int,
    val title: String,
    val total: Int
)
