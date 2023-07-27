package com.litgo.ui.reward

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.litgotesting.viewModel.RewardUiState
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.litgo.R

/**
 * Displays full details of a reward (that may or may not be/have been claimed by the user).
 * Fragment created when user presses on a reward item on the "My Rewards" screen, or when user
 * presses on a reward item when viewing past claimed rewards.
 */
class RewardFragment(
    private val rewardUiState: RewardUiState
) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideAppAndNavBar()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reward, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
}