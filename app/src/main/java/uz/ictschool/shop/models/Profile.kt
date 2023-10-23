package uz.ictschool.shop.models

import java.io.Serializable

data class Profile (
    val email: String,
    val firstName: String,
    val gender: String,
    val id: Int,
    val image: String,
    val lastName: String,
    val token: String,
    val username: String
) : Serializable