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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.model.LatLng
import com.litgo.data.models.LitterSiteCreation
import com.litgo.databinding.FragmentFormBinding

import com.litgo.viewModel.LitgoViewModel
import kotlinx.coroutines.launch

class FormFragment() : Fragment() {

    private val viewModel: LitgoViewModel by viewModels()
    private var _binding: FragmentFormBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private lateinit var pickMultipleMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var userLocation: LatLng
    private var newImageUris: List<Uri> = emptyList()

    private lateinit var images: MutableList<Uri>


    private fun Int.dpToPx(context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (this * density + 0.5f).toInt()
    }

    private fun addImageCards(images: List<Uri>) {
        val parentLayout = binding.cardHolder
        parentLayout.removeAllViews()

        if (images != null) {
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
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    val images = uiState.cameraUiState.imagesCaptured
                    val userState = uiState.userUiState
                    // addImageCards(images) TODO: Uncomment when Michael pushes
                    userLocation = LatLng(userState.latitude, userState.longitude)
                }
            }
        }

        pickMultipleMedia =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
                // Callback is invoked after the user selects media items or closes the
                // photo picker.

                if (uris.isNotEmpty()) {
                    // This currently only makes the image picker select the images that you want to display
                    // TODO: Make sure this is working when Michael pushes!
                    for (uri in uris) {
                        // viewModel.addImageUri(uri)
                    }
                    addImageCards(uris)
                    Log.d("PhotoPicker", "Number of items selected: ${uris.size}")
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        _binding = FragmentFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitButton.setOnClickListener { submitButtonClicked() }
        binding.imageButton.setOnClickListener {
            Log.i("Form Activity", "Launch PhotoPicker")
            getImages()
        }

    }


    private fun submitButtonClicked() {
        val litterSiteCreation = LitterSiteCreation(
            userLocation.latitude,
            userLocation.longitude,
            if (binding.toggleDanger.isChecked) "Danger" else "Test",
            binding.descriptionText.text.toString(), /**/
            1,
            "dummy"
        )
        viewModel.createLitterSite(litterSiteCreation)

        // HANDLE SUBMIT BUTTON PRESS
        Log.i("Form Activity", "Clicked")
    }

    private fun getImages() {
        // Registers a photo picker activity launcher in multi-select mode.
        // In this example, the app lets the user select up to 5 media files.
        pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
