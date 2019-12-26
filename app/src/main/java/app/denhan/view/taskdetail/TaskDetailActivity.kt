package app.denhan.view.taskdetail

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import app.denhan.adapter.InstructionAdapter
import app.denhan.adapter.TaskAdapter
import app.denhan.android.R
import app.denhan.android.databinding.ActivityTaskDetailBinding
import app.denhan.model.jobs.Attachment
import app.denhan.model.jobs.MaintenanceInstruction
import app.denhan.model.jobs.MaintenanceJob
import app.denhan.util.AppConstants
import app.denhan.util.AppConstants.selectedJob
import app.denhan.util.AppConstants.selectedSubTaskData
import app.denhan.util.ArrayConstant
import app.denhan.util.ArrayConstant.attachmentArrayList
import app.denhan.util.CommonMethods
import app.denhan.view.imageslider.ImageSlider
import app.denhan.view.owner.OwnerActivity
import app.denhan.view.subtask.SubTaskActivity
import kotlinx.android.synthetic.main.task_detail_middle.view.*
import kotlinx.android.synthetic.main.task_detail_top.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import skycap.android.core.livedata.observeNonNull

class TaskDetailActivity : AppCompatActivity(), TaskAdapter.TaskAdapterListener {
    lateinit var binding :ActivityTaskDetailBinding
    private val viewModel:TaskDetailViewModel by viewModel()
    lateinit var taskAdapter: TaskAdapter
    lateinit var instructionAdapter: InstructionAdapter
    lateinit var dialog:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_detail)
        intiView()

    }

    private fun intiView() {
        dialog = ProgressDialog(this)
        bindObserver()
        clickEvent()
    }

    override fun onResume() {
        super.onResume()
        viewModel.callTaskDetail()
    }


    private fun clickEvent() {

        binding.addTaskButton.setOnClickListener {
            CommonMethods.showAddTaskDialog(this, object: DialogCallBack{
                override fun filledValue(tittle: String, description: String) {
                    viewModel.addTask(tittle,description)
                }
            })
        }

        binding.backImage.setOnClickListener {

            finish()
        }
        binding.mainLayout.topLayoutUi.attachmentImage.setOnClickListener {

            if (selectedJob.property.attachments.isNotEmpty()){
                attachmentArrayList = ArrayList()
                selectedJob.property.attachments.forEach {
                    attachmentArrayList.add(it.attachment_path?:"")
                }
                startImageSlidingScreen()
            }
            else {
                CommonMethods.defaultDialog(
                    this, this.resources.getString(R.string.attachment_text),
                    this.resources.getString(R.string.no_attachment_text)
                )
            }
        }

        binding.ownerImage.setOnClickListener {

            startOwnerScreen()
        }
    }

    /*
    * startOwnerScreen=> start the owner screen where the
    * list of not available owner  will be there
    * */
    private fun startOwnerScreen() {

        val ownerScreen = Intent(this, OwnerActivity::class.java)
        startActivity(ownerScreen)
    }

    private fun bindObserver() {
        observeNonNull(viewModel.loadAllData){
            binding.mainLayout.topLayoutUi.jobTitle.text= viewModel.jobTitle.value?:""
            binding.mainLayout.topLayoutUi.dateTime.text = viewModel.dateTime.value?:""
            binding.mainLayout.topLayoutUi.locationText.text = viewModel.address.value?:""
            binding.mainLayout.topLayoutUi.callText.text = viewModel.callDetail.value?:""

            if (viewModel.detailNoteVisible.value==true){
                binding.middleLayout.txtDetailNote.text = viewModel.detailNotes.value?:""
                binding.middleLayout.detailNoteCardView.visibility = View.VISIBLE

            }
            else{
                binding.middleLayout.detailNoteCardView.visibility = View.GONE
            }
            setInstructionArray(viewModel.instructionArray)
            setTaskAdapter(viewModel.taskArrayList.value as ArrayList<MaintenanceJob>)

        }
        observeNonNull(viewModel.addedTask){

            taskAdapter.notifyAdapter(viewModel.taskArrayList.value as ArrayList<MaintenanceJob>)
        }
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
    }

    private fun setInstructionArray(instructionArray: MutableLiveData<ArrayList<MaintenanceInstruction>>) {

        if (instructionArray.value?.isNotEmpty() == true) {
            binding.middleLayout.specialInstructionList.visibility=View.VISIBLE
            binding.middleLayout.text1.visibility = View.VISIBLE
            binding.middleLayout.specialInstructionList.layoutManager= GridLayoutManager(this,1)
            instructionAdapter = InstructionAdapter(instructionArray.value as ArrayList<MaintenanceInstruction>)
            binding.middleLayout.specialInstructionList.adapter = instructionAdapter
        }
        else{
            binding.middleLayout.specialInstructionList.visibility=View.GONE
            binding.middleLayout.text1.visibility = View.GONE
        }
    }

    /*
    * setTaskAdapter=>  Here we are set the sub task of jobs and their status
    *
    * */
    private fun setTaskAdapter(taskArrayList: ArrayList<MaintenanceJob>) {

        binding.middleLayout.taskList.layoutManager = GridLayoutManager(this,1)
        taskAdapter = TaskAdapter(taskArrayList, this)
        binding.middleLayout.taskList.adapter =taskAdapter
    }

    override fun onItemClick(selectedTaskData: MaintenanceJob) {
        selectedSubTaskData = selectedTaskData
        startSubTaskDetailScreen()
    }

    private fun startSubTaskDetailScreen() {

        val subTaskIntent = Intent(this,SubTaskActivity::class.java)
        startActivity(subTaskIntent)
    }

    /*startImageSlidingScreen==>start the screen if the
    * attachment array is not empty
    * */
    private fun startImageSlidingScreen() {

        val imageSlideScreen = Intent(this, ImageSlider::class.java)
        startActivity(imageSlideScreen)
    }
}
