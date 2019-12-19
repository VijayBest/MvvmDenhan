package app.denhan.model.jobs

data class ActiveTenancy(
    val agent_id: Int,
    val borough_id: Int,
    val comments: String,
    val created_date: String,
    val end_date: Any,
    val id: Int,
    val modified_date: String,
    val next_relet_property_id: Any,
    val next_reserved_date: Any,
    val property_id: Int,
    val relet_date: Any,
    val relet_property_id: Any,
    val rent: Int,
    val rent_type: String,
    val reserved_date: Any,
    val reserved_rent: Any,
    val reserved_rent_type: Any,
    val start_date: String,
    val status: Boolean,
    val tenancy_type: String,
    val tenant: Tenant,
    val tenant_id: Int,
    val void_date: Any
)