package com.litgo

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.litgo.databinding.FragmentFormBinding
import com.litgo.viewModel.LitterSiteViewModel
import kotlinx.coroutines.launch

class FormFragment() : Fragment() {

    private lateinit var images: MutableList<Uri>
    private val viewModel: LitterSiteViewModel by viewModels()

    private fun Int.dpToPx(context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (this * density + 0.5f).toInt()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        fun onUpdate() {

            val view = inflater.inflate(R.layout.fragment_form, container, false)

            val parentLayout = view.findViewById<LinearLayout>(R.id.cardHolder)
            parentLayout.removeAllViews()

            for (uri in images) {
                val cardView = CardView(requireContext())

                val layoutParams = LinearLayout.LayoutParams(
                    90.dpToPx(requireContext()),
                    120.dpToPx(requireContext())
                )

                layoutParams.marginEnd = 16.dpToPx(requireContext())
                cardView.layoutParams = layoutParams
                cardView.cardElevation = 8.dpToPx(requireContext()).toFloat()
                cardView.radius = 15.dpToPx(requireContext()).toFloat()

                val imageView = ImageView(requireContext())
                imageView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                imageView.setImageURI(uri)
                cardView.addView(imageView)
                parentLayout.addView(cardView)
            }

        }

        onUpdate()

        val viewBinding = FragmentFormBinding.inflate(inflater, container, false)
        viewBinding.submitButton.setOnClickListener { submitButtonClicked() }
        viewBinding.addImage.setOnClickListener { getImages() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    TODO("Update UI elements based on the uiState")
                }
            }
        }

        return view
    }


    fun submitButtonClicked() {
        // HANDLE SUBMIT BUTTON PRESS
        Log.i("Form Activity", "Clicked")
    }

    fun getImages(): List<Uri> {
        // Registers a photo picker activity launcher in multi-select mode.
        // In this example, the app lets the user select up to 5 media files.
        var imageUris: List<Uri> = emptyList()

        val pickMultipleMedia =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
                // Callback is invoked after the user selects media items or closes the
                // photo picker.
                if (uris.isNotEmpty()) {
                    imageUris = uris
                    Log.d("PhotoPicker", "Number of items selected: ${uris.size}")
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

        return imageUris
    }


}
