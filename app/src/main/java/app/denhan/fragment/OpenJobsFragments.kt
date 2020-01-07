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
import app.denhan.model.jobs.Maintenance
import app.denhan.util.AppConstants
import app.denhan.util.ArrayConstant.openJobsArray
import app.denhan.util.ConstValue
import app.denhan.view.home.HomeActivity
import app.denhan.view.home.HomeViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import skycap.android.core.livedata.observeNonNull

class OpenJobsFragments :Fragment(),OpenJobsAdapter.OpenJobsAdapterListener{
    lateinit var binding:OpenJobsFragmentBinding
    private val homeViewModel:HomeViewModel by sharedViewModel()
    lateinit var openJobsAdapter:OpenJobsAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.open_jobs_fragment, container, false)
        intiView()
        return binding.root
    }

    override fun onItemClick(selectedListData: Maintenance) {
        AppConstants.selectedJob= selectedListData
        AppConstants.selectedJobType = ConstValue.openJobSelected
        (activity as HomeActivity).startTaskDetailScreen()
    }

    private fun intiView() {
        openJobsArray= ArrayList()
        setAdapter()
        bindObserver()
        if (homeViewModel.openJobsArray.value?.isNotEmpty() == true){
            openJobsArray = homeViewModel.openJobsArray.value as ArrayList<Maintenance>
            openJobsAdapter.notifyAdapter(openJobsArray)
            showList()
        }
        else{

            hideList()
        }
    }
    private fun hideList(){
        binding.openJobsList.visibility = View.GONE
        binding.txtNoJobAssigned.visibility = View.VISIBLE
    }

    private fun showList(){
        binding.openJobsList.visibility = View.VISIBLE
        binding.txtNoJobAssigned.visibility = View.GONE
    }

    private fun bindObserver() {

        observeNonNull(homeViewModel.openJobsArray){
            openJobsArray  = it
            if (openJobsArray.size>0) {
                openJobsAdapter.notifyAdapter(openJobsArray)
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
        binding.openJobsList.layoutManager= GridLayoutManager(this.requireContext(),1)
        openJobsAdapter = OpenJobsAdapter(openJobsArray, this)
        binding.openJobsList.adapter = openJobsAdapter


    }

    fun updateSearchList(searchList:ArrayList<Maintenance>){
        if (searchList.size>0){
            binding.txtNoJobAssigned.visibility=View.GONE
        }
        else{
            binding.txtNoJobAssigned.text = this.resources.getString(R.string.no_update_job_find)
            binding.txtNoJobAssigned.visibility= View.VISIBLE
        }
        openJobsAdapter.notifyAdapter(searchList)
    }

}