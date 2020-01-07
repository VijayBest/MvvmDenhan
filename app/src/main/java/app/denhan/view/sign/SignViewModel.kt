package app.denhan.view.sign

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.denhan.data.repos.AuthRepository
import app.denhan.model.subtask.MaintenanceJobImage
import app.denhan.module.ResourceProvider
import app.denhan.util.AppConstants
import app.denhan.util.ConstValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import skycap.android.core.livedata.SingleEventLiveData
import skycap.android.core.resource.Resource
import java.io.File

class SignViewModel(private val userRepository: AuthRepository,
                    private val resourceProvider: ResourceProvider) : ViewModel(){
    val hourText = MutableLiveData<String>()
    val minuteText= MutableLiveData<String>()
    val totalTime = MutableLiveData<String>()
    val progressVisible = MutableLiveData<Boolean>()
    val successesCommand = SingleEventLiveData<String>()


    fun addSignature(file: File){
        GlobalScope.launch {
            saveSignatureOnServer(file)
        }
    }

    private suspend fun saveSignatureOnServer(file: File) {
        val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val imageData = MultipartBody.Part.createFormData("sign_file", file.name, requestBody)
        totalTime.postValue(hourText.value?:"0"+":"+ minuteText.value?:"0")
        val totalTime = MultipartBody.Part.createFormData(
            "total_time",totalTime.value?:"")
        val id = MultipartBody.Part.createFormData(
            "id",AppConstants.selectedJob.id.toString())
        when (val resource =
            userRepository.uploadSignature(imageData, totalTime, id, AppConstants.selectedJob.id)) {
            is Resource.Success -> {
                progressVisible.postValue(false)
                successesCommand.postValue("")
            }
            is Resource.Error -> {
                progressVisible.postValue(false)

            }

        }
    }


}
