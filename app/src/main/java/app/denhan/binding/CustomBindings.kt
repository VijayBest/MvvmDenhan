package app.denhan.binding
import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputLayout
import skycap.android.core.view.setVisibleOrGone

class CustomBindings {
        @BindingAdapter("error")
        fun EditText.setError(errorMessage: String?) {
            if (errorMessage!="") {
                error = errorMessage
            }
            else{
                error=""
            }
        }

        @BindingAdapter("visibility")
        fun View.setVisibility( value:MutableLiveData<Boolean>){
            setVisibleOrGone(value.value)
        }

}


