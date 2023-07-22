package com.litgo.ui.map

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.litgo.databinding.FragmentLitterSiteInfoBinding
import com.litgo.viewModel.LitterSiteViewModel

class LitterSiteInfoFragment : Fragment() {
    private lateinit var litterSiteViewModel: LitterSiteViewModel
    private var _binding: FragmentLitterSiteInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLitterSiteInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        litterSiteViewModel = ViewModelProvider(requireActivity()).get(LitterSiteViewModel::class.java)

        litterSiteViewModel.selectedLitterSite.observe(viewLifecycleOwner) { litterSite ->
            // TO BE CHANGED
            binding.harmTextView.text = litterSite.harm
            binding.descriptionTextView.text = litterSite.description
            // binding.litterCountTextView.text = litterSite.litterCount.toString() not sure what this is
            binding.locationTextView.text = TODO("Format location")

            binding.litterSiteCard.setOnClickListener {
                TODO("Switch to report details page")
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
