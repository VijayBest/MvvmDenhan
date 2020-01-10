package app.denhan.util

import app.denhan.model.jobs.Maintenance
import app.denhan.model.jobs.MaintenanceInstruction
import app.denhan.model.jobs.MaintenanceJob
import app.denhan.model.login.UserDetail

object AppConstants {
    const val notStarted = "0"
    const val inProgress = "1"
    const val completed = "2"
    var notificationToken = "demoToken123456"
    const val deviceType = 0
    var sessionToken = ""
    const val BASE_URL: String = "http://propdenhan.skycap.co.in/"
    lateinit var selectedJob: Maintenance
    lateinit var selectedSubTaskData:MaintenanceJob
    var selectedJobType = ConstValue.openJobSelected
    lateinit var jobInstructionArray:ArrayList<MaintenanceInstruction>

    /*Here we take global variable for imageStatusType.it means
        type=0 => this image belongs to before Completion of the work
        type =1 => this image belongs to after Completion of work
        type =2 => this image belongs to bill of work
     */
    var imageTypeStatus="0"
    var notificationObject=""
    lateinit var userDetailData:UserDetail
    var fromTaskDetailScreen= ConstValue.signScreen

}
