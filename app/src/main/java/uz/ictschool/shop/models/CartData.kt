package uz.ictschool.shop.models

data class CartData(
    val carts: List<Cart>,
    val limit: Int,
    val skip: Int,
    val total: Int
)
