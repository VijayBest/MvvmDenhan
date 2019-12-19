package app.denhan.model.jobs

data class Property(
    val account_id: Int,
    val account_name: String,
    val account_number: String,
    val active_tenancy: ActiveTenancy,
    val address: String,
    val agent_id: Int,
    val attachments: List<Attachment>,
    val bank_name: String,
    val bank_type: Any,
    val borough_id: Int,
    val council_tax: Int,
    val council_tax_band: String,
    val council_tax_lose: String,
    val created_date: String,
    val dont_have_gas: Int,
    val elec_key_location: String,
    val elec_meter_location: String,
    val elec_meter_reading: String,
    val electric_meter: Int,
    val f_council_tax: String,
    val f_lease_break_date: String,
    val f_lease_end_date: String,
    val f_lease_end_reminder: String,
    val f_lease_start_date: String,
    val f_relet_date: String,
    val f_rent: String,
    val floor_types: String,
    val gas_key_location: String,
    val gas_meter: Int,
    val gas_meter_location: String,
    val gas_meter_reading: String,
    val have_elec_certificate: Int,
    val have_epc_certificate: Int,
    val have_gas_certificate: Int,
    val have_lift: Boolean,
    val id: Int,
    val is_active: Boolean,
    val is_archived: Boolean,
    val is_lease_end: Boolean,
    val is_monthly_elec_bill: Int,
    val is_monthly_gas_bill: Int,
    val is_pay_elec: Int,
    val is_pay_gas: Int,
    val landlord_lose: String,
    val latitude: Any,
    val lease_break_clause: String,
    val lease_end: String,
    val lease_end_reminder: String,
    val lease_start: String,
    val longitude: Any,
    val missing_certificates: String,
    val nightly_rent: Double,
    val p_lease_end: String,
    val payment_method: String,
    val postcode: String,
    val prop: Any,
    val property_subtype: String,
    val property_type: String,
    val reason_for_leaving: Any,
    val reason_text: String,
    val ref_code: String,
    val rent: Double,
    val rent_day: Int,
    val rent_type: String,
    val show_reason_text: String,
    val sort_code: String,
    val tenant_lose: String,
    val tenant_nightly_rent: String,
    val total_lose: String,
    val total_rooms: Int,
    val updated_date: String,
    val void_date: String,
    val void_days: Int,
    val water_stop_location: String,
    val without_symbol_council_tax: Int,
    val without_symbol_landlord_lose: Int,
    val without_symbol_tenant_lose: Int
)