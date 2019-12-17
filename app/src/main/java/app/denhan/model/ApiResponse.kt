package app.denhan.model

data class ApiResponse<T>(
    val code: Int,
    val `data`: T? = null
)