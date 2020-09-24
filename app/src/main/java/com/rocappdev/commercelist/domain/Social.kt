package com.rocappdev.commercelist.domain

import java.io.Serializable

data class Social(
    val facebook: String?,
    val instagram: String?,
    val twitter: String?
) : Serializable