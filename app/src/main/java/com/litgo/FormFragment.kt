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
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class FormFragment(private val images: MutableList<Uri> /* temp */) : Fragment() {

    private fun Int.dpToPx(context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (this * density + 0.5f).toInt()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_form, container, false)
        // Customize your form interactions and logic here

        val parentLayout = view.findViewById<LinearLayout>(R.id.cardHolder)

        for (uri in images) {
            val newCardView = CardView(requireContext())

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, 0, 8.dpToPx(requireContext()), 0)
            newCardView.layoutParams = layoutParams

            newCardView.radius = 15f
            newCardView.cardElevation = 8f

            val imageView = ImageView(requireContext())

            imageView.layoutParams = LinearLayout.LayoutParams(
                100.dpToPx(requireContext()), LinearLayout.LayoutParams.MATCH_PARENT
            )

            imageView.scaleType = ImageView.ScaleType.CENTER
            imageView.setImageURI(uri)

            newCardView.addView(imageView)
            parentLayout.addView(newCardView)
        }

        return view
    }

    fun btnClicked(view: View) {
        when(view.id) {
            R.id.submitButton -> {
                // HANDLE SUBMIT BUTTON PRESS
                Log.i("Form Activity", "Clicked")
            }
        }
    }
}
