package app.denhan.model.owner

data class MainteUnavailImage(
    val created_date: String,
    val id: Int,
    val image_path: String,
    val mainte_unavail_log_id: Int,
    val path: String
)