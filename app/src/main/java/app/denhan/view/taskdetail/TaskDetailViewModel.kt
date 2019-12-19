package app.denhan.view.taskdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.denhan.data.repos.AuthRepository
import app.denhan.model.jobs.MaintenanceInstruction
import app.denhan.model.jobs.MaintenanceJob
import app.denhan.module.ResourceProvider
import app.denhan.util.AppConstants.selectedJob
import skycap.android.core.livedata.SingleEventLiveData

class TaskDetailViewModel(private val userRepository: AuthRepository,
                          private val resourceProvider: ResourceProvider) :ViewModel(){

    var jobTitle=MutableLiveData<String>()
    var dateTime = MutableLiveData<String>()
    var address= MutableLiveData<String>()
    var callDetail = MutableLiveData<String>()
    var detailNotes= MutableLiveData<String>()
    var taskArrayList = MutableLiveData<ArrayList<MaintenanceJob>>()
    var instructionArray = MutableLiveData<ArrayList<MaintenanceInstruction>>()
    val loadAllData= SingleEventLiveData<String>()
    var detailNoteVisible=MutableLiveData<Boolean>()




    init {
        setSelectedData()
    }

    private fun setSelectedData(){

        jobTitle.postValue(selectedJob.property.ref_code+"\n"+
                selectedJob.property.active_tenancy.tenant.full_name)
        dateTime.postValue(selectedJob.f_created_date+", "+ selectedJob.f_created_time)
        address.postValue(selectedJob.property.address)
        callDetail.postValue(selectedJob.property.active_tenancy.tenant.mobile)
        selectedJob.detail_note.let {
            detailNotes.postValue("#"+selectedJob.detail_note)
            detailNoteVisible.postValue(true)
        }

        taskArrayList.postValue(selectedJob.maintenance_jobs as ArrayList<MaintenanceJob>)
        selectedJob.maintenance_instructions?.let {
            instructionArray.postValue(selectedJob.maintenance_instructions as ArrayList<MaintenanceInstruction>)
        }

        loadAllData.postValue("")


    }


}