package app.denhan.view.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import app.denhan.android.R
import app.denhan.android.databinding.ActivityHomeBinding
import app.denhan.fragment.OpenJobsFragments
import app.denhan.adapter.TabAdapter
import app.denhan.fragment.CompleteFragment
import app.denhan.fragment.UpdatedFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_home.*
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView


class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_home)
        intiView()
    }

    private fun intiView() {
        openFragment()
        setTabs()
        clickEvent()
    }

    private fun clickEvent() {
        val closeButton = binding.searchView.findViewById(app.denhan.android.R.id.search_close_btn) as ImageView
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                Log.e("sdsad ",tab.position.toString())
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        binding.searchImage.setOnClickListener {

            showSearchView()
        }


        closeButton.setOnClickListener {

            hideSearchView()
        }


        /*  text submit Click event on SearchView
        *
        * */
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

    }

    private fun hideSearchView() {
        binding.searchView.setQuery("", false)
        //Collapse the action view
        binding.searchView.onActionViewCollapsed()
        binding.searchView.visibility = View.GONE
        binding.refreshImage.visibility=View.VISIBLE
        binding.searchImage.visibility = View.VISIBLE
        binding.tittleText.visibility = View.VISIBLE
    }

    /*showSearchView=> show the searchView and hide the other image placed
    * at right side of of the top bar
    * */
    private fun showSearchView() {

        binding.searchView.visibility= View.VISIBLE
        binding.searchView.onActionViewExpanded() //new Added line
        binding.refreshImage.visibility=View.GONE
        binding.searchImage.visibility = View.GONE
        binding.tittleText.visibility = View.GONE
    }


    fun setTabs(){
        val slidingTabStrip = binding.tabLayout.getChildAt(0) as ViewGroup
        val betweenSpace=100
        for (i in 0 until slidingTabStrip.childCount - 1) {
            val v = slidingTabStrip.getChildAt(i)
            val params = v.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(30, 5, 80, 0)
        }
    }

    private fun openFragment() {
        val  tabAdapter = TabAdapter(supportFragmentManager)
        tabAdapter.addFragment(OpenJobsFragments(), this.resources.getString(R.string.open_text))
        tabAdapter.addFragment(UpdatedFragment(), this.resources.getString(R.string.in_progress_text))
        tabAdapter.addFragment(CompleteFragment(), this.resources.getString(R.string.complete_text))
        binding.viewPager.adapter = tabAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

    }
}
