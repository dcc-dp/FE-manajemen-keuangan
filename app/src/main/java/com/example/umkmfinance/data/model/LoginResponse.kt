package com.example.umkmfinance.data.model

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: LoginData?
)

data class LoginData(
    val token: String,
    val user: LoginUser
)

data class LoginUser(
    val id: Long,
    val nama: String,
    val username: String,
    val email: String?,
    val no_hp: String?,
    val roles: List<String>
)