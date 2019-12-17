package app.denhan.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import app.denhan.android.R
import app.denhan.android.databinding.OpenJobsFragmentBinding
import app.denhan.android.databinding.UpdateFragmentBinding

class UpdatedFragment : Fragment(){
    lateinit var binding: UpdateFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.update_fragment, container, false)
        return binding.root
    }

}