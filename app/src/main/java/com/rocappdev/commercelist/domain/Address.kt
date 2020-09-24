package com.rocappdev.commercelist.domain

import java.io.Serializable

data class Address(
    val city: String?,
    val country: String?,
    val street: String?,
    val zip: String?
) : Serializable