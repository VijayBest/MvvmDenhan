package app.denhan.util

import app.denhan.model.jobs.Attachment
import app.denhan.model.jobs.Maintenance

object ArrayConstant {

    lateinit var inProgressJobsArray: ArrayList<Maintenance>
    lateinit var completeJobsArrayList: ArrayList<Maintenance>
    lateinit var openJobsArray: ArrayList<Maintenance>
    lateinit var attachmentArrayList: ArrayList<String>
}