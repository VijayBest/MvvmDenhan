package app.denhan.model

import app.denhan.model.login.UserDetail

data class ApiResponse<T>(
    val code: Int,
    val `data`: T?=null,
    val message: String,
    val valid: Boolean
)