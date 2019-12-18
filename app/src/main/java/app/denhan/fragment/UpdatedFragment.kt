package app.denhan.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import app.denhan.adapter.OpenJobsAdapter
import app.denhan.android.R
import app.denhan.android.databinding.OpenJobsFragmentBinding
import app.denhan.android.databinding.UpdateFragmentBinding
import app.denhan.model.jobs.Maintenance
import app.denhan.view.home.HomeViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import skycap.android.core.livedata.observeNonNull

class UpdatedFragment : Fragment(),OpenJobsAdapter.OpenJobsAdapterListener{
    lateinit var binding: UpdateFragmentBinding
    private val homeViewModel: HomeViewModel by sharedViewModel()
    lateinit var inProgressJobsArray: ArrayList<Maintenance>
    lateinit var openJobsAdapter: OpenJobsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.update_fragment, container, false)
        intiView()
        return binding.root
}


    override fun onResume() {
        super.onResume()
        intiView()

    }
    private fun intiView(){
        inProgressJobsArray= ArrayList()
        setAdapter()
        bindObserver()
        if (homeViewModel.inProgressJobsArray.value?.isNotEmpty() == true){
            inProgressJobsArray = homeViewModel.inProgressJobsArray.value as ArrayList<Maintenance>
            openJobsAdapter.notifyAdapter(inProgressJobsArray)
            showList()
        }
        else{
            hideList()
        }
    }
    private fun hideList(){
        binding.updatedList.visibility = View.GONE
        binding.txtNoJobStarted.visibility = View.VISIBLE
    }

    private fun showList(){
        binding.updatedList.visibility = View.VISIBLE
        binding.txtNoJobStarted.visibility = View.GONE
    }

    private fun bindObserver() {

        observeNonNull(homeViewModel.inProgressJobsArray){
            inProgressJobsArray  = it
            if (inProgressJobsArray.size>0) {
                openJobsAdapter.notifyAdapter(inProgressJobsArray)
                showList()
            }
            else{
                hideList()
            }
        }
    }

    /*set the Adapter for the first time
    * */
    private fun setAdapter() {
        binding.updatedList.layoutManager= GridLayoutManager(this.requireContext(),1)
        openJobsAdapter = OpenJobsAdapter(inProgressJobsArray, this)
        binding.updatedList.adapter = openJobsAdapter


    }

    override fun onItemClick(selectedListData: Maintenance) {

    }
    fun updateSearchList(searchList:ArrayList<Maintenance>){
        openJobsAdapter.notifyAdapter(searchList)
    }
}