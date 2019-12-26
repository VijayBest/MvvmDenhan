package app.denhan.view.subtask

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.denhan.android.R
import app.denhan.data.repos.ApiResponseCode
import app.denhan.data.repos.AuthRepository
import app.denhan.model.jobs.Maintenance
import app.denhan.model.jobs.MaintenanceJob
import app.denhan.model.subtask.MaintenanceJobImage
import app.denhan.module.ResourceProvider
import app.denhan.util.AppConstants
import app.denhan.util.AppConstants.selectedJob
import app.denhan.util.AppConstants.selectedSubTaskData
import app.denhan.util.ArrayConstant
import app.denhan.util.ConstValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import skycap.android.core.livedata.SingleEventLiveData
import skycap.android.core.resource.Resource
import java.io.File

class SubTaskViewModel(private val userRepository: AuthRepository,
                       private val resourceProvider: ResourceProvider):ViewModel(){
    val progressVisible = MutableLiveData<Boolean>()
    val errorCommand = MutableLiveData<String>()
    val taskDescription=MutableLiveData<String>()
    val comment=MutableLiveData<String>()
    val labourCost = MutableLiveData<String>()
    val materialCost = MutableLiveData<String>()
    val jobStatus=MutableLiveData<String>()

    var imagesBeforeCompletion= ArrayList<MaintenanceJobImage>()
    var imagesAfterCompletion= ArrayList<MaintenanceJobImage>()
    var billImages= ArrayList<MaintenanceJobImage>()
    val notifyAllAdapter= SingleEventLiveData<String>()


        init {
            imagesAfterCompletion= ArrayList()
            imagesBeforeCompletion= ArrayList()
            billImages= ArrayList()
            addFirstItemInAllArrayList()
            setSubTaskDetail()
        }



    private fun setSubTaskDetail(){
        selectedSubTaskData.job_detail.let {
            taskDescription
                .postValue(it)
        }
        selectedSubTaskData.comments?.let {

            comment.postValue(it)
        }
        selectedSubTaskData.labour_charges?.let {

            labourCost.postValue(it.toString())
        }

        selectedSubTaskData.amount?.let {
            materialCost.postValue(it.toString())
        }

        selectedSubTaskData.maintenance_job_images?.let {
            it.forEach {
                when(it.type){
                    ConstValue.beforeCompletionImages.toString()-> {
                        imagesBeforeCompletion.add(it)
                    }
                    ConstValue.workCompletionImages.toString()->{

                        imagesAfterCompletion.add(it)
                    }
                    ConstValue.billImages.toString()->{

                        billImages.add(it)
                    }
                }
            }

            jobStatus.postValue(selectedJob.status)
            notifyAllAdapter.postValue("")



        }
    }

    private fun addFirstItemInAllArrayList() {

        val firstObject= MaintenanceJobImage("",0,"",0,"","")
        imagesBeforeCompletion.add(0,firstObject)
        imagesAfterCompletion.add(0,firstObject)
        billImages.add(0,firstObject)
    }

    fun uploadImage(it: Uri) {
        progressVisible.postValue(true)
        GlobalScope.launch {
            val file = File(it.path)
            val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val imageData = MultipartBody.Part.createFormData("image_file", file.name, requestBody)
            val maintenanceId = MultipartBody.Part.createFormData("maintenance_job_id", selectedJob.id.toString())
            val mediaType = MultipartBody.Part.createFormData("type", AppConstants.imageTypeStatus)
            when (val resource = userRepository.uploadJobMediaAsync(imageData, maintenanceId,mediaType).await()) {
                is Resource.Success -> {
                    progressVisible.postValue(false)


                }
                is Resource.Error -> {
                    progressVisible.postValue(false)

                }

            }
        }
    }



}