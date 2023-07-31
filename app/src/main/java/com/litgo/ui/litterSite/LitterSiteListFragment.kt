package com.litgo.ui.litterSite

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.litgotesting.viewModel.LitgoUiState
import com.example.litgotesting.viewModel.LitterSiteUiState
import com.litgo.R
import com.litgo.databinding.FragmentLitterSiteListBinding
import com.litgo.viewModel.LitgoViewModel
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of LitterSite reports
 * (that may or may not have been cleaned up by the user)
 */
class LitterSiteListFragment : Fragment() {

    private val viewModel: LitgoViewModel by activityViewModels()
    private var _binding: FragmentLitterSiteListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLitterSiteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val supportFragmentManager = activity?.supportFragmentManager
        var litterSiteRecyclerView = binding.litterSiteRecyclerView

        // Set up sort and filter spinners
        val sortSpinner = binding.litterSiteSortSpinner.apply {
            adapter = this.context?.let {
                ArrayAdapter.createFromResource(
                    it,
                    R.array.litter_site_sort_spinner_array,
                    android.R.layout.simple_spinner_item
                )
            }
        }

        // Set spinners for sorting and filtering reports/cleanups
        val filterSpinner = binding.litterSiteFilterSpinner.apply {
            adapter = this.context?.let {
                ArrayAdapter.createFromResource(
                    it,
                    R.array.litter_site_filter_spinner_array,
                    android.R.layout.simple_spinner_item
                )
            }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                /* FOR TESTING
                val testLitterSites = testReports()

                val reportsAdapter = LitterSitesRecyclerViewAdapter(
                    testLitterSites[1],
                    supportFragmentManager
                )
                val cleanupsAdapter = LitterSitesRecyclerViewAdapter(
                    testLitterSites[2],
                    supportFragmentManager
                )
                val allAdapter = LitterSitesRecyclerViewAdapter(
                    testLitterSites[0],
                    supportFragmentManager
                )
                 */

                val reportsAdapter = LitterSitesRecyclerViewAdapter(
                    viewModel.uiState.value.userUiState.reports,
                    supportFragmentManager
                )
                val cleanupsAdapter = LitterSitesRecyclerViewAdapter(
                    viewModel.uiState.value.userUiState.cleanups,
                    supportFragmentManager
                )

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (resources.getStringArray(R.array.litter_site_filter_spinner_array)[position]) {
                        "My Reports" -> litterSiteRecyclerView.adapter = reportsAdapter
                        "My Cleanups" -> litterSiteRecyclerView.adapter = cleanupsAdapter
                        else -> litterSiteRecyclerView.adapter = reportsAdapter
                    }
                }

                // Display all reports created by the user by default
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    litterSiteRecyclerView.adapter = reportsAdapter
                }

            }
        }

        binding.litterSiteRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
        }
        // Ensure we are fetching all relevant information to the user
//        viewModel.getLitterSitesCreatedByUser()
//        viewModel.getLitterSitesCleanedByUser()

        lifecycleScope.launch {
            viewModel.observeState().collect {
                renderState(it)
            }
        }

    }

    private fun renderState(it: LitgoUiState) {
        var litterSiteRecyclerView = binding.litterSiteRecyclerView

        /* FOR TESTING
        val testLitterSites = testReports()
        val allAdapter = LitterSitesRecyclerViewAdapter(testLitterSites[0], activity?.supportFragmentManager)
        val reportsAdapter = LitterSitesRecyclerViewAdapter(testLitterSites[1], activity?.supportFragmentManager)
        val cleanupsAdapter = LitterSitesRecyclerViewAdapter(testLitterSites[2], activity?.supportFragmentManager)
         */

        val reportsAdapter = LitterSitesRecyclerViewAdapter(it.userUiState.reports, activity?.supportFragmentManager)
        val cleanupsAdapter = LitterSitesRecyclerViewAdapter(it.userUiState.cleanups, activity?.supportFragmentManager)

        // Display the reports according to whatever is currently selected on the dropdown menu
        when (binding.litterSiteFilterSpinner.selectedItem) {
            "My Reports" -> litterSiteRecyclerView.adapter = reportsAdapter
            "My Cleanups" -> litterSiteRecyclerView.adapter = cleanupsAdapter
            else -> litterSiteRecyclerView.adapter = reportsAdapter
        }
    }

    /**
     * THIS IS A METHOD FOR TESTING PURPOSES
     * Generates a list of reports and cleanups
     */
    private fun testReports() : List<List<LitterSiteUiState>> {
        val testAll = listOf(
            LitterSiteUiState(
                "",
                "",
                "",
                false,
                37,
                "",
                "HAZARDOUS",
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
                "NONE",
                resources.getString(R.string.test_lorem_ipsum),
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
                "CAUTION",
                resources.getString(R.string.test_lorem_ipsum),
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
                "CAUTION",
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
                resources.getString(R.string.test_lorem_ipsum),
                -33.868820,
                151.209290
            ),
        )

        val testReports = listOf(
            LitterSiteUiState(
                "",
                "",
                "",
                false,
                37,
                "",
                "HAZARDOUS",
                "This is a description of the litter found at the site.",
                13.756331,
                100.501762
            ),
            LitterSiteUiState(
                "",
                "",
                "",
                false,
                50,
                "",
                "CAUTION",
                resources.getString(R.string.test_lorem_ipsum),
                -33.868820,
                151.209290
            ),
        )

        val testCleanups = listOf(
            LitterSiteUiState(
                "",
                "",
                "",
                true,
                97,
                "",
                "CAUTION",
                resources.getString(R.string.test_lorem_ipsum),
                -6.175110,
                106.865036
            ),

            LitterSiteUiState(
                "",
                "",
                "",
                true,
                37,
                "",
                "HAZARDOUS",
                "This is a description of the litter found at the site.",
                13.756331,
                100.501762
            ),
            LitterSiteUiState(
                "",
                "",
                "",
                true,
                3,
                "",
                "NONE",
                resources.getString(R.string.test_lorem_ipsum),
                -33.868820,
                151.209290
            ),
        )

        return listOf(testAll, testReports, testCleanups)
    }

}