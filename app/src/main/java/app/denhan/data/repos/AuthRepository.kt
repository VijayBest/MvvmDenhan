package app.denhan.data.repos

import android.util.Log
import app.denhan.data.api.WebService
import app.denhan.helper.DeviceIdHelper
import app.denhan.helper.getStatusCode
import app.denhan.model.ApiResponse
import app.denhan.model.jobs.JobResponse
import app.denhan.model.jobs.Maintenance
import app.denhan.model.jobs.MaintenanceJob
import app.denhan.model.login.LoginResponse
import app.denhan.model.owner.OwnerNotAvailableData
import app.denhan.model.subtask.JobDetailResponse
import app.denhan.model.uploaad.ImageUploadResponse
import app.denhan.util.AppConstants
import kotlinx.coroutines.*
import okhttp3.MultipartBody
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
            } catch (e:Exception) {
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


    suspend fun addTaskAsync(title:String, maintenanceId:Int): Deferred<Resource<ApiResponse<MaintenanceJob>?>> {
        return GlobalScope.async {
            try {
                val response: Response<ApiResponse<MaintenanceJob>> = webService.addTaskAsync(maintenanceId,title)
                val completedJobsResponse =response.body()
                if (response.code()== ApiResponseCode.SUCCESS_CODE) {
                    Resource.Success(completedJobsResponse)

                } else {
                    val jObjError = JSONObject(response.errorBody()?.string())
                    Resource.Error<ApiResponse<MaintenanceJob>>(jObjError.getInt("code"))
                }
            } catch (e:Exception) {
                Log.e("exception ", e.message + " " + e.getStatusCode())
                Resource.Error<ApiResponse<MaintenanceJob>>(e.getStatusCode())
            }
        }
    }

    suspend fun fetchUnavailableOwnerAsync(maintenanceId:Int): Deferred<Resource<ApiResponse<ArrayList<OwnerNotAvailableData>>?>> {
        return GlobalScope.async {
            try {
                val response: Response<ApiResponse<ArrayList<OwnerNotAvailableData>>> = webService.unavailableOwnerAsync(maintenanceId)
                val completedJobsResponse =response.body()
                if (response.code()== ApiResponseCode.SUCCESS_CODE) {
                    Resource.Success(completedJobsResponse)

                } else {
                    val jObjError = JSONObject(response.errorBody()?.string())
                    Resource.Error<ApiResponse<ArrayList<OwnerNotAvailableData>>>(jObjError.getInt("code"))
                }
            } catch (e:Exception) {
                Log.e("exception ", e.message + " " + e.getStatusCode())
                Resource.Error<ApiResponse<ArrayList<OwnerNotAvailableData>>>(e.getStatusCode())
            }
        }
    }

    fun uploadImageToServerAsync(imageData: MultipartBody.Part, tbl: MultipartBody.Part):
            Deferred<Resource<ImageUploadResponse?>> {
        return GlobalScope.async {
            try {
                val response: Response<ImageUploadResponse> = webService.uploadImageAsync(imageData, tbl)
                val imageUploadResponse = response.body()
                if (response.code()== ApiResponseCode.SUCCESS_CODE){
                    Resource.success(imageUploadResponse)
                }
                else {

                    val jObjError = JSONObject(response.errorBody()?.string())
                    Resource.Error<ImageUploadResponse>(jObjError.getInt("code"))
                }


            } catch (e: Exception) {
                Resource.Error<ImageUploadResponse>(e.getStatusCode())
            }
        }
    }


    suspend fun addNewLogsAsync(imageName:ArrayList<String>,comment:String,
                                maintenanceId:Int): Deferred<Resource<ResponseBody?>> {
        return GlobalScope.async {
            try {
                val response: Response<ResponseBody> =
                    webService.addNewLogsAsync(imageName,comment,maintenanceId)
                val completedJobsResponse =response.body()
                if (response.code()== ApiResponseCode.SUCCESS_CODE) {
                    Resource.Success(completedJobsResponse)

                } else {
                    val jObjError = JSONObject(response.errorBody()?.string())
                    Resource.Error<ResponseBody>(jObjError.getInt("code"))
                }
            } catch (e:Exception) {
                Log.e("exception ", e.message + " " + e.getStatusCode())
                Resource.Error<ResponseBody>(e.getStatusCode())
            }
        }
    }


    /*getSubTaskDetailAsync=> get the meta data of the task
    * The user token will pass in Header section
    * */
    suspend fun getJobDetailsAsync(maintenanceId: Int): Deferred<Resource<JobDetailResponse?>> {
        return GlobalScope.async {
            try {
                val response: Response<JobDetailResponse> = webService.jobDetailsAsync(maintenanceId)
                val openJobsResponse =response.body()
                if (response.code()== ApiResponseCode.SUCCESS_CODE) {
                    Resource.Success(openJobsResponse)
                }else {
                    val jObjError = JSONObject(response.errorBody()?.string())
                    Resource.Error<JobDetailResponse>(jObjError.getInt("code"))
                }
            } catch (e:Exception) {
                Log.e("exception ", e.message + " " + e.getStatusCode())
                Resource.Error<JobDetailResponse>(e.getStatusCode())
            }
        }
    }


    fun uploadJobMediaAsync(imageData: MultipartBody.Part, maintenance_job_id: MultipartBody.Part,type: MultipartBody.Part):
            Deferred<Resource<ImageUploadResponse?>> {
        return GlobalScope.async {
            try {
                val response: Response<ImageUploadResponse> = webService.uploadJobMediaAsync(imageData,
                    maintenance_job_id,type)
                val imageUploadResponse = response.body()
                if (response.code()== ApiResponseCode.SUCCESS_CODE){
                    Resource.success(imageUploadResponse)
                }
                else {

                    val jObjError = JSONObject(response.errorBody()?.string())
                    Resource.Error<ImageUploadResponse>(jObjError.getInt("code"))
                }


            } catch (e: Exception) {
                Resource.Error<ImageUploadResponse>(e.getStatusCode())
            }
        }
    }



}
