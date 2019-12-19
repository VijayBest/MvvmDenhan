package app.denhan.view.taskdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import app.denhan.adapter.InstructionAdapter
import app.denhan.adapter.TaskAdapter
import app.denhan.android.R
import app.denhan.android.databinding.ActivityTaskDetailBinding
import app.denhan.model.jobs.MaintenanceInstruction
import app.denhan.model.jobs.MaintenanceJob
import kotlinx.android.synthetic.main.task_detail_middle.view.*
import kotlinx.android.synthetic.main.task_detail_top.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import skycap.android.core.livedata.observeNonNull

class TaskDetailActivity : AppCompatActivity(), TaskAdapter.TaskAdapterListener {
    lateinit var binding :ActivityTaskDetailBinding
    private val viewModel:TaskDetailViewModel by viewModel()
    lateinit var taskAdapter: TaskAdapter
    lateinit var instructionAdapter: InstructionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_detail)
        intiView()

    }

    private fun intiView() {
        bindObserver()

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

    }
}
