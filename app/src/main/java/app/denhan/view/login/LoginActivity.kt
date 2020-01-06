package app.denhan.view.login

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import app.denhan.android.R
import app.denhan.android.databinding.ActivityLoginBinding
import app.denhan.util.CommonMethods
import app.denhan.view.home.HomeActivity
import app.denhan.view.location.LocationActivity
import org.koin.android.viewmodel.ext.android.viewModel
import skycap.android.core.livedata.observeNonNull

class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModel()
    lateinit var binding:ActivityLoginBinding
    lateinit var dialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_login)
        binding.viewModel=viewModel
        binding.lifecycleOwner= this
        intiView()

    }

    private fun intiView() {
        dialog = ProgressDialog(this)
        setPasswordFieldFont()
        bindObserver()
    }

    private fun bindObserver() {

        observeNonNull(viewModel.loginSuccessCommand){

            if (!isLocationEnabled()){

                startLocationActivity()
            }
            else {
                startLoginActivity()
            }
        }

        observeNonNull(viewModel.loginErrorCommand){

            CommonMethods.showSnackBar(binding.layout1, it)
        }

        observeNonNull(viewModel.progressVisible){
            val visible = it ?: false
            if (visible){
                CommonMethods.showProgressDialog(dialog,
                    this.resources.getString(R.string.progress_tittle_text),this.resources.getString(R.string.server_request_text))
            }
            else{
                if (dialog.isShowing){
                    CommonMethods.hideProgressDialog(dialog)
                }
            }
        }
    }

    private fun startLocationActivity() {

        val intent = Intent(this, LocationActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun startLoginActivity() {

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    /*setPasswordFieldFont=> change the font of password in run time
    * */
    private fun setPasswordFieldFont() {
        CommonMethods.changePasswordFiledFont(binding.edtPassword)
    }

}
