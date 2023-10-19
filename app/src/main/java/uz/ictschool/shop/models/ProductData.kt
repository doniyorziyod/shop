package uz.ictschool.shop.models

data class ProductData(
    val limit: Int,
    val skip: Int,
    val total: Int,
    val products: List<Product>
)