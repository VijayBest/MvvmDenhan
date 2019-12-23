package app.denhan.model.owner

data class OwnerNotAvailableData(
    val comment: String,
    val created_date: String,
    val id: Int,
    val mainte_unavail_images: List<MainteUnavailImage>,
    val maintenance_id: Int
)