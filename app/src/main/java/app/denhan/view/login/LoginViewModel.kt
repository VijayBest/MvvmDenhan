package app.denhan.view.login

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.denhan.android.R
import app.denhan.data.repos.ApiResponseCode
import app.denhan.data.repos.AuthRepository
import app.denhan.module.ResourceProvider
import app.denhan.util.AppConstants
import app.denhan.util.CommonMethods
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import skycap.android.core.livedata.SingleEventLiveData
import skycap.android.core.resource.Resource
import kotlin.coroutines.coroutineContext

class LoginViewModel(
    private val userRepository: AuthRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    val emailString = MutableLiveData<String>()
    val passwordString = MutableLiveData<String>()

    val emailErrorString = MediatorLiveData<String>()
    val passwordErrorString = MediatorLiveData<String>()
    val progressVisible = MutableLiveData<Boolean>()
    val emailValid = MutableLiveData<Boolean>()
    val passwordValid = MutableLiveData<Boolean>()
    val isShowError = MutableLiveData<Boolean>()


    val loginErrorCommand = SingleEventLiveData<String>()
    val loginSuccessCommand = SingleEventLiveData<String>()

    init {
        emailValid.postValue(false)
        passwordValid.postValue(false)
        emailErrorString.addSource(emailString) { validateEmail() }
        passwordErrorString.addSource(passwordString) { validateCode() }

    }

    private fun validateEmail() {
        val email = emailString.value ?: ""
        when {
            email.isEmpty() -> {
                emailValid.postValue(false)
                emailErrorString.postValue(resourceProvider.getStringResource(R.string.invalid_email_error))
            }
            CommonMethods.isEmailValid(email).not() -> {
                emailValid.postValue(false)
                emailErrorString.postValue(resourceProvider.getStringResource(R.string.invalid_email_error))

            }
            else -> {
                emailErrorString.postValue("")
                emailValid.postValue(true)

            }
        }
    }

    /* validate the  password here
    * */
    private fun validateCode() {
        val code = passwordString.value?.trim()
        when {
            code.isNullOrEmpty() -> {
                passwordValid.postValue(false)
                passwordErrorString.postValue(resourceProvider.getStringResource(R.string.password_error))
            }
            code.length < 7 -> {
                passwordValid.postValue(false)
                passwordErrorString.postValue(resourceProvider.getStringResource(R.string.password_error))
            }
            else -> {
                passwordValid.postValue(true)
                passwordErrorString.postValue("")
            }
        }
    }

    private fun isLoginData(): Boolean {
        return emailValid.value ?: false && passwordValid.value ?: false
    }

    fun loginWithUserData() {
        isShowError.postValue(true)
        if (isLoginData()) {
            isShowError.postValue(false)
            hitLoginService(emailString.value ?: "", passwordString.value ?: "")
        } else {
            if (emailValid.value == false) {
                emailErrorString.postValue(resourceProvider.getStringResource(R.string.invalid_email_error))
            }
            if (passwordValid.value == false) {
                passwordErrorString.postValue(resourceProvider.getStringResource(R.string.password_error))
            }
        }

    }

    private fun hitLoginService(emailId: String, password: String) {
        progressVisible.postValue(true)
        AppConstants.notificationToken = FirebaseInstanceId.getInstance().token?:""
        GlobalScope.launch {
            when (val resource = userRepository.userLoginAsync(emailId, password).await()) {
                is Resource.Success -> {
                    progressVisible.postValue(false)
                    loginSuccessCommand.postValue("")
                }
                is Resource.Error -> {
                    progressVisible.postValue(false)
                    when (resource.code) {
                        ApiResponseCode.NETWORK_NOT_AVAILABLE -> {
                            loginErrorCommand.postValue(resourceProvider.getStringResource(R.string.network_unavailable))
                        }
                        ApiResponseCode.UN_AUTHORIZE -> {
                            loginErrorCommand.postValue(resourceProvider.getStringResource(R.string.unauthorize_error))
                        }
                        else -> {
                            loginErrorCommand.postValue(resourceProvider.getStringResource(R.string.common_api_error))
                        }
                    }
                }
            }
        }
    }
}







