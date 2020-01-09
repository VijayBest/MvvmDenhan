package app.denhan.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import app.denhan.android.R
import app.denhan.broadcast.NotificationReceiver
import app.denhan.util.AppConstants
import app.denhan.view.splash.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject






class NotificationReceived :FirebaseMessagingService(){
    val CHANNEL_ID = "denhan_app"
    val CHANNEL_NAME = "denhan_app_noti"
    val CHANNEL_DESC = "denhan_app_code"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val hashMap: MutableMap<String, String>? = remoteMessage?.data
        val responseObject = JSONObject(hashMap)
        Log.e("OBJECT_NOTIFICATION", responseObject.toString())
        val tittle = responseObject.getString("title")
        val subtittle= responseObject.getString("body")
        setupChannelId()
        notificationActions(tittle, subtittle,responseObject.toString())
    }

    private fun setupChannelId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_DESC
            val manager = getSystemService(NotificationManager::class.java) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }


    private fun notificationActions(tittle: String, subTittle: String, notificationObject: String) {
        AppConstants.notificationObject=notificationObject
        val NOTIFICATION_ID = 1
        val builder = NotificationCompat.Builder(this,CHANNEL_ID)
        builder.setSmallIcon(R.drawable.tittle_icon)
        builder.color = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        builder.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.tittle_icon))
        builder.setContentTitle(tittle)
         builder.setStyle(NotificationCompat.BigTextStyle()
                .bigText(subTittle))
        builder.setContentText(subTittle)
        builder.setAutoCancel(true)


        val intentConfirm = Intent(this, NotificationReceiver::class.java)
        intentConfirm.action = "VIEW"
        intentConfirm.putExtra("noti_object",notificationObject)
        intentConfirm.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)


        val intentCancel = Intent(this, NotificationReceiver::class.java)
        intentCancel.putExtra("noti_object",notificationObject)
        intentCancel.action = "DISMISS"
        intentCancel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val launchIntent = getLaunchIntent(NOTIFICATION_ID,notificationObject)

        //clicked by user.
        val pendingIntentConfirm = PendingIntent.getBroadcast(this, 0, intentConfirm, PendingIntent.FLAG_CANCEL_CURRENT)

//This Intent will be called when Cancel button from notification will be
//clicked by user.
        val pendingIntentCancel = PendingIntent.getBroadcast(this, 1, intentCancel, PendingIntent.FLAG_CANCEL_CURRENT)

        builder.setContentIntent(launchIntent)

        builder.addAction(android.R.drawable.ic_menu_view, "View", pendingIntentConfirm)
        builder.addAction(android.R.drawable.ic_delete, "Dismiss", pendingIntentCancel)
        /*builder.addAction(android.R.drawable.ic_menu_view, "VIEW", pendingIntent)
        builder.addAction(android.R.drawable.ic_delete, "DISMISS", dismissIntent)*/



        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Will display the notification in the notification bar
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun getLaunchIntent(notificationId: Int, notificationObject: String): PendingIntent {
        val intent = Intent(this, SplashActivity::class.java)
        intent.putExtra("notificationId", notificationId)
        intent.putExtra("noti_object",notificationObject)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }



}