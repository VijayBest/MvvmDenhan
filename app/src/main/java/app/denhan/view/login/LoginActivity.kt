package app.denhan.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import app.denhan.android.R
import app.denhan.android.databinding.ActivityLoginBinding
import app.denhan.util.CommonMethods
import app.denhan.view.home.HomeActivity
import org.koin.android.viewmodel.ext.android.viewModel
import skycap.android.core.livedata.observeNonNull

class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModel()
    lateinit var binding:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_login)
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding.viewModel=viewModel
        binding.lifecycleOwner = this
        setPasswordFieldFont()
        bindObserver()
    }

    private fun bindObserver() {

        observeNonNull(viewModel.loginSuccessCommand){

            stratLoginActivity()
        }
    }

    private fun stratLoginActivity() {

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    /*setPasswordFieldFont=> chnage the font of password in run time
    * */
    private fun setPasswordFieldFont() {
        CommonMethods.changePasswordFiledFont(binding.edtPassword)
    }

}
