package app.denhan.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import app.denhan.android.R
import app.denhan.view.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        intiView()
    }


    /*intiView=> start the handler for stay in Splash  screen for 4 seconds after that start
    * the Login Activity
    * */
    private fun intiView() {
        Handler().postDelayed({
            /* Create an Intent that will start the Login Activity  */
            val mainIntent = Intent(this, LoginActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, 4000)
    }
}
