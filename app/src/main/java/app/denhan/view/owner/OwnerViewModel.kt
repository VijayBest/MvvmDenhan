package app.denhan.view.owner

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.denhan.android.R
import app.denhan.data.repos.ApiResponseCode
import app.denhan.data.repos.AuthRepository
import app.denhan.model.owner.OwnerNotAvailableData
import app.denhan.module.ResourceProvider
import app.denhan.util.AppConstants
import app.denhan.util.AppConstants.selectedJob
import app.denhan.util.ArrayConstant
import app.denhan.util.ArrayConstant.attachmentArrayList
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import skycap.android.core.livedata.SingleEventLiveData
import skycap.android.core.resource.Resource

class OwnerViewModel(private val userRepository: AuthRepository,
                     private val resourceProvider: ResourceProvider):ViewModel(){

    val progressVisible = MutableLiveData<Boolean>()
    val errorCommand=MutableLiveData<String>()
    val successesCommand= MutableLiveData<String>()
    lateinit var ownerArrayList: ArrayList<OwnerNotAvailableData>
    val imageSlideData = SingleEventLiveData<ArrayList<String>>()
    val visibleNoLogText=MutableLiveData<Boolean>()

    val TAG="OwnerScreen"

    fun getOwnerList(){
       visibleNoLogText.postValue(false)
       progressVisible.postValue(true)
        GlobalScope.launch {
            when (val resource = userRepository.fetchUnavailableOwnerAsync(selectedJob.id).await()) {
                is Resource.Success -> {
                    ownerArrayList= ArrayList()
                    ownerArrayList = resource.value?.data as ArrayList<OwnerNotAvailableData>
                  /*  if (ownerArrayList.size>0){
                        visibleNoLogText.postValue(false)
                    }
                    else{
                        visibleNoLogText.postValue(true)
                    }*/
                    progressVisible.postValue(false)
                    successesCommand.postValue("")

               //     checkJOnType()

                }
                is Resource.Error -> {
                    visibleNoLogText.postValue(true)
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

    fun fetchAttachmentList(clickedData: OwnerNotAvailableData) {

        attachmentArrayList = ArrayList()
        clickedData.mainte_unavail_images?.forEach {
            attachmentArrayList.add(it.image_path)
        }

        imageSlideData.postValue(attachmentArrayList)


    }


}