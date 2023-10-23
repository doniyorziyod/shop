package uz.ictschool.shop.models
data class Cart(
    val discountedTotal: Int,
    val id: Int,
    val products: List<ProductNumber>,
    val total: Int,
    val totalProducts: Int,
    val totalQuantity: Int,
    val userId: Int
)
