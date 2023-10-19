package uz.ictschool.shop.apis

import retrofit2.Call
import retrofit2.http.GET
import uz.ictschool.shop.models.ProductData

interface APIService {
    @GET("/products")
    fun getAll(): Call<ProductData>
}