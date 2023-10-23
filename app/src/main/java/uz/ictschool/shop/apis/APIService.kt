package uz.ictschool.shop.apis

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import uz.ictschool.shop.models.CartData
import uz.ictschool.shop.models.Login
import uz.ictschool.shop.models.Product
import uz.ictschool.shop.models.ProductData
import uz.ictschool.shop.models.Profile

interface APIService {
    @GET("/products")
    fun getAllProducts(): Call<ProductData>

    @GET("/products/categories")
    fun getAllCategories(): Call<List<String>>

    @GET("/products/category/{category}")
    fun getByCategory(@Path("category") category : String): Call<ProductData>

    @GET("/products/search")
    fun getProductsBySearch(@Query("q") query : String): Call<ProductData>

    @POST("/auth/login")
    fun login(@Body login: Login): Call<Profile>

    @GET("/carts/user/{id}")
    fun getCartsOfUser(@Path("id") id : Int): Call<CartData>
}