package app.denhan.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationManager
import android.util.Log
import app.denhan.view.splash.SplashActivity


class NotificationReceiver :BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action=="VIEW") {
            startSplashScreen(context,intent?.getStringExtra("noti_object"))
        } else if (intent?.action=="DISMISS") {
            Log.e("cacel","smlssdsds")
            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(1)

        }

    }

    private fun startSplashScreen(context: Context?, stringExtra: String) {
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1)
        val intent = Intent(context, SplashActivity::class.java)
        intent.putExtra("noti_object", stringExtra)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(intent)

    }

}

