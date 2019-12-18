package app.denhan.data.repos

import android.util.Log
import app.denhan.data.api.WebService
import app.denhan.helper.DeviceIdHelper
import app.denhan.helper.getStatusCode
import app.denhan.model.ApiResponse
import app.denhan.model.jobs.JobResponse
import app.denhan.model.login.LoginResponse
import app.denhan.util.AppConstants
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody

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
                    AppConstants.sessionToken = response.body()?.data?.get(0)?.session_token?:""
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

    /*getOpenJobsAsync=> get the All Open user
    * The user token will pass in Header section
    * */
    suspend fun getOpenJobsAsync(): Deferred<Resource<JobResponse?>> {
        return GlobalScope.async {
            try {
                val response: Response<JobResponse> = webService.openJobsAsync()
                val openJobsResponse =response.body()
                if (response.code()== ApiResponseCode.SUCCESS_CODE) {
                    Resource.Success(openJobsResponse)

                } else {
                    val jObjError = JSONObject(response.errorBody()?.string())
                    Resource.Error<JobResponse>(jObjError.getInt("code"))
                }
            } catch (e:Exception) {
                Log.e("exception ", e.message + " " + e.getStatusCode())
                Resource.Error<JobResponse>(e.getStatusCode())
            }
        }
    }

    /*getInProgressJobsAsync=> get the All the uncompleted
  * The user token will pass in Header section
  * */
    suspend fun getInProgressJobsAsync(): Deferred<Resource<JobResponse?>> {
        return GlobalScope.async {
            try {
                val response: Response<JobResponse> = webService.inProgressJobs()
                val inProgressJobs =response.body()
                if (response.code()== ApiResponseCode.SUCCESS_CODE) {
                    Resource.Success(inProgressJobs)

                } else {
                    val jObjError = JSONObject(response.errorBody()?.string())
                    Resource.Error<JobResponse>(jObjError.getInt("code"))
                }
            } catch (e:Exception) {
                Log.e("exception ", e.message + " " + e.getStatusCode())
                Resource.Error<JobResponse>(e.getStatusCode())
            }
        }
    }

    /*getInProgressJobsAsync=> get the All the uncompleted
 * The user token will pass in Header section
 * */
    suspend fun getCompletedJobsAsync(): Deferred<Resource<JobResponse?>> {
        return GlobalScope.async {
            try {
                val response: Response<JobResponse> = webService.completedJobs()
                val completedJobsResponse =response.body()
                if (response.code()== ApiResponseCode.SUCCESS_CODE) {
                    Resource.Success(completedJobsResponse)

                } else {
                    val jObjError = JSONObject(response.errorBody()?.string())
                    Resource.Error<JobResponse>(jObjError.getInt("code"))
                }
            } catch (e:Exception) {
                Log.e("exception ", e.message + " " + e.getStatusCode())
                Resource.Error<JobResponse>(e.getStatusCode())
            }
        }
    }




}
