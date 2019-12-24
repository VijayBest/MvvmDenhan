package app.denhan.view.owner

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.denhan.android.R
import app.denhan.data.repos.ApiResponseCode
import app.denhan.data.repos.AuthRepository
import app.denhan.module.ResourceProvider
import app.denhan.util.AppConstants
import app.denhan.util.AppConstants.selectedJob
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import skycap.android.core.livedata.SingleEventLiveData
import skycap.android.core.resource.Resource
import java.io.File

class AddLogViewModel(
    private val userRepository: AuthRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    val showProgress = MutableLiveData<Boolean>()
    val commentValue = MutableLiveData<String>()
    var imageArrayList: ArrayList<String>
    var errorCommand = SingleEventLiveData<String>()

    val addAttachment = SingleEventLiveData<String>()
    val imageName = MutableLiveData<app.denhan.model.uploaad.File>()
    val showCommentError = SingleEventLiveData<String>()
    val successesCommand = SingleEventLiveData<String>()

    init {

        imageArrayList = ArrayList()
    }


    fun addAttachment() {

        addAttachment.postValue("")
    }

    fun uploadImage(it: Uri) {
        showProgress.postValue(true)
        GlobalScope.launch {
            val file = File(it.path)
            val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val imageData = MultipartBody.Part.createFormData("files", file.name, requestBody)
            val tbl = MultipartBody.Part.createFormData("tbl", "maintenance")
            when (val resource = userRepository.uploadImageToServerAsync
                (imageData, tbl).await()) {
                is Resource.Success -> {
                    showProgress.postValue(false)
                    resource.value?.files?.get(0)?.let {
                        imageName.postValue(it)
                    }
                }
                is Resource.Error -> {
                    showProgress.postValue(false)

                }

            }
        }
    }

    fun addNewLogDataValidate() {

        if (commentValue.value?.isNotEmpty() == true && imageName.value?.name?.isNotEmpty() == true) {
            addNewLogs(imageName.value?.name ?: "")
        } else if (commentValue.value?.isEmpty() == true) {
            showCommentError.postValue(resourceProvider.getStringResource(R.string.add_comment_error))
        } else {

            showCommentError.postValue(resourceProvider.getStringResource(R.string.select_image_error))
        }
    }

    private fun addNewLogs(uploadImageName: String) {
        imageArrayList.add(uploadImageName)
        showProgress.postValue(true)
        GlobalScope.launch {
            when (val resource = userRepository.addNewLogsAsync(
                imageArrayList,
                commentValue.value ?: "",
                selectedJob.id
            ).await()) {
                is Resource.Success -> {
                    showProgress.postValue(false)
                    successesCommand.postValue("")
                }
                is Resource.Error -> {
                    showProgress.postValue(false)
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