package com.litgo.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.litgotesting.viewModel.MapUiState
import com.litgo.databinding.FragmentLitterSiteInfoBinding
import com.litgo.viewModel.LitgoViewModel

class LitterSiteInfoFragment : Fragment() {
    private lateinit var binding: FragmentLitterSiteInfoBinding
    private lateinit var litterSiteViewModel: LitgoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLitterSiteInfoBinding.inflate(layoutInflater)
    }


    fun updateFromUiState(state: MapUiState) {
        val litterSite = state.currentlySelectedLitterSite
        if (litterSite != null) {
            binding.harmTextView.text = litterSite?.harm
            binding.descriptionTextView.text = litterSite.description
            binding.dateTimeTextView.text = litterSite.createdAt
        }
    }
}
