package app.denhan.model.jobmedia

data class UploadJobMedia(
    val id: Int,
    val image_file: ImageFile,
    val image_path: String,
    val maintenance_job_id: Int,
    val path: String,
    val type: String
)