package app.denhan.data.api

import app.denhan.model.ApiResponse
import app.denhan.model.login.UserDetail
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface WebService {

    @FormUrlEncoded
    @POST("login")
    fun userLoginAsync(@Field("email") emailId: String, @Field("password") password: String,
                       @Field("device_token") deviceToken: String,
                       @Field("notification_token")notificationToken:String,
                       @Field("device_type") deviceType:Int): Deferred<Response<ApiResponse<UserDetail>>>

}