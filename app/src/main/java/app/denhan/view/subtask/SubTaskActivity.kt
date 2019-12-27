package app.denhan.view.subtask

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import app.denhan.adapter.AttachmentAdapter
import app.denhan.android.R
import app.denhan.android.databinding.ActivitySubTaskBinding
import app.denhan.model.subtask.MaintenanceJobImage
import app.denhan.util.AppConstants
import app.denhan.util.CommonMethods
import app.denhan.util.ConstValue
import app.denhan.util.ImageCompress
import gun0912.tedbottompicker.TedBottomPicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.android.viewmodel.ext.android.viewModel
import skycap.android.core.livedata.observeNonNull


class SubTaskActivity : AppCompatActivity(),AttachmentAdapter.AttachmentAdapterListener {
    lateinit var binding:ActivitySubTaskBinding
    private val viewModel:SubTaskViewModel by viewModel()
    lateinit var attachmentAdapterBefore: AttachmentAdapter
    lateinit var attachmentAdapterAfter: AttachmentAdapter
    lateinit var attachmentAdapterBill: AttachmentAdapter
    lateinit var compressUriList: ArrayList<Uri>
    companion object {
        private val WRITE_EXTERNAL = 101
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub_task)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        intiView()
    }

    private fun intiView() {
        setAdapters()
        bindObserve()
        clickEvent()
    }

    private fun clickEvent() {

        binding.backImage.setOnClickListener {

            finish()
        }
        binding.mainLayout.setOnClickListener {

            if (viewModel.jobStatus.value==ConstValue.notStarted) {
                CommonMethods.defaultDialog(
                    this, this.resources.getString(R.string.alert_text),
                    this.resources.getString(R.string.task_not_start_error)
                )
            }
        }
    }

    private fun bindObserve() {
        observeNonNull(viewModel.notifyAllAdapter){
            attachmentAdapterAfter.notifyAdapter(viewModel.imagesAfterCompletion)
            attachmentAdapterBefore.notifyAdapter(viewModel.imagesBeforeCompletion)
            attachmentAdapterBill.notifyAdapter(viewModel.billImages)
        }

        observeNonNull(viewModel.jobStatus){
            if (it==ConstValue.notStarted||it==ConstValue.completed){
                CommonMethods.disableAll(binding.mainLayout)
                if (it==ConstValue.completed){
                    CommonMethods.hideVisibility(binding.startTask)
                }
                else{
                    CommonMethods.showVisibility(binding.startTask)
                }
            }
            else{
                CommonMethods.hideVisibility(binding.startTask)
                CommonMethods.enableAll(binding.mainLayout)
            }
        }
    }

    private fun setAdapters() {
        binding.beforeCompletionList.layoutManager=
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        binding.afterCompletionList.layoutManager=
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        binding.billAttachmentList.layoutManager=
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        attachmentAdapterBefore = AttachmentAdapter(viewModel.imagesBeforeCompletion,ConstValue.beforeCompletionImages,this)
        attachmentAdapterAfter = AttachmentAdapter(viewModel.imagesAfterCompletion,ConstValue.workCompletionImages,this)
        attachmentAdapterBill = AttachmentAdapter(viewModel.billImages,ConstValue.billImages,this)


        binding.beforeCompletionList.adapter=attachmentAdapterBefore
        binding.afterCompletionList.adapter= attachmentAdapterAfter
        binding.billAttachmentList.adapter = attachmentAdapterBill

    }

    override fun onItemClickListener(
        clickedData: MaintenanceJobImage,
        adapterStatus: String) {
        AppConstants.imageTypeStatus= adapterStatus
        Log.e("dds ",adapterStatus)
        setupPermissions(this)
    }

    override fun removeImage(
        clickedData: MaintenanceJobImage,
        adapterStatus: String) {
        AppConstants.imageTypeStatus= adapterStatus
        runBlocking {
            withContext(Dispatchers.IO) {
                viewModel.deleteImage(clickedData)
                viewModel.notifyAllAdapter.postValue("")
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
            .setPreviewMaxCount(20)
            .showMultiImage {
                compressUriList = ArrayList()
                it.forEach {
                    val imageUri= ImageCompress.compressImagePath(it.toString(), this)
                    compressUriList.add( Uri.parse(imageUri))
                }
                viewModel.uploadMultipleImage(compressUriList)
            }


    }

    private fun makeRequest() {
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


}
