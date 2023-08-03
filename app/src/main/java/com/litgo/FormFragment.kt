package com.litgo

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
// import com.example.conversion.ImageConversion.uriToBase64
import com.google.android.gms.maps.model.LatLng
import com.litgo.camera.CameraFragment
import com.litgo.data.models.LitterSiteCreation
import com.litgo.databinding.FragmentFormBinding

import com.litgo.viewModel.LitgoViewModel
import kotlinx.coroutines.launch

class FormFragment() : Fragment() {

    private val viewModel: LitgoViewModel by activityViewModels()
    private var _binding: FragmentFormBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private lateinit var userLocation: LatLng

    private lateinit var images: List<Uri>

    private lateinit var pickImage: ActivityResultLauncher<PickVisualMediaRequest>


    private fun Int.dpToPx(context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (this * density + 0.5f).toInt()
    }

    private fun addImageCards() {
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

        _binding = FragmentFormBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeState().collect { uiState ->
                    images = uiState.cameraUiState.imagesCaptured
                    val userState = uiState.userUiState
                    addImageCards()
                    userLocation = LatLng(userState.latitude, userState.longitude)
                }
            }
        }

        pickImage =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    viewModel.takePicture(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitButton.setOnClickListener { submitButtonClicked() }
        binding.deleteImagesButton.setOnClickListener { viewModel.clearPictures() }
        binding.imageButton.setOnClickListener {
            Log.i("Form Activity", "Launch PhotoPicker")
            getImages()
        }

    }


    private fun submitButtonClicked() {
        // val imageB64  = uriToBase64(images[0], requireContext())
        // if (imageB64 != null) {

        if (images.isEmpty()) {
            Toast.makeText(requireContext(), "You need one image!", Toast.LENGTH_SHORT).show()
            return
        }
            val litterSiteCreation = LitterSiteCreation(
                userLocation.latitude,
                userLocation.longitude,
                if (binding.toggleDanger.isChecked) "CAUTION" else "NONE",
                binding.descriptionText.text.toString(), /* */
                1,
                images[0].toString()
            )
            viewModel.createLitterSite(litterSiteCreation)

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            activity?.findViewById<TextView>(R.id.app_bar_title_textview)?.text = "Camera"
            transaction?.addToBackStack("Camera")
            transaction?.replace(R.id.nav_host_fragment_content_main, CameraFragment())
            transaction?.commit()

        // }
        Log.i("Form Activity", "Clicked")
    }

    private fun getImages() {
        if (images.isNotEmpty()) {
            Toast.makeText(requireContext(), "You can only have one image!", Toast.LENGTH_SHORT).show()
        } else {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}