package com.rocappdev.commercelist.repository

import com.rocappdev.commercelist.domain.Commerce
import com.rocappdev.commercelist.network.ApiClient
import com.rocappdev.commercelist.util.NetworkUtil
import retrofit2.HttpException

class CommerceRepository {
    private val apiClient = ApiClient()

    suspend fun getCommerces(): CommerceResponse {
        val response: List<Commerce>

        try {
            response = apiClient.getCommerces()
        } catch (e: Exception) {
            return getError(e)
        }
        return CommerceList(response)
    }

    private fun getError(e: Exception): ErrorResponse {
        val networkUtil = NetworkUtil()
        var message = "Unexpected error"

        if (e is HttpException)
            message = e.message()
        else if (networkUtil.checkNetwork().not())
            message = "No internet connection"

        return ErrorResponse(message)
    }
}