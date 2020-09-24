package com.rocappdev.commercelist.domain

import java.io.Serializable

data class Contact(
    val phone: String?,
    val email: String?,
    val web: String?,
) : Serializable