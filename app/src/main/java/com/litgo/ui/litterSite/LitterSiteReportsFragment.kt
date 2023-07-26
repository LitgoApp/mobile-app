package com.litgo.ui.litterSite

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.litgotesting.viewModel.LitterSiteUiState
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

        var reportsAdapter = LitterSitesRecyclerViewAdapter(viewModel.uiState.value.userUiState.reports)
        var cleanupsAdapter = LitterSitesRecyclerViewAdapter(viewModel.uiState.value.userUiState.cleanups)

        lifecycleScope.launch {
            viewModel.observeState().collect {
                // Default adapter shows the user all reports they've made
                var litterSiteRecyclerView = binding.litterSiteRecyclerView
                litterSiteRecyclerView.layoutManager = LinearLayoutManager(context)
                litterSiteRecyclerView.adapter = LitterSitesRecyclerViewAdapter(testReports)

//                reportsAdapter = LitterSitesRecyclerViewAdapter(it.userUiState.reports)
//                cleanupsAdapter = LitterSitesRecyclerViewAdapter(it.userUiState.cleanups)
            }
        }
    }
}