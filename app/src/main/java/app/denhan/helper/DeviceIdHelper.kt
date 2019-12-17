package app.denhan.helper

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

class DeviceIdHelper constructor(private val context: Context) {

    @SuppressLint("HardwareIds")
    fun getDeviceId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}