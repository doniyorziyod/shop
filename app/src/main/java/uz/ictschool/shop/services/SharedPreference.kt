package uz.ictschool.shop.services

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.ictschool.shop.models.Profile

class SharedPreference(context: Context) {
    val shared : SharedPreferences = context.getSharedPreferences("data", 0)
    private val edit: SharedPreferences.Editor = shared.edit()
    private val gson  = Gson()

    companion object {
        var instance : SharedPreference? = null
        fun getInstance(context: Context) : SharedPreference {
            if (instance == null) instance = SharedPreference(context)
            return instance!!
        }
    }

    fun setProfile(profile : Profile) {
        val data = gson.toJson(profile)
        edit.putString("profile", data).apply()
    }

    fun getProfile() : Profile? {
        val data = shared.getString("profile", "")
        if (data == "") return null
        val typeToken = object : TypeToken<Profile>() {}.type
        return gson.fromJson(data, typeToken)
    }

    fun logout(){
        edit.putString("profile", "").apply()
    }
}