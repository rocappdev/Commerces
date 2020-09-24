package com.rocappdev.commercelist.domain

import java.io.Serializable

data class Commerce(
    val id: String?,
    var name: String?,
    val category: String?,
    val description: String?,
    val shortDescription: String?,
    val address: Address?,
    val logo: Logo?,
    val features: List<String>?,
    val contact: Contact?,
    val social: Social?,
    val latitude: Double,
    val longitude: Double
) : Serializable