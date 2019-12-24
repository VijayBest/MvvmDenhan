package app.denhan.data.api

import app.denhan.model.ApiResponse
import app.denhan.model.jobs.JobResponse
import app.denhan.model.jobs.MaintenanceJob
import app.denhan.model.login.LoginResponse
import app.denhan.model.owner.OwnerNotAvailableData
import app.denhan.model.uploaad.ImageUploadResponse
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface WebService {

    @FormUrlEncoded
    @POST("api/login")
    suspend fun userLoginAsync(@Field("email") emailId: String, @Field("password") password: String,
                       @Field("device_token") deviceToken: String,
                       @Field("notification_token")notificationToken:String,
                       @Field("device_type") deviceType:Int): Response<LoginResponse>

    @GET("apiv2/maintenances/my_requests")
    suspend fun openJobsAsync():Response<JobResponse>

    @GET("apiv2/maintenances/updated_requests")
    suspend fun inProgressJobs():Response<JobResponse>

    @GET("apiv2/maintenances/completed_requests")
    suspend fun completedJobs():Response<JobResponse>

    @FormUrlEncoded
    @POST("api/maintenance-jobs/add")
    suspend fun addTaskAsync(@Field("maintenance_id") maintenanceId: Int,
                             @Field("job_detail") taskTitle:String):Response<ApiResponse<MaintenanceJob>>


    @GET("api/mainte-unavail-logs")
    suspend fun unavailableOwnerAsync(@Query("id")maintenanceId:Int):Response<ApiResponse<ArrayList<OwnerNotAvailableData>>>

    @Multipart
    @POST("/api/uploads/manage")
    suspend fun uploadImageAsync(@Part filePart: MultipartBody.Part, @Part tbl: MultipartBody.Part):Response<ImageUploadResponse>


    @FormUrlEncoded
    @POST("api/mainte-unavail-logs/add")
    suspend fun addNewLogsAsync(@Field("images[]") imageName: ArrayList<String>,
                             @Field("comment") comment:String, @Field("maintenance_id")maintenanceId: Int):Response<ResponseBody>

}