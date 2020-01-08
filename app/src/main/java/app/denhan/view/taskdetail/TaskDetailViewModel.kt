package app.denhan.view.taskdetail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.denhan.android.R
import app.denhan.data.repos.ApiResponseCode
import app.denhan.data.repos.AuthRepository
import app.denhan.model.jobs.Maintenance
import app.denhan.model.jobs.MaintenanceInstruction
import app.denhan.model.jobs.MaintenanceJob
import app.denhan.module.ResourceProvider
import app.denhan.util.AppConstants
import app.denhan.util.AppConstants.selectedJob
import app.denhan.util.ArrayConstant
import app.denhan.util.ArrayConstant.inProgressJobsArray
import app.denhan.util.ArrayConstant.openJobsArray
import app.denhan.util.ConstValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import skycap.android.core.livedata.SingleEventLiveData
import skycap.android.core.resource.Resource

class TaskDetailViewModel(private val userRepository: AuthRepository,
                          private val resourceProvider: ResourceProvider) :ViewModel(){

    var jobTitle=MutableLiveData<String>()
    var dateTime = MutableLiveData<String>()
    var address= MutableLiveData<String>()
    var callDetail = MutableLiveData<String>()
    var detailNotes= MutableLiveData<String>()
    var taskArrayList = MutableLiveData<ArrayList<MaintenanceJob>>()
    var instructionArray = MutableLiveData<ArrayList<MaintenanceInstruction>>()
    val loadAllData= SingleEventLiveData<String>()
    var detailNoteVisible=MutableLiveData<Boolean>()
    var progressVisible = MutableLiveData<Boolean>()
    var successCommand = MutableLiveData<String>()
    var errorCommand= SingleEventLiveData<String>()
    val jobCompleted = MutableLiveData<Boolean>()
    val addedTask = SingleEventLiveData<String>()
    val buttonVisibilityStatus=MutableLiveData<Boolean>()


    fun callTaskDetail(jobId:Int){
        GlobalScope.launch {
            getTaskDetail(jobId)

        }
    }
   private suspend  fun getTaskDetail(jobId: Int){
        progressVisible.postValue(true)
        GlobalScope.launch {
            when (val resource = userRepository.getJobDetailsAsync(jobId).await()) {
                is Resource.Success -> {
                    progressVisible.postValue(false)

                    selectedJob=resource.value?.maintenance!!
                    setSelectedData()
                    Log.e("ss ", resource.value.toString())
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

    private  fun setSelectedData(){


        if (selectedJob.status==ConstValue.completeJobSelected){
            jobCompleted.postValue(true)
        }
        taskArrayList.postValue(selectedJob.maintenance_jobs as ArrayList<MaintenanceJob>)
        Log.e("markstatus ", ""+taskArrayList.value?.size)

        if(selectedJob.property.active_tenancy!=null) {
            jobTitle.postValue(
                selectedJob.property.ref_code + "\n" +
                        selectedJob.property.active_tenancy?.tenant?.full_name
            )
        }
        else{
            jobTitle.postValue(selectedJob.property.ref_code + "\n" +
                    selectedJob.reporter_name
            )
        }
        dateTime.postValue(selectedJob.f_created_date+", "+ selectedJob.f_created_time)
        address.postValue(selectedJob.property.address)
        selectedJob.property.active_tenancy?.let {
            callDetail.postValue(selectedJob.property.active_tenancy?.tenant?.mobile)
        }

        selectedJob.detail_note.let {
            detailNotes.postValue("#"+selectedJob.detail_note)
            detailNoteVisible.postValue(true)
        }



        selectedJob.maintenance_instructions?.let {
            instructionArray.postValue(selectedJob.maintenance_instructions as ArrayList<MaintenanceInstruction>)
        }
        loadAllData.postValue("")


    }

    /*addTask=> Here we are adding the subTask  in specific main task
    *  Take the input from the user like as title and
    * description of the job
    * Here description is optional value
    * */
    fun addTask(title:String,description:String){
        progressVisible.postValue(true)
                    GlobalScope.launch {
                        when (val resource = userRepository.addTaskAsync(title,selectedJob.id).await()) {
                            is Resource.Success -> {

                                checkJOnType()

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

    private fun checkJOnType() {

        when(AppConstants.selectedJobType){
            ConstValue.openJobSelected->{
                getOpenJobs()
            }
            ConstValue.inProgressJobSelected->{
                getInProgressJobs()

            }
        }
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
                    progressVisible.postValue(false)
                    openJobsArray=resource.value?.maintenances as ArrayList<Maintenance>
                    openJobsArray.forEach {

                        if (it.id== selectedJob.id){
                            selectedJob = it

                        }
                    }

                    taskArrayList.postValue(selectedJob.maintenance_jobs as ArrayList<MaintenanceJob>)
                    addedTask.postValue("")
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
                    progressVisible.postValue(false)
                    inProgressJobsArray=resource.value?.maintenances as ArrayList<Maintenance>
                    inProgressJobsArray.forEach {
                        if (it.id== selectedJob.id){
                            selectedJob = it
                        }
                    }

                    taskArrayList.postValue(selectedJob.maintenance_jobs as ArrayList<MaintenanceJob>)
                    addedTask.postValue("")
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

                    ArrayConstant.completeJobsArrayList=resource.value?.maintenances as ArrayList<Maintenance>
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

    }


