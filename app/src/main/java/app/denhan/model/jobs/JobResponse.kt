package app.denhan.model.jobs

data class JobResponse(
    val maintenances: List<Maintenance>,
    val title: String
)