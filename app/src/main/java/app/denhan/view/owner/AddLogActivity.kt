package app.denhan.view.owner

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import app.denhan.android.R
import app.denhan.android.databinding.ActivityAddLogBinding
import app.denhan.android.databinding.ActivityLoginBinding
import app.denhan.util.CommonMethods
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import gun0912.tedbottompicker.TedBottomPicker
import org.koin.android.viewmodel.ext.android.viewModel
import skycap.android.core.livedata.observeNonNull

class AddLogActivity : AppCompatActivity() {
    companion object {
        private val WRITE_EXTERNAL = 101
    }
    lateinit var binding:ActivityAddLogBinding
    private val viewModel: AddLogViewModel by viewModel()
    lateinit var dialog:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_log)
        binding.viewModel = viewModel
        binding.lifecycleOwner= this
        intiView()
    }

    private fun intiView() {
        dialog = ProgressDialog(this)
        observeLiveData()
        clickEvent()

    }

    private fun clickEvent() {

        binding.backImage.setOnClickListener {

            finish()
        }
    }

    /*Attachment=>  Here we are observing all the live data
    * */
    private fun observeLiveData() {
        observeNonNull(viewModel.addAttachment){
            setupPermissions(this)
        }
        observeNonNull(viewModel.showCommentError){

            CommonMethods.showSnackBar(binding.tickImage,it)
        }
        observeNonNull(viewModel.errorCommand){

            CommonMethods.showSnackBar(binding.topLayout, it)
        }

        observeNonNull(viewModel.successesCommand){
            finish()
        }

        observeNonNull(viewModel.imageName){
            Glide.with(this)
                .load(it.url)
                .apply(RequestOptions().placeholder(R.drawable.image_place_holder).error(R.drawable.image_place_holder))
                .into(binding.uploadedImage)
        }

        observeNonNull(viewModel.showProgress){
            val visible = it ?: false
            if (visible){
                CommonMethods.showProgressDialog(dialog,
                    this.resources.getString(R.string.progress_tittle_text),this.resources.getString(R.string.server_request_text))
            }
            else{
                if (dialog.isShowing){
                    CommonMethods.hideProgressDialog(dialog)
                }
            }
        }


    }

    private fun setupPermissions(context: Context) {
        val permission = ContextCompat.checkSelfPermission(context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {
              showImageSeletion()
        }


        }


    private fun showImageSeletion() {
        TedBottomPicker.with(this).showGallery=false
        TedBottomPicker.with(this)
            .showGalleryTile(false)
            .setPreviewMaxCount(0)
            .show {

                viewModel.uploadImage(it)
            }

    }

    private fun makeRequest() {
        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA),
            WRITE_EXTERNAL)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            WRITE_EXTERNAL -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setupPermissions(this)
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }
    }

}



