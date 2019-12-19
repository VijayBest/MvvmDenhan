package app.denhan.data.api

import app.denhan.model.jobs.JobResponse
import app.denhan.model.login.LoginResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

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

}