package app.denhan.model.subtask

import app.denhan.model.jobs.Maintenance

data class JobDetailResponse(
    val maintenance: Maintenance,
    val title: String
)