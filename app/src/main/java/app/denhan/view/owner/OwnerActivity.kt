package app.denhan.view.owner

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import app.denhan.adapter.OwnerAdapter
import app.denhan.android.R
import app.denhan.android.databinding.ActivityOwnerBinding
import app.denhan.model.owner.OwnerNotAvailableData
import app.denhan.util.ArrayConstant
import app.denhan.util.CommonMethods
import app.denhan.util.CommonMethods.hideVisibility
import app.denhan.util.CommonMethods.showVisibility
import app.denhan.view.imageslider.ImageSlider
import kotlinx.android.synthetic.main.activity_owner.*
import org.koin.android.viewmodel.ext.android.viewModel
import skycap.android.core.livedata.observeNonNull

class OwnerActivity : AppCompatActivity(), OwnerAdapter.OwnerAdapterListener {
    lateinit var binding: ActivityOwnerBinding
    private val viewModel:OwnerViewModel by viewModel()
    lateinit var dialog: ProgressDialog
    lateinit var ownerAdapter: OwnerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_owner)
        binding.viewModel= viewModel
        binding.lifecycleOwner= this
        intiView()
    }

    private fun intiView() {
        dialog = ProgressDialog(this)
        intilizeList()
        bindObserver()
        clickEvent()


    }

    override fun onResume() {
        super.onResume()
        viewModel.getOwnerList()
    }

    private fun clickEvent() {

        binding.backImage.setOnClickListener {
            finish()
        }
        binding.addNewLog.setOnClickListener {

            startAddNewLogScreen()
        }
    }


    /*
    * startAddNewLogScreen=> start the AddNewLog Screen
    * */
    private fun startAddNewLogScreen() {

        val addNewLogScreen = Intent(this, AddLogActivity::class.java)
        startActivity(addNewLogScreen)


    }

    private fun intilizeList() {
        binding.ownerList.layoutManager= GridLayoutManager(this,1)
    }


    /*
    * bindObserver=> Here we are observing the live data
    * */
    private fun bindObserver() {

        observeNonNull(viewModel.progressVisible){
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

        //observe ownerList here
        observeNonNull(viewModel.successesCommand){
            setAdapter()
        }

        observeNonNull(viewModel.imageSlideData){

            startImageSlideData()
        }
    }

    private fun startImageSlideData() {

        val imageSlideScreen = Intent(this,ImageSlider::class.java)
        startActivity(imageSlideScreen)

    }

    private fun setAdapter() {
        ownerAdapter = OwnerAdapter(viewModel.ownerArrayList, this)
        binding.ownerList.adapter = ownerAdapter
        if (viewModel.ownerArrayList.size>0){
            hideVisibility(binding.txtNoLog)
        }
        else{
            showVisibility(binding.txtNoLog)
        }
    }

    override fun onItemClickListener(clickedData: OwnerNotAvailableData) {

        viewModel.fetchAttachmentList(clickedData)
    }



}
