package app.denhan.binding

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

class CustomBindings {
    companion object {

        @BindingAdapter("error")
        @JvmStatic
        fun setError(editText: TextInputLayout, errorMessage: String) {
            if (errorMessage!="") {
                editText.error = errorMessage
            }
            else{
                editText.error=""
            }
        }

    }


}


