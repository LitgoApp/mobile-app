package com.litgo.ui.litterSite

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.litgotesting.viewModel.LitterSiteUiState
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.litgo.MainActivity
import com.litgo.R
import com.litgo.databinding.FragmentLitterSiteReportsBinding
import com.litgo.viewModel.LitterSiteViewModel
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of LitterSite reports
 * (that may or may not have been cleaned up by the user)
 */
class LitterSiteReportsFragment : Fragment() {

    private val viewModel: LitterSiteViewModel by activityViewModels()
    private var _binding: FragmentLitterSiteReportsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLitterSiteReportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val mainActivityLayout = activity?.findViewById<ConstraintLayout>(R.id.main_activity_layout)
//        mainActivityLayout?.setBackgroundColor(resources.getColor(R.color.lightest_gray))

        val appBarLayout = activity?.findViewById<AppBarLayout>(R.id.app_bar_layout)
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_bottom)
        appBarLayout?.visibility = View.VISIBLE
        navBar?.visibility = View.VISIBLE

//        (activity as MainActivity).setBackgroundColor(R.color.lightest_gray)
//        (activity as MainActivity).showAppAndNavBars()

        val testReports = listOf(
            LitterSiteUiState(
                "",
                "",
                "",
                false,
                37,
                "",
                "Hazardous",
                "This is a description of the litter found at the site.",
                13.756331,
                100.501762
            ),
            LitterSiteUiState(
                "",
                "",
                "",
                true,
                50,
                "",
                "",
                resources.getString(R.string.lorem_ipsum),
                -6.175110,
                106.865036
            ),
            LitterSiteUiState(
                "",
                "",
                "",
                false,
                50,
                "",
                "",
                resources.getString(R.string.lorem_ipsum),
                -33.868820,
                151.209290
            ),
            LitterSiteUiState(
                "",
                "",
                "",
                true,
                37,
                "",
                "Hazardous",
                "This is a description of the litter found at the site.",
                13.756331,
                100.501762
            ),
            LitterSiteUiState(
                "",
                "",
                "",
                true,
                50,
                "",
                "",
                resources.getString(R.string.lorem_ipsum),
                -33.868820,
                151.209290
            ),
        )

        // Set up sort and filter spinners
        val sortSpinner = binding.litterSiteSortSpinner
        val sortSpinnerAdapter = this.context?.let { ArrayAdapter.createFromResource(it, R.array.litter_site_sort_spinner_array, android.R.layout.simple_spinner_item) }
        sortSpinner.adapter = sortSpinnerAdapter

        val filterSpinner = binding.litterSiteFilterSpinner
        val filterSpinnerAdapter = this.context?.let { ArrayAdapter.createFromResource(it, R.array.litter_site_filter_spinner_array, android.R.layout.simple_spinner_item) }
        filterSpinner.adapter = filterSpinnerAdapter

        val supportFragmentManager = activity?.supportFragmentManager
        var reportsAdapter = LitterSitesRecyclerViewAdapter(viewModel.uiState.value.userUiState.reports, supportFragmentManager)
        var cleanupsAdapter = LitterSitesRecyclerViewAdapter(viewModel.uiState.value.userUiState.cleanups, supportFragmentManager)

        lifecycleScope.launch {
            viewModel.observeState().collect {
                // Default adapter shows the user all reports they've made
                var litterSiteRecyclerView = binding.litterSiteRecyclerView
                litterSiteRecyclerView.layoutManager = LinearLayoutManager(context)
                litterSiteRecyclerView.adapter = LitterSitesRecyclerViewAdapter(testReports, supportFragmentManager)

//                reportsAdapter = LitterSitesRecyclerViewAdapter(it.userUiState.reports)
//                cleanupsAdapter = LitterSitesRecyclerViewAdapter(it.userUiState.cleanups)
            }
        }
    }
}