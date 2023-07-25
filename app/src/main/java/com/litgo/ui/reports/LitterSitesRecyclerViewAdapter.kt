package com.litgo.ui.reports

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.litgo.R
import com.litgo.data.models.LitterSite
import com.litgo.databinding.FragmentLitterSiteBinding

/**
 * [RecyclerView.Adapter] that can display LitterSites that the user has reported
 * (that may or may not have been cleaned by them)
 */
class LitterSitesRecyclerViewAdapter(
    private val values: List<LitterSite>
) : RecyclerView.Adapter<LitterSitesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentLitterSiteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val litterSite = values[position]
        holder.descriptionTextView.text = litterSite.description
        holder.lastUpdatedTextView.text = litterSite.updatedAt
        holder.locationTextView.text = litterSite.longitude.toString() + ", " + litterSite.latitude.toString()
        // TODO: How to get the number of points associated with the cleanup?
        holder.pointsTextView.text = litterSite.description
        // TODO: Set the status icon for the report accordingly
        when (litterSite.isCollected) {
            true -> holder.statusImageView.drawable = Drawable(R.drawable.checkmark_90_green)
            else -> holder.statusImageView.drawable = Drawable(R.drawable.do_not_disturb_100_black)
        }
        // TODO: Show the first 1-2 images of the report
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentLitterSiteBinding) : RecyclerView.ViewHolder(binding.root) {
        var statusImageView: ImageView = binding.litterSiteStatusIconImageview
        var lastUpdatedTextView: TextView = binding.litterSiteLastUpdatedTextview
        val descriptionTextView: TextView = binding.litterSiteDescriptionTextview
        val pointsTextView: TextView = binding.litterSitePointsTextview
        val locationTextView: TextView = binding.litterSiteAddressTextview
        val photoImageView: ImageView = binding.litterSitePhotoImageview

        override fun toString(): String {
            return super.toString()
        }
    }

}