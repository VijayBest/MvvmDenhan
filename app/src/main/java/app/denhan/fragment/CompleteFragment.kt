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
import app.denhan.android.databinding.CompleteFragmentBinding
import app.denhan.model.jobs.Maintenance
import app.denhan.util.AppConstants
import app.denhan.view.home.HomeActivity
import app.denhan.view.home.HomeViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import skycap.android.core.livedata.observeNonNull

class CompleteFragment : Fragment(),OpenJobsAdapter.OpenJobsAdapterListener{
    lateinit var binding: CompleteFragmentBinding
    private val homeViewModel: HomeViewModel by sharedViewModel()
    lateinit var completeJobsArrayList: ArrayList<Maintenance>
    lateinit var openJobsAdapter: OpenJobsAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.complete_fragment, container, false)
        intiView()
        return binding.root
    }

    private fun intiView(){
        completeJobsArrayList= ArrayList()
        setAdapter()
        bindObserver()
        if (homeViewModel.completedJobsArray.value?.isNotEmpty() == true){
            completeJobsArrayList = homeViewModel.completedJobsArray.value as ArrayList<Maintenance>
            openJobsAdapter.notifyAdapter(completeJobsArrayList)
            showList()
        }
        else{
            hideList()
        }
    }
    private fun hideList(){
        binding.completeList.visibility = View.GONE
        binding.txtCompletedJobs.visibility = View.VISIBLE
    }

    private fun showList(){
        binding.completeList.visibility = View.VISIBLE
        binding.txtCompletedJobs.visibility = View.GONE
    }

    private fun bindObserver() {

        observeNonNull(homeViewModel.completedJobsArray){
            completeJobsArrayList  = it
            if (completeJobsArrayList.size>0) {
                openJobsAdapter.notifyAdapter(completeJobsArrayList)
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
        binding.completeList.layoutManager= GridLayoutManager(this.requireContext(),1)
        openJobsAdapter = OpenJobsAdapter(completeJobsArrayList, this)
        binding.completeList.adapter = openJobsAdapter

    }

    override fun onItemClick(selectedListData: Maintenance) {
        AppConstants.selectedJob= selectedListData
        (activity as HomeActivity).startTaskDetailScreen()
    }

}