package com.litgo.ui.litterSite

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import com.example.litgotesting.viewModel.LitterSiteUiState
import com.litgo.MainActivity
import com.litgo.R
import com.litgo.databinding.FragmentLitterSiteItemBinding

/**
 * [RecyclerView.Adapter] that can display a [LitterSiteFragment] that the user has reported
 * (that may or may not have been cleaned by them)
 */
class LitterSitesRecyclerViewAdapter(
    private val values: List<LitterSiteUiState>,
    private val supportFragmentManager: FragmentManager?
) : RecyclerView.Adapter<LitterSitesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentLitterSiteItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val litterSite = values[position]
        holder.descriptionTextView.text = litterSite.description
//        holder.lastUpdatedTextView.text = litterSite.updated
        holder.locationTextView.text = litterSite.longitude.toString() + ", " + litterSite.latitude.toString()
        holder.pointsTextView.text = (litterSite.litterCount * 10).toString()
        when (litterSite.isCollected) {
            true -> holder.statusImageView.setImageResource(R.drawable.checkmark_90_green)
            false -> holder.statusImageView.setImageResource(R.drawable.do_not_disturb_100_black)
        }
        holder.photoImageView.setImageURI(Uri.parse(litterSite.image))
        // TODO: Show the first 1-2 images of the report
        holder.litterSiteItemLayout.setOnClickListener {
            val transaction = supportFragmentManager?.beginTransaction()
            transaction?.addToBackStack("litter_sites_fragment")
            transaction?.replace(R.id.nav_host_fragment_content_main, LitterSiteFragment(litterSite))
            transaction?.commit()
        }

        holder.harmTextView.apply {
            text = litterSite.harm
            when (litterSite.harm) {
                "CAUTION" -> {
                    visibility = View.VISIBLE
//                    setBackgroundColor(resources.getColor(R.color.lightest_yellow))
                    setTextColor(resources.getColor(R.color.darker_yellow))
                    background = resources.getDrawable(R.drawable.label_yellow)
                }
                "HAZARDOUS" -> {
                    visibility = View.VISIBLE
//                    setBackgroundColor(resources.getColor(R.color.lighter_red))
                    setTextColor(resources.getColor(R.color.dark_red))
                    background = resources.getDrawable(R.drawable.label_red)
                }
                else -> visibility = View.GONE
            }
        }

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentLitterSiteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var statusImageView: ImageView = binding.litterSiteStatusIconImageview
        var lastUpdatedTextView: TextView = binding.litterSiteLastUpdatedTextview
        val descriptionTextView: TextView = binding.litterSiteDescriptionTextview
        val pointsTextView: TextView = binding.litterSitePointsTextview
        val locationTextView: TextView = binding.litterSiteAddressTextview
        val photoImageView: ImageView = binding.litterSitePhotoImageview
        var harmTextView: TextView = binding.litterSiteHarmTextview
        val litterSiteItemLayout: LinearLayout = binding.litterSiteLayout
    }

}