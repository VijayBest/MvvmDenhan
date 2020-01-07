package app.denhan.util

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import app.denhan.android.R
import app.denhan.android.databinding.AddTaskDialogBinding
import app.denhan.android.databinding.CommonDialogBinding
import app.denhan.view.subtask.StartTaskCallBack
import app.denhan.view.taskdetail.DialogCallBack
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*


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

    fun showToast(context: Context, message:String){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

    /*showAddTaskDialog=> it will show the Add task Ui
    *  and Call back method and it return the filled value
    * */
    fun showAddTaskDialog(context: Context, callBack: DialogCallBack){
        val dialog = Dialog(context)
        val back  = ColorDrawable(Color.TRANSPARENT)
        val  inset = InsetDrawable(back,20)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(inset)
        dialog.window?.setGravity(Gravity.CENTER)
        val binding: AddTaskDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(dialog.context),
            R.layout.add_task_dialog, null, false)
        dialog.setContentView(binding.root)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window?.attributes = lp
        binding.cancelButton.setOnClickListener {

            dialog.cancel()

        }

        binding.submitButton.setOnClickListener {
            val taskTitle= binding.edtInoutTitle.text.toString()
            val description = binding.edtDescription.text.toString()?:""
            if (taskTitle.isEmpty()){
                showToast(context,context.resources.getString(R.string.job_title_require))
            }
            else{
                callBack.filledValue(taskTitle,description)
                dialog.cancel()
            }

        }
        dialog.show()
    }

    fun defaultDialog(context: Context, title:String, message:String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(context.resources.getString(R.string.okay_text)){dialogInterface, which ->

        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()


    }

        fun  showVisibility(view:View){

            view.visibility = View.VISIBLE
        }

    fun  hideVisibility(view:View){

        view.visibility = View.GONE
    }

     fun disableAll(layout: ViewGroup) {
        for (i in 0 until layout.childCount) {
            val child = layout.getChildAt(i)
            child.isEnabled = false
            if (child is ViewGroup) disableAll(child)
        }
    }

    fun enableAll(layout: ViewGroup) {
        for (i in 0 until layout.childCount) {
            val child = layout.getChildAt(i)
            child.isEnabled = true
            child.isClickable = true
            if (child is ViewGroup) enableAll(child)
        }
    }

    fun startTaskDialog(context: Context, title:String, message:String, startTaskCallBack: StartTaskCallBack){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(context.resources.getString(R.string.okay_text)){dialogInterface, which ->

            startTaskCallBack.startTask()
        }
         //performing cancel action
       builder.setNeutralButton(context.resources.getString(R.string.cancel_text)){dialogInterface , which ->

           dialogInterface.dismiss()

       }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()


    }

    fun customCommonDialog(context: Context, title: String,
                           description:String, leftButtonText:String, rightButtonText:String,
                           customDialogCallBack: CustomDialogCallBack){

        val dialog = Dialog(context)
        val back  = ColorDrawable(Color.TRANSPARENT)
        val  inset = InsetDrawable(back,20)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(inset)
        dialog.window?.setGravity(Gravity.CENTER)
        val binding: CommonDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(dialog.context),
            R.layout.common_dialog, null, false)
        dialog.setContentView(binding.root)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window?.attributes = lp
        binding.dialogTittle.text = title
        binding.leftButton.text= leftButtonText
        binding.rightButton.text = rightButtonText
        binding.textDescription.text = description
        binding.leftButton.setOnClickListener {
            dialog.dismiss()
        }
        binding.rightButton.setOnClickListener {
            customDialogCallBack.positiveButtonClick()

        }
        dialog.show()

    }

    fun currentDateWithString(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = dateFormat.format(calendar.time)
        val currentDate = dateFormat.format(calendar.time)
        Log.e("Date ", currentDate)
        return currentDate.toString()
    }

    fun returnDateTime(dateTime:String): String{
        val dateFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        val date = dateFormat.parse(dateTime)

        val dateFormat1 = SimpleDateFormat("yyyy-MM-dd HH:mm")
        Log.e("date ", dateFormat1.format(date))
        return dateFormat1.format(date)
    }
}