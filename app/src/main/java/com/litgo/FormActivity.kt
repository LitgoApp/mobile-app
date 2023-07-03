package com.litgo

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.compose.ui.unit.dp
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import java.io.File


class FormActivity : AppCompatActivity() {

    private fun Int.dpToPx(context: Context): Int {
        val density = context.resources.displayMetrics.density
        return (this * density + 0.5f).toInt()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form_view)

        val directoryPath = "C:\\Users\\mapun\\Pictures\\Screenshots" // THIS IS A DUMMY, UPDATE ONCE YOU GET A DIRECTORY
        //val directory = File(directoryPath)

        /*
        var fileCount = 0

        if (directory.exists() && directory.isDirectory) {
            val files = directory.listFiles()

            if (files != null) {
                fileCount = files.size
            }
        } else {
            Log.e("Form Activity", "Invalid directory path")
        }
         */

        val parentLayout = findViewById<LinearLayout>(R.id.cardHolder)

        for (i in 0 until 2) { // dummy
            val newCardView = CardView(this)

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, 0, 8.dpToPx(this), 0)
            newCardView.layoutParams = layoutParams

            newCardView.radius = 15f
            newCardView.cardElevation = 8f

            val imageView = ImageView(this)

            imageView.layoutParams = LinearLayout.LayoutParams(
                100.dpToPx(this), LinearLayout.LayoutParams.MATCH_PARENT
            )

            imageView.scaleType = ImageView.ScaleType.CENTER
            imageView.setImageResource(R.drawable.ic_launcher_background)

            newCardView.addView(imageView)
            parentLayout.addView(newCardView)
        }

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
