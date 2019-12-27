package app.denhan.view.subtask

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.denhan.android.R
import app.denhan.data.repos.ApiResponseCode
import app.denhan.data.repos.AuthRepository
import app.denhan.model.subtask.MaintenanceJobImage
import app.denhan.module.ResourceProvider
import app.denhan.util.AppConstants
import app.denhan.util.AppConstants.selectedJob
import app.denhan.util.AppConstants.selectedSubTaskData
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

            jobStatus.postValue(selectedSubTaskData.status)
            notifyAllAdapter.postValue("")



        }
    }

    private fun addFirstItemInAllArrayList() {

        val firstObject= MaintenanceJobImage("",0,"",0,"","")
        imagesBeforeCompletion.add(0,firstObject)
        imagesAfterCompletion.add(0,firstObject)
        billImages.add(0,firstObject)
    }

    fun uploadMultipleImage(it: MutableList<Uri>){
        GlobalScope.launch {
            uploadImage(it)
            notifyAllAdapter.postValue("")
        }
    }
   suspend fun uploadImage(it: MutableList<Uri>) {
        it.forEach {
            progressVisible.postValue(true)
                val file = File(it.path)
                val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                val imageData =
                    MultipartBody.Part.createFormData("image_file", file.name, requestBody)
                val maintenanceId = MultipartBody.Part.createFormData(
                    "maintenance_job_id",
                    selectedSubTaskData.id.toString()
                )
                val mediaType =
                    MultipartBody.Part.createFormData("type", AppConstants.imageTypeStatus)
                when (val resource =
                    userRepository.uploadJobMediaAsync(imageData, maintenanceId, mediaType)) {
                    is Resource.Success -> {
                        progressVisible.postValue(false)
                        val responseObject = resource.value?.data?.get(0)
                        responseObject?.let {
                            val addImageObject = MaintenanceJobImage(
                                "",
                                responseObject?.id,
                                responseObject?.image_path,
                                responseObject?.maintenance_job_id,
                                it.path,
                                it.type
                            )

                            when (it.type) {
                                ConstValue.beforeCompletionImages -> {
                                    imagesBeforeCompletion.add(addImageObject)
                                }
                                ConstValue.workCompletionImages -> {

                                    imagesAfterCompletion.add(addImageObject)
                                }
                                ConstValue.billImages -> {

                                    billImages.add(addImageObject)
                                }

                                else -> {

                                }
                            }
                        }

                    }
                    is Resource.Error -> {
                        progressVisible.postValue(false)

                    }

                }

        }
    }




   suspend fun deleteImage(deleteMediaObject: MaintenanceJobImage){

       when (val resource = userRepository.deleteMediaAsync(deleteMediaObject.id)) {
            is Resource.Success -> {
                progressVisible.postValue(false)
                when(AppConstants.imageTypeStatus){
                    ConstValue.beforeCompletionImages->{
                        imagesBeforeCompletion.remove(deleteMediaObject)
                    }
                    ConstValue.workCompletionImages->{
                        imagesAfterCompletion.remove(deleteMediaObject)
                    }
                    ConstValue.billImages->{
                        billImages.remove(deleteMediaObject)
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