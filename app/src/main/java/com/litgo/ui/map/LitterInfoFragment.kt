package com.litgo.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.litgo.databinding.FragmentLitterSiteInfoBinding
import com.litgo.viewModel.LitterSiteViewModel
import kotlinx.coroutines.launch

class LitterSiteInfoFragment : Fragment() {
    private val viewModel: LitterSiteViewModel by viewModels()
    private lateinit var binding: FragmentLitterSiteInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLitterSiteInfoBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.litterSiteCard.setOnClickListener {
            TODO("Nav to report details page")
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    val litterSite = uiState.mapUiState.litterSiteSelected
                    if (litterSite != null) {
                        binding.harmTextView.text = litterSite?.harm
                        binding.descriptionTextView.text = litterSite.description
                        binding.dateTimeTextView.text = litterSite.createdAt
                    }
                }
            }
        }
    }
}
