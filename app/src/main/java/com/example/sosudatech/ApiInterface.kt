package com.example.sosudatech

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {

    @POST("login")
    fun getLogin(@Body body: ItemRequestedData): Call<LoginResponsedata>

    @GET("users?page=1")
    fun getDetails(): Call<DetailsDataResponse>
}