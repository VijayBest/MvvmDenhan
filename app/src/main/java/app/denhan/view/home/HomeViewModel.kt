package app.denhan.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.denhan.android.R
import app.denhan.data.repos.ApiResponseCode
import app.denhan.data.repos.AuthRepository
import app.denhan.model.jobs.Maintenance
import app.denhan.model.jobs.MaintenanceInstruction
import app.denhan.module.ResourceProvider
import app.denhan.util.AppConstants
import app.denhan.util.ConstValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import skycap.android.core.livedata.SingleEventLiveData
import skycap.android.core.resource.Resource

class HomeViewModel(private val userRepository: AuthRepository,
                    private val resourceProvider: ResourceProvider) : ViewModel(){

    val progressVisible = MutableLiveData<Boolean>()
    private val successCommand = MutableLiveData<String>()
    val errorCommand = MutableLiveData<String>()
    var openJobsArray = MutableLiveData<ArrayList<Maintenance>>()
    var inProgressJobsArray= MutableLiveData<ArrayList<Maintenance>>()
    var completedJobsArray= MutableLiveData<ArrayList<Maintenance>>()
    val logoUserCommand= SingleEventLiveData<String>()


    init {
        AppConstants.jobInstructionArray= ArrayList()
        getOpenJobs()
        getInProgressJobs()
        getCompletedJobs()
    }


    /*
    * getOpenJobs=>  here we are fetching the open jobs of user
    * and show on the Open jobs section , if the there is no jobs contains
    * in this section then it will show the message to the user "No jobs are assigned.."
    * */
    private fun getOpenJobs() {
        progressVisible.postValue(true)
        GlobalScope.launch {
            when (val resource = userRepository.getOpenJobsAsync().await()) {
                is Resource.Success -> {
                    openJobsArray.postValue(resource.value?.maintenances as ArrayList<Maintenance>?)
                    if(!AppConstants.notificationObject.isNullOrEmpty()) {
                        val jsonObject = JSONObject(AppConstants.notificationObject)
                        var jobId= jsonObject.getString("id").toInt()
                        resource.value?.maintenances?.forEach {
                            if(it.id==jobId){
                                AppConstants.selectedJob= it
                                AppConstants.jobInstructionArray= ArrayList()
                                AppConstants.jobInstructionArray= it.maintenance_instructions as ArrayList<MaintenanceInstruction>
                                AppConstants.selectedJobType = ConstValue.openJobSelected

                            }
                        }
                    }
                }
                is Resource.Error -> {
                    progressVisible.postValue(false)
                    when (resource.code) {
                        ApiResponseCode.NETWORK_NOT_AVAILABLE -> {
                            errorCommand.postValue(resourceProvider.getStringResource(R.string.network_unavailable))
                        }
                        ApiResponseCode.UN_AUTHORIZE -> {
                            errorCommand.postValue(resourceProvider.getStringResource(R.string.unauthorize_error))
                        }
                        else -> {
                            errorCommand.postValue(resourceProvider.getStringResource(R.string.common_api_error))
                        }
                    }
                }
            }
        }
    }



    /*getInProgressJobs=> Here we are fetching the In Progress Jobs of the user
    *and show in InProgress section
    * */
    private fun getInProgressJobs(){
        GlobalScope.launch {
            when (val resource = userRepository.getInProgressJobsAsync().await()) {
                is Resource.Success -> {
                    inProgressJobsArray.postValue(resource.value?.maintenances as ArrayList<Maintenance>?)
                }
                is Resource.Error -> {
                    progressVisible.postValue(false)


                    when (resource.code) {
                        ApiResponseCode.NETWORK_NOT_AVAILABLE -> {
                            errorCommand.postValue(resourceProvider.getStringResource(R.string.network_unavailable))
                        }
                        ApiResponseCode.UN_AUTHORIZE -> {
                            errorCommand.postValue(resourceProvider.getStringResource(R.string.unauthorize_error))
                        }
                        else -> {
                            errorCommand.postValue(resourceProvider.getStringResource(R.string.common_api_error))
                        }
                    }
                }
            }
        }
    }

    /*getCompletedJobs=> Here we are fetching the completed jobs of the user
    * and it will show under the Completed Tab
    * */
    fun getCompletedJobs(){
        GlobalScope.launch {
            when (val resource = userRepository.getCompletedJobsAsync().await()) {
                is Resource.Success -> {
                    progressVisible.postValue(false)
                    completedJobsArray.postValue(resource.value?.maintenances as ArrayList<Maintenance>?)
                }
                is Resource.Error -> {
                    progressVisible.postValue(false)
                    when (resource.code) {
                        ApiResponseCode.NETWORK_NOT_AVAILABLE -> {
                            errorCommand.postValue(resourceProvider.getStringResource(R.string.network_unavailable))
                        }
                        ApiResponseCode.UN_AUTHORIZE -> {
                            errorCommand.postValue(resourceProvider.getStringResource(R.string.unauthorize_error))
                        }
                        else -> {
                            errorCommand.postValue(resourceProvider.getStringResource(R.string.common_api_error))
                        }
                    }
                }
            }
        }
    }

    /*refreshButtonClick=> Click event on Button
    * */
    fun refreshButtonClick(){
        getOpenJobs()
        getInProgressJobs()
        getCompletedJobs()

    }

    fun logoutUser() {

        GlobalScope.launch {
            hitLogoutUser()
        }

    }

    private suspend fun hitLogoutUser(){
        when (val resource = userRepository.logOutUserAsync()) {
            is Resource.Success -> {
                progressVisible.postValue(false)
                logoUserCommand.postValue("")
            }

            is Resource.Error -> {
                progressVisible.postValue(false)
                when (resource.code) {
                    ApiResponseCode.NETWORK_NOT_AVAILABLE -> {
                        errorCommand.postValue(resourceProvider.getStringResource(R.string.network_unavailable))
                    }
                    ApiResponseCode.UN_AUTHORIZE -> {
                        errorCommand.postValue(resourceProvider.getStringResource(R.string.unauthorize_error))
                    }
                    else -> {
                        errorCommand.postValue(resourceProvider.getStringResource(R.string.common_api_error))
                    }
                }
            }
        }
    }

}