package app.denhan.model.login

data class LoginResponse(
    val code: Int,
    val `data`: List<UserDetail>,
    val message: String,
    val valid: Boolean
)