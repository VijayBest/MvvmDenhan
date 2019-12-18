package app.denhan.data.api

import app.denhan.model.ApiResponse
import app.denhan.model.jobs.JobResponse
import app.denhan.model.login.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface WebService {

    @FormUrlEncoded
    @POST("login")
    suspend fun userLoginAsync(@Field("email") emailId: String, @Field("password") password: String,
                       @Field("device_token") deviceToken: String,
                       @Field("notification_token")notificationToken:String,
                       @Field("device_type") deviceType:Int): Response<LoginResponse>

    @GET("maintenances/my_requests")
    suspend fun openJobsAsync():Response<JobResponse>

    @GET("maintenances/updated_requests")
    suspend fun inProgressJobs():Response<JobResponse>

    @GET("maintenances/completed_requests")
    suspend fun completedJobs():Response<JobResponse>

}