package app.denhan.model.jobs

import app.denhan.model.subtask.MaintenanceJobImage

data class MaintenanceJob(
    val amount: Int,
    val comments: String,
    val end_time: String,
    val f_end_time: String,
    val f_modified_date: String?,
    val f_start_time: String,
    val id: Int,
    val job_detail: String,
    val labour_charges: Double,
    val maintenance_id: Int,
    val modified_date: String,
    val start_time: String,
    var status: String,
    val tenant_request_issue_id: Any,
    val maintenance_job_images: List<MaintenanceJobImage>?
)