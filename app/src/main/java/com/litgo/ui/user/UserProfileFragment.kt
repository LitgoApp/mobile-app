package com.litgo.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.litgotesting.viewModel.LitgoUiState
import com.example.litgotesting.viewModel.LitterSiteUiState
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.litgo.R
import com.litgo.databinding.FragmentUserProfileBinding
import com.litgo.ui.litterSite.LitterSitesRecyclerViewAdapter
import com.litgo.viewModel.LitgoViewModel
import kotlinx.coroutines.launch

class UserProfileFragment : Fragment() {
    private val viewModel: LitgoViewModel by activityViewModels()
    private var _binding: FragmentUserProfileBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.observeState().collect {
                renderState(it)
            }
        }
    }

    private fun renderState(it: LitgoUiState) {
        binding.userRecentActivityRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.userRecentActivityRecyclerView.adapter = LitterSitesRecyclerViewAdapter(testReports()[0], activity?.supportFragmentManager)
//        binding.userRecentActivityRecyclerView.adapter = LitterSitesRecyclerViewAdapter(it.userUiState.reports, activity?.supportFragmentManager)
        binding.userNameTextview.text = it.userUiState.name
        binding.userPointsTextview.text = it.userUiState.points.toString()
        binding.userReportsCountTextview.text = it.userUiState.cleanups.size.toString()
        binding.userJoinedDateTextview.text = it.userUiState.joinDate
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

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