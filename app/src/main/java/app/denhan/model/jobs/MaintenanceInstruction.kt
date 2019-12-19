package app.denhan.model.jobs

data class MaintenanceInstruction(
    val created_date: String,
    val detail: String,
    val id: Int,
    val maintenance_id: Int,
    val modified_date: String
)