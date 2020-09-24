package com.rocappdev.commercelist.network

import com.rocappdev.commercelist.domain.Commerce
import retrofit2.http.GET

interface ApiService {

    @GET(".")
    suspend fun getCommerceList(): List<Commerce>

}