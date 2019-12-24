package app.denhan.model.uploaad

data class File(
    val delete_type: String,
    val delete_url: String,
    val medium_url: String,
    val name: String,
    val size: Int,
    val thumb_path: String,
    val thumbnail_url: String,
    val type: String,
    val url: String
)