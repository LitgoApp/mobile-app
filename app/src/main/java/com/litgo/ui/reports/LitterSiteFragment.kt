package com.litgo.ui.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.litgo.R
import com.litgo.databinding.FragmentLitterSiteBinding
import com.litgo.viewModel.LitterSiteViewModel

/**
 * Displays the details of a litter site (which may or may not have been cleaned already).
 * Fragment created when the user presses a report shown on the map, or when the user
 * views their existing reports / cleanups.
 */
class LitterSiteFragment : Fragment() {

    private val viewModel: LitterSiteViewModel by activityViewModels()
    private var _binding: FragmentLitterSiteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_litter_site_item, container, false)
    }
}