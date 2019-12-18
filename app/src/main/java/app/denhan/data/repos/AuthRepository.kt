package app.denhan.data.repos

import android.util.Log
import app.denhan.data.api.WebService
import app.denhan.helper.DeviceIdHelper
import app.denhan.helper.getStatusCode
import app.denhan.model.ApiResponse
import app.denhan.model.login.LoginResponse
import app.denhan.util.AppConstants
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

import org.json.JSONObject
import retrofit2.Response
import skycap.android.core.resource.Resource
import skycap.android.core.sharedprefs.SharedPrefsHelper


class  AuthRepository(private val webService: WebService, private val sharedPreferences: SharedPrefsHelper,
                      private val deviceIdHelper: DeviceIdHelper){

   suspend fun userLoginAsync(emailId:String, password:String): Deferred<Resource<LoginResponse?>> {
        return GlobalScope.async {
            try {
                val response: Response<LoginResponse> = webService.userLoginAsync(emailId, password,
                    deviceIdHelper.getDeviceId(),
                    AppConstants.notificationToken, AppConstants.deviceType)
                val userLoginResponse = response.body()
                if (response.code()== ApiResponseCode.SUCCESS_CODE) {

                    Resource.Success(userLoginResponse)

                } else {
                    val jObjError = JSONObject(response.errorBody()?.string())
                    Resource.Error<LoginResponse>(jObjError.getInt("code"))
                }
            } catch (e: Exception) {
                Log.e("exception ", e.message+" "+ e.getStatusCode())

                Resource.Error<LoginResponse>(e.getStatusCode())
            }
        }
    }



}
