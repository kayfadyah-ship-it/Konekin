package com.example.konekin.network

import com.example.konekin.model.UserDetail
import com.example.konekin.model.Users
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("employees")
    fun getAllUsers(): Call<Users>

    // Endpoint untuk mendapatkan detail karyawan berdasarkan ID
    @GET("employee/{id}")
    fun getUserDetail(@Path("id") id: Int): Call<UserDetail>
}