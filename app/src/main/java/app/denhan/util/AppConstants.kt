package app.denhan.util

import app.denhan.model.jobs.Maintenance

object AppConstants {
    const val notStarted="0"
    const val inProgress="1"
    const val completed="2"
    var notificationToken ="demoToken123456"
  const val deviceType =0
  var sessionToken=""
  const val BASE_URL: String = "http://propdenhan.skycap.co.in/"
  lateinit var selectedJob : Maintenance
}
