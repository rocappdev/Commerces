package com.rocappdev.commercelist.repository

import com.rocappdev.commercelist.domain.Commerce

sealed class CommerceResponse

data class CommerceList(val list: List<Commerce> = listOf()) : CommerceResponse()

data class ErrorResponse(val message: String) : CommerceResponse()