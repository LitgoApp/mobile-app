package com.litgo.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.litgo.R
import com.litgo.databinding.FragmentUserProfileBinding
import com.litgo.viewModel.LitterSiteViewModel

class UserProfileFragment : Fragment() {
    private val viewModel: LitterSiteViewModel by activityViewModels()
    private var _binding: FragmentUserProfileBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

//    companion object {
//        fun newInstance() = UserProfileFragment()
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}