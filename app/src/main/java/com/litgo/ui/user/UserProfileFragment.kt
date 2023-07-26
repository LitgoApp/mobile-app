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
        val mainActivityLayout = activity?.findViewById<ConstraintLayout>(R.id.main_activity_layout)

        mainActivityLayout?.setBackgroundColor(resources.getColor(R.color.white))
        // Ensure the bottom navigation bar and top app bar are not visible
//        mainActivity.hideAppAndNavBars()
        val appBarLayout = activity?.findViewById<AppBarLayout>(R.id.app_bar_layout)
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_bottom)
        appBarLayout?.visibility = View.VISIBLE
        navBar?.visibility = View.VISIBLE

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
                "",
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
                resources.getString(R.string.test_lorem_ipsum),
                -33.868820,
                151.209290
            ),
        )
        binding.userRecentActivityRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.userRecentActivityRecyclerView.adapter = LitterSitesRecyclerViewAdapter(testReports, activity?.supportFragmentManager)
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
}