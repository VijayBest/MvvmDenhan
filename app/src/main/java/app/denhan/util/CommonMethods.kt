package app.denhan.util

import android.app.ProgressDialog
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText


object  CommonMethods {
    fun isEmailValid(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun changePasswordFiledFont(password: TextInputEditText){
        password.transformationMethod = PasswordTransformationMethod()
    }
    fun showSnackBar(view1: View, message:String){

        Snackbar.make(view1, message, Snackbar.LENGTH_LONG).show()
    }

    /*showProgressDialog>  show the progressDialog
    * */
    fun showProgressDialog(dialog: ProgressDialog, title:String, message:String)
    {
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setCancelable(false)
        dialog.show()
    }

    /*
    * hideProgressDialog=> Hide the progress Dialog
    * */
    fun hideProgressDialog(dialog: ProgressDialog)
    {
        dialog.dismiss()
    }
}