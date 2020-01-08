package app.denhan.view.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.DatabaseUtils
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.denhan.android.R
import app.denhan.android.databinding.ActivitySplashBinding
import app.denhan.model.login.UserDetail
import app.denhan.util.AppConstants
import app.denhan.util.AppConstants.notificationObject
import app.denhan.util.ConstValue
import app.denhan.util.SharedPrefernencesKeys
import app.denhan.view.home.HomeActivity
import app.denhan.view.location.LocationActivity
import app.denhan.view.login.LoginActivity
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class SplashActivity : AppCompatActivity() {
    val sharedPreferences :SharedPreferences by inject()
    private val viewModel:SplashViewModel by viewModel()
    lateinit var binding :ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_splash)
        viewModel.saveDeviceApi()
        onNewIntent(intent)
        intiView()
    }


    /*intiView=> start the handler for stay in Splash  screen for 4 seconds after that start
    * the Login Activity
    * */
    private fun intiView() {
        Handler().postDelayed({
            /* Create an Intent that will start the Login Activity  */
            checkUserLoginStatus()

        }, 4000)
    }

    override fun onNewIntent(intent: Intent?) {
        val extras = intent?.extras
        extras?.let {
            notificationObject = extras.getString(ConstValue.notificationObject)?:""
            Log.e("splash_noti","  "+notificationObject)
        }
    }

    private fun checkUserLoginStatus() {
        val  userStatus= sharedPreferences.getBoolean(SharedPrefernencesKeys.loginStatus,false)
        if (userStatus){
            val gson = Gson()
            val json: String = sharedPreferences.getString(SharedPrefernencesKeys.userModel, "")
            val userData :UserDetail = gson.fromJson(json,UserDetail::class.java)
            AppConstants.userDetailData= userData
            userData?.let {

                it.session_token?.let {
                    AppConstants.sessionToken = it
                    if (isLocationEnabled()) {
                        val mainIntent = Intent(this, HomeActivity::class.java)
                        mainIntent.putExtra(ConstValue.notificationObject,notificationObject)
                        startActivity(mainIntent)
                        finish()
                    }
                    else{
                        val mainIntent = Intent(this, LocationActivity::class.java)
                        startActivity(mainIntent)
                        finish()
                    }
                }
            }
        }
        else{

            val mainIntent = Intent(this, LoginActivity::class.java)
            startActivity(mainIntent)
            finish()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

}
