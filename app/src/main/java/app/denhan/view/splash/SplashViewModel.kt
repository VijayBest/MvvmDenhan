package app.denhan.view.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import app.denhan.android.R
import app.denhan.data.repos.ApiResponseCode
import app.denhan.data.repos.AuthRepository
import app.denhan.module.ResourceProvider
import app.denhan.util.AppConstants.notificationToken
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import skycap.android.core.resource.Resource

class SplashViewModel(private val userRepository: AuthRepository,
                      private val resourceProvider: ResourceProvider) :ViewModel(){

    val TAG="Splash=>"

    fun saveDeviceApi(){
        notificationToken= FirebaseInstanceId.getInstance().token?:""
        GlobalScope.launch {
            hitSaveDeviceApi()
        }
    }

    private suspend fun hitSaveDeviceApi(){
        notificationToken= FirebaseInstanceId.getInstance().token?:""
        Log.e(TAG, notificationToken)
        when (val resource = userRepository.addDevice()) {

        }
    }
}