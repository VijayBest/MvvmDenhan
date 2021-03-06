package app.denhan.view.home

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import app.denhan.adapter.TabAdapter
import app.denhan.android.R
import app.denhan.android.databinding.ActivityHomeBinding
import app.denhan.fragment.CompleteFragment
import app.denhan.fragment.OpenJobsFragments
import app.denhan.fragment.UpdatedFragment
import app.denhan.model.jobs.Maintenance
import app.denhan.util.AppConstants
import app.denhan.util.CommonMethods
import app.denhan.util.ConstValue
import app.denhan.util.CustomDialogCallBack
import app.denhan.view.login.LoginActivity
import app.denhan.view.taskdetail.TaskDetailActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.viewmodel.ext.android.viewModel
import skycap.android.core.livedata.observeNonNull

import java.lang.reflect.Field


class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModel()
    lateinit var searchArrayList: ArrayList<Maintenance>
    lateinit var dialog:ProgressDialog
    lateinit var tabAdapter: TabAdapter
    lateinit var searchIcon:ImageView
    var searchBarStatus = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        onNewIntent(intent)
        intiView()
    }

    private fun intiView() {
        dialog = ProgressDialog(this)
        openFragment()
        setTabs()
        clickEvent()
        bindObserver()
        selectedTabTextFont(0)
    }

    /* Here we are observing the live data of viewModel
    * */
    private fun bindObserver() {

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

        observeNonNull(viewModel.logoUserCommand){

            startLoginScreen()
        }

    }

    override fun onNewIntent(intent: Intent?) {
        val extras = intent?.extras
        if (extras != null) {
            val msg = extras.getString(ConstValue.notificationObject)
            AppConstants.notificationObject=msg
            Log.e("home_noti ", msg)
                if(!AppConstants.notificationObject.isNullOrEmpty()) {
                    AppConstants.fromTaskDetailScreen = ConstValue.homeScreen
                    val intent = Intent(this, TaskDetailActivity::class.java)
                    intent.putExtra(ConstValue.notificationObject,AppConstants.notificationObject)
                    startActivity(intent)
                }

        }
    }
    override fun onResume() {
        super.onResume()
        if (AppConstants.fromTaskDetailScreen==ConstValue.signScreen){
            viewModel.refreshButtonClick()
        }
    }

    private fun clickEvent() {
        val closeButton = binding.searchView.findViewById(R.id.search_close_btn) as ImageView
       // closeButton.setImageDrawable(this.resources.getDrawable(R.drawable.ic_cross_icon))
        closeButton.setColorFilter(resources.getColor(R.color.whiteColor))
        searchIcon = binding.searchView.findViewById(R.id.search_button)as ImageView
        searchIcon.setColorFilter(resources.getColor(R.color.search_place_holder))
       // searchIcon.setImageDrawable(this.resources.getDrawable(R.drawable.search))
        val searchEditText =
            binding.searchView.findViewById(R.id.search_src_text) as EditText
        searchEditText.setTextColor(resources.getColor(R.color.whiteColor))
        searchEditText.setTextSize(2,15f)
        searchEditText.setHint(this.resources.getString(R.string.search_hint_text))
        searchEditText.setHintTextColor(resources.getColor(R.color.search_place_holder))
        val searchTextView =
            binding.searchView.findViewById(R.id.search_src_text) as AutoCompleteTextView
        try {
            val mCursorDrawableRes: Field =
                TextView::class.java.getDeclaredField("mCursorDrawableRes")
            mCursorDrawableRes.setAccessible(true)
            mCursorDrawableRes.set(
                searchTextView,
                R.drawable.search_cursor
            ) //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (e: Exception) {
        }
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                selectedTabTextFont(tab.position)

            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

                    unselectedTabTextFont(tab.position)
            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        binding.searchImage.setOnClickListener {

            showSearchView()
        }


        closeButton.setOnClickListener {

            hideSearchView()
            viewModel.refreshButtonClick()
        }

        /*  text submit Click event on SearchView
        *
        * */
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchSpecificString(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        binding.logoutImage.setOnClickListener {
            CommonMethods.customCommonDialog(this,this.resources.getString(R.string.app_name),
                this.resources.getString(R.string.logout_instruction), this.resources.getString(R.string.cancel_text),
                this.resources.getString(R.string.okay_text),object :CustomDialogCallBack{
                    override fun positiveButtonClick() {
                        viewModel.logoutUser()
                    }

                    override fun negativeButtonClick() {

                    }
                })

        }

    }

    /*startLoginScreen=> start the login screen after logout the user
    * */
    private fun startLoginScreen() {

        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun searchSpecificString(query: String) {
        searchArrayList= ArrayList()
        val currentFragment = viewPager.currentItem
        when(currentFragment){
            0->{
              searchOpenJobs(query)
            }
            1->{
                searchInProgressJobs(query)
            }
            2->{
                searchInCompleteJobs(query)
            }
        }

    }

    /*selectedTabTextFont=> Here we are changing the font of selected tab text
    * */
  private  fun selectedTabTextFont(tabPosition:Int){
        val tabLayout =
            ( binding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(tabPosition) as LinearLayout
        val tabTextView = tabLayout.getChildAt(1) as TextView
        val selectedFont = Typeface.createFromAsset(this.resources.assets, "fonts/muli_bold.ttf")
        tabTextView.setTypeface(selectedFont)

    }

    /*unselectedTabTextFont=> Here We are changing the font of unselected Tab text
    *
    * */
  private  fun unselectedTabTextFont(tabPosition:Int){
        val tabLayout =
            ( binding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(tabPosition) as LinearLayout
        val tabTextView = tabLayout.getChildAt(1) as TextView
        val selectedFont = Typeface.createFromAsset(this.resources.assets, "fonts/muli_regular.ttf")
        tabTextView.setTypeface(selectedFont)
    }

    private fun searchOpenJobs(query: String) {

        val openJobs = viewModel.openJobsArray.value as ArrayList
        openJobs.forEach {
            if (it.repair_code.contains(query) || it.property.address.toLowerCase().contains(query.toLowerCase())){
                searchArrayList.add(it)
            }
        }
        val openFragment =   tabAdapter.getItem(0) as OpenJobsFragments
        openFragment.updateSearchList(searchArrayList)
    }

    private fun searchInProgressJobs(query: String)
    {
        val progressJobs = viewModel.inProgressJobsArray.value as ArrayList
        progressJobs.forEach {
            if (it.repair_code.contains(query) || it.property.address.toLowerCase().contains(query.toLowerCase())){
                searchArrayList.add(it)
            }
        }
        val openFragment =   tabAdapter.getItem(1) as UpdatedFragment
        openFragment.updateSearchList(searchArrayList)
    }

    fun searchJobs(){
        if (binding.searchView.query.length>=1 && searchBarStatus){
            searchSpecificString(binding.searchView.query.toString())
        }
    }

    private fun searchInCompleteJobs(query: String)
    {
        val progressJobs = viewModel.completedJobsArray.value as ArrayList
        progressJobs.forEach {
            if (it.repair_code.contains(query) || it.property.address.toLowerCase().contains(query.toLowerCase())){
                searchArrayList.add(it)
            }
        }
        val openFragment =   tabAdapter.getItem(2) as CompleteFragment
        openFragment.updateSearchList(searchArrayList)
    }



    private fun hideSearchView() {
        binding.searchView.setQuery("", false)
        //Collapse the action view
        binding.searchView.onActionViewCollapsed()
        binding.searchView.visibility = View.GONE
        searchBarStatus=false
        binding.refreshImage.visibility=View.VISIBLE
        binding.searchImage.visibility = View.VISIBLE
        binding.tittleText.visibility = View.VISIBLE
    }

    /*showSearchView=> show the searchView and hide the other image placed
    * at right side of of the top bar
    * */
    private fun showSearchView() {

        binding.searchView.visibility= View.VISIBLE
        searchBarStatus=true
        binding.searchView.onActionViewExpanded() //new Added line
        binding.refreshImage.visibility=View.GONE
        binding.searchImage.visibility = View.GONE
        binding.tittleText.visibility = View.GONE
    }


    private fun setTabs(){
        val slidingTabStrip = binding.tabLayout.getChildAt(0) as ViewGroup
        val betweenSpace=100
        for (i in 0 until slidingTabStrip.childCount - 1) {
            val v = slidingTabStrip.getChildAt(i)
            val params = v.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(30, 5, 80, 0)
        }
    }

    private fun openFragment() {
        tabAdapter = TabAdapter(supportFragmentManager)
        tabAdapter.addFragment(OpenJobsFragments(), this.resources.getString(R.string.open_text))
        tabAdapter.addFragment(UpdatedFragment(), this.resources.getString(R.string.in_progress_text))
        tabAdapter.addFragment(CompleteFragment(), this.resources.getString(R.string.complete_text))
        binding.viewPager.adapter = tabAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

    }

    fun startTaskDetailScreen(){
        AppConstants.fromTaskDetailScreen = ConstValue.homeScreen
        val intent = Intent(this, TaskDetailActivity::class.java)
        startActivity(intent)

    }

    override fun onBackPressed()
    {
        if (binding.searchView.query.length<1 && searchBarStatus) {
            hideSearchView()
            viewModel.refreshButtonClick()
        }
        else {
            super.onBackPressed()
        }

    }
}
