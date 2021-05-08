package com.torresj.PS5Stock.dtos

data class PS5Status(
    val available: Boolean,
    val statusText: String = "",
    var hasChanged: Boolean = false,
    val web: Web? = null,
    val url: String = ""
)
