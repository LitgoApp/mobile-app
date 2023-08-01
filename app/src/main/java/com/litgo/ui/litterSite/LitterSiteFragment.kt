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
import com.litgo.data.models.Coordinates
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

        // Set the litter site icon for whether it has been collected
        binding.litterSiteStatusIconImageview.setImageResource(
            when (litterSiteUiState.isCollected) {
                true -> R.drawable.checkmark_90_green
                false -> R.drawable.do_not_disturb_100_black
            }
        )
        // Set the text color for the litter site harm level
        binding.litterSiteHarmTextview.apply {
            text = litterSiteUiState.harm
            when (litterSiteUiState.harm) {
                "CAUTION" -> {
                    visibility = View.VISIBLE
                    setTextColor(resources.getColor(R.color.darker_yellow))
                    background = resources.getDrawable(R.drawable.label_yellow)
//                    setBackgroundColor(resources.getColor(R.color.lightest_yellow))
                }

                "HAZARDOUS" -> {
                    visibility = View.VISIBLE
                    setTextColor(resources.getColor(R.color.dark_red))
                    background = resources.getDrawable(R.drawable.label_red)
//                    setBackgroundColor(resources.getColor(R.color.lighter_red))
                }

                else -> visibility = View.GONE
            }
        }

        binding.litterSiteDescriptionTextview.text = litterSiteUiState.description
        binding.litterSitePointsTextview.text = (litterSiteUiState.litterCount * 10).toString()
        binding.litterSiteCoordinatesTextview.text =
            litterSiteUiState.longitude.toString() + ", " + litterSiteUiState.latitude.toString()
        binding.litterSiteReportDateTextview.text =
            resources.getString(R.string.litter_site_reported_textview_text) + resources.getString(R.string.test_litter_site_reported_date)

        binding.backTextview.setOnClickListener {
            // Pop the current view off the stack to the previous view
            activity?.supportFragmentManager?.popBackStack()
            showAppAndNavBar()
        }

        // TODO: Show the first 1-2 images of the report

        binding.litterSiteCompletedButton.apply {
            setOnClickListener {
                viewModel.cleanLitterSite(litterSiteUiState.id, Coordinates(0.0, 0.0))
                viewModel.deleteLitterSite(litterSiteUiState.id)
                navigateBack()
            }
        }

        binding.litterSiteDeclineButton.apply {
            setOnClickListener {
                navigateBack()
            }
        }

        binding.litterSiteActionsLayout.apply {
            visibility =
                if (litterSiteUiState.reportingUserId == viewModel.uiState.value.userUiState.id
                    || litterSiteUiState.collectingUserId == viewModel.uiState.value.userUiState.id
                ) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
        }

    }

    private fun showAppAndNavBar() {
        activity?.findViewById<AppBarLayout>(R.id.app_bar_layout)?.apply {
            visibility = View.VISIBLE
        }
        activity?.findViewById<BottomNavigationView>(R.id.nav_bottom)?.apply {
            visibility = View.VISIBLE
        }
    }

    private fun hideAppAndNavBar() {
        activity?.findViewById<AppBarLayout>(R.id.app_bar_layout)?.apply {
            visibility = View.GONE
        }
        activity?.findViewById<BottomNavigationView>(R.id.nav_bottom)?.apply {
            visibility = View.GONE
        }
    }

    private fun navigateBack() {
        activity?.supportFragmentManager?.popBackStack()
        showAppAndNavBar()
    }
}