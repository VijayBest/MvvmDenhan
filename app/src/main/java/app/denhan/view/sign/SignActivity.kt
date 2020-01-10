package app.denhan.view.sign

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import app.denhan.android.R
import app.denhan.android.databinding.ActivitySignBinding
import app.denhan.util.AppConstants
import app.denhan.util.CommonMethods
import app.denhan.util.ConstValue
import com.github.gcacace.signaturepad.views.SignaturePad
import org.koin.android.viewmodel.ext.android.viewModel
import skycap.android.core.livedata.observeNonNull
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class SignActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignBinding
    private val viewModel:SignViewModel by  viewModel()
    lateinit var dialog:ProgressDialog
    var signStatus= false
    companion object {
        private val WRITE_EXTERNAL = 101
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        intiView()

    }

    private fun intiView() {
        dialog = ProgressDialog(this)
        clickEvent()
        observerLiveData()
        setupPermissions(this)
    }

    private fun observerLiveData() {

        observeNonNull(viewModel.progressVisible){
            val visible = it ?: false
            if (visible){

                if (dialog.isShowing){CommonMethods.showProgressDialog(dialog,
                    this.resources.getString(R.string.progress_tittle_text),this.resources.getString(R.string.server_request_text))
                }
                else{
                    CommonMethods.hideProgressDialog(dialog)
                }
            }
        }

        observeNonNull(viewModel.successesCommand){
            viewModel.deleteDirectoryAfterUploadThePics()
            AppConstants.fromTaskDetailScreen = ConstValue.signScreen
            finish()
        }

        observeNonNull(viewModel.errorCommand){

            CommonMethods.showSnackBar(binding.mainLayout, it)
        }
    }

    private fun clickEvent() {
        binding.signaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                binding.retakeButton.visibility= View.VISIBLE
                signStatus = true
             //   Toast.makeText(this@SignActivity, "OnStartSigning", Toast.LENGTH_SHORT).show()
            }
            override fun onSigned() {

            }
            override fun onClear() {

            }
        })

        binding.retakeButton.setOnClickListener {

            binding.retakeButton.visibility= View.GONE
            binding.signaturePad.clear()
            signStatus= false
        }
        binding.backImage.setOnClickListener {
            finish()
        }

        binding.done.setOnClickListener {

            val signatureBitmap: Bitmap = binding.signaturePad.signatureBitmap
            if (addJpgSignatureToGallery(signatureBitmap)) {
                val path = File(
                    Environment.getExternalStorageDirectory(),
                    ConstValue.temFolder+"/"+AppConstants.userDetailData?.id+".jpg"
                )
                viewModel.addSignature(path,signStatus)
            }
        }
    }

    private fun setupPermissions(context: Context) {
        val permission = ContextCompat.checkSelfPermission(context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        } else {

        }
    }


    private fun makeRequest(){
        requestPermissions(arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA),
            WRITE_EXTERNAL
        )
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

    private fun addJpgSignatureToGallery(signature: Bitmap?): Boolean {
        var result = false
        try {
            val photo = File(getAlbumStorageDir(),
                String.format(AppConstants.userDetailData.id.toString()+".jpg")
            )
            saveBitmapToJPG(signature!!, photo)

            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    private fun getAlbumStorageDir(): File? { // Get the directory for the user's public pictures directory.

        val file = File(Environment.getExternalStorageDirectory().path, ConstValue.temFolder)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    @Throws(IOException::class)
    fun saveBitmapToJPG(bitmap: Bitmap, photo: File?) {
        val newBitmap =
            Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        val stream: OutputStream = FileOutputStream(photo)
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        stream.close()
    }
}
