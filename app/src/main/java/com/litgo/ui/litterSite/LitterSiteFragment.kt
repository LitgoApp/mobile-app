package com.litgo.ui.litterSite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.litgotesting.viewModel.LitterSiteUiState
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.litgo.R
import com.litgo.databinding.FragmentLitterSiteBinding
import com.litgo.viewModel.LitgoViewModel

/**
 * Displays the full details of a litter site (which may or may not have been cleaned already).
 * Fragment created when the user presses a report shown on the map, or when the user
 * views their existing reports / cleanups.
 */
class LitterSiteFragment(
    private val litterSiteUiState: LitterSiteUiState
) : Fragment() {

    private val viewModel: LitgoViewModel by activityViewModels()

    private var _binding: FragmentLitterSiteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLitterSiteBinding.inflate(inflater, container, false)

        // Ensure the the app bar and bottom nav bar are not visible
        hideAppAndNavBar()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (litterSiteUiState.isCollected) {
            true -> binding.litterSiteStatusIconImageview.setImageResource(R.drawable.checkmark_90_green)
            false -> binding.litterSiteStatusIconImageview.setImageResource(R.drawable.do_not_disturb_100_black)
        }
        binding.litterSiteDescriptionTextview.text = litterSiteUiState.description
        binding.litterSitePointsTextview.text = (litterSiteUiState.litterCount * 10).toString()
        binding.litterSiteCoordinatesTextview.text = litterSiteUiState.longitude.toString() + ", " + litterSiteUiState.latitude.toString()
        binding.litterSiteReportDateTextview.text = litterSiteUiState.reportingUserId.toString()

        binding.backTextview.setOnClickListener {
            // Pop the current view off the stack to the previous view
            activity?.supportFragmentManager?.popBackStack()
            showAppAndNavBar()
        }

        // TODO: Show the first 1-2 images of the report

    }

    private fun showAppAndNavBar() {
        val appBarLayout = activity?.findViewById<AppBarLayout>(R.id.app_bar_layout)
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_bottom)
        appBarLayout?.visibility = View.VISIBLE
        navBar?.visibility = View.VISIBLE
    }

    private fun hideAppAndNavBar() {
        val appBarLayout = activity?.findViewById<AppBarLayout>(R.id.app_bar_layout)
        val navBar = activity?.findViewById<BottomNavigationView>(R.id.nav_bottom)
        appBarLayout?.visibility = View.GONE
        navBar?.visibility = View.GONE
    }
}