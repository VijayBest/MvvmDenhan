package app.denhan.model.jobs

data class MaintenanceJob(
    val amount: Int,
    val comments: String,
    val end_time: String,
    val f_end_time: String,
    val f_modified_date: String,
    val f_start_time: String,
    val id: Int,
    val job_detail: String,
    val labour_charges: Int,
    val maintenance_id: Int,
    val modified_date: String,
    val start_time: String,
    val status: String,
    val tenant_request_issue_id: Any
)