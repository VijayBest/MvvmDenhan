package app.denhan.util

import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText

object  CommonMethods {
    fun isEmailValid(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun changePasswordFiledFont(password: TextInputEditText){
        password.transformationMethod = PasswordTransformationMethod()
    }
}