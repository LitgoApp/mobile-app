package com.litgo.ui.reports

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.litgotesting.viewModel.LitterSiteUiState
import com.litgo.R
import com.litgo.databinding.FragmentLitterSiteItemBinding

/**
 * [RecyclerView.Adapter] that can display LitterSites that the user has reported
 * (that may or may not have been cleaned by them)
 */
class LitterSitesRecyclerViewAdapter(
    private val values: List<LitterSiteUiState>
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
        holder.cleanupPointsTextView.text = (litterSite.litterCount * 10).toString()
        holder.reportPointsTextView.text = (litterSite.litterCount * 3).toString()
        when (litterSite.isCollected) {
            true -> holder.statusImageView.setImageResource(R.drawable.checkmark_90_green)
            false -> holder.statusImageView.setImageResource(R.drawable.do_not_disturb_100_black)
        }
        holder.photoImageView.setImageURI(Uri.parse(litterSite.image))
        // TODO: Show the first 1-2 images of the report
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentLitterSiteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var statusImageView: ImageView = binding.litterSiteStatusIconImageview
        var lastUpdatedTextView: TextView = binding.litterSiteLastUpdatedTextview
        val descriptionTextView: TextView = binding.litterSiteDescriptionTextview
        val cleanupPointsTextView: TextView = binding.litterSitePointsTextview
        val reportPointsTextView: TextView = binding.litterSitePointsTextview
        val locationTextView: TextView = binding.litterSiteAddressTextview
        val photoImageView: ImageView = binding.litterSitePhotoImageview

        override fun toString(): String {
            return super.toString()
        }
    }

}