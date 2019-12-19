package app.denhan.model.jobs

data class Tenant(
    val acc_id: Int,
    val account_type: String,
    val address: Any,
    val agent_name: String,
    val cd_balance: Any,
    val corporate_email: Any,
    val created_date: String,
    val designation: Any,
    val e_code: String,
    val ejabberd_id: Any,
    val ejabberd_pass: Any,
    val email: String,
    val first_name: String,
    val full_name: String,
    val full_profile_name: String,
    val home_contact_person_details: Any,
    val home_phone: String,
    val id: Int,
    val is_active: Boolean,
    val is_archived: Boolean,
    val last_name: String,
    val local_ref: String,
    val marketing: String,
    val marketing_detail: Any,
    val middle_name: String,
    val mobile: String,
    val mobile_contact_person_details: Any,
    val office_contact_person_details: Any,
    val office_phone: String,
    val postcode: Any,
    val profile_pic: String,
    val profile_pic_url: String,
    val reason_for_leaving: Any,
    val reason_text: Any,
    val ref_code: String,
    val reset_password_token: String,
    val service_type: String,
    val title: String,
    val trouble_tenant: Boolean,
    val updated_date: String
)