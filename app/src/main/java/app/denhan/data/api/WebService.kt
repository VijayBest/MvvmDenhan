package app.denhan.data.api

import app.denhan.model.ApiResponse
import app.denhan.model.jobmedia.UploadJobMedia
import app.denhan.model.jobs.JobResponse
import app.denhan.model.jobs.Maintenance
import app.denhan.model.jobs.MaintenanceJob
import app.denhan.model.login.LoginResponse
import app.denhan.model.owner.OwnerNotAvailableData
import app.denhan.model.subtask.JobDetailResponse
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

    @GET("api/maintenances/view/{id}")
    suspend fun jobDetailsAsync(@Path("id")id:Int):Response<JobDetailResponse>

    @Multipart
    @POST("/api/maintenance-job-images/add")
    suspend fun uploadJobMediaAsync(@Part filePart: MultipartBody.Part, @Part maintenance_job_id: MultipartBody.Part,
                                    @Part type: MultipartBody.Part):Response<ApiResponse<ArrayList<UploadJobMedia>>>

    @DELETE("api/maintenance-job-images/delete/{id}")
    suspend fun deleteMediaAsync(@Path("id")id:Int):Response<ResponseBody>

    @FormUrlEncoded
    @POST("api/maintenance-jobs/edit/{id}")
    suspend fun saveTaskStatus(
        @Path("id")id:Int,
        @Field("id") taskId: Int,
        @Field("status") taskStatus: String,
        @Field("comment") comment: String,
        @Field("start_time") startTime: String,
        @Field("end_time") endTime: String,
        @Field("amount") materialCost: Double,
        @Field("labour_charges") labourCharges: Double
    ): Response<ResponseBody>


}