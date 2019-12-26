package app.denhan.model.subtask

data class MaintenanceJobImage(
    val created_date: String,
    val id: Int,
    val image_path: String,
    val maintenance_job_id: Int,
    val path: String,
    val type: String
)