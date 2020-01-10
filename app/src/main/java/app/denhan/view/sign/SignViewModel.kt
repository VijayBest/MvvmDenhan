package app.denhan.view.sign

import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.denhan.android.R
import app.denhan.data.repos.AuthRepository
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
    val errorCommand = SingleEventLiveData<String>()

    fun addSignature(file: File, signStatus: Boolean){
        val hourValue = hourText.value?:"0"
        val minute = minuteText.value?:"0"
        if (hourValue.toInt()>99){
            errorCommand.postValue(resourceProvider.getStringResource(R.string.hours_error))
        }
        else if (hourValue=="0" || minute=="0"){
            errorCommand.postValue(resourceProvider.getStringResource(R.string.elpase_time_error))
        }
        else if (minute.toInt()>59){

            errorCommand.postValue(resourceProvider.getStringResource(R.string.minute_error))

        }
        else if (!signStatus){
            errorCommand.postValue(resourceProvider.getStringResource(R.string.signature_error))
        }

        else {

            GlobalScope.launch {
                saveSignatureOnServer(file)
            }
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

    fun deleteDirectoryAfterUploadThePics(){
        val temFolderPath = File(Environment.getExternalStorageDirectory().path, ConstValue.deleteTempFolder)
        val someDir = File(temFolderPath.toString())
        someDir.deleteRecursively()

    }
}
