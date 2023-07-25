package com.litgo.ui.reports

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.litgo.data.models.LitterSite
import com.litgo.databinding.FragmentLitterSiteBinding

/**
 * [RecyclerView.Adapter] that can display LitterSites that the user has reported
 * (that may or may not have been cleaned by them)
 */
class LitterReportsRecyclerViewAdapter(
    private val values: List<LitterSite>
) : RecyclerView.Adapter<LitterReportsRecyclerViewAdapter.ViewHolder>() {

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
        litterSite.
//        // TODO: Set the status icon for the report accordingly
//        holder.dateCreatedView.text = report.date_created
//        holder.descriptionView.text = report.description
//        // TODO: Show as many points icons as deemed appropriate by the app
//        holder.locationView.text = report.location
//        // TODO: Show the first 1-2 images of the report
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentLitterSiteBinding) : RecyclerView.ViewHolder(binding.root) {
        val statusImageView: ImageView = binding.litterSiteStatusIconImageview
        val lastUpdatedTextView: TextView = binding.litterSiteLastUpdatedTextview
        val descriptionTextView: TextView = binding.litterSiteDescriptionTextview
        val pointsTextView: TextView = binding.litterSitePointsTextview
        val locationTextView: TextView = binding.litterSiteAddressTextview
        val photoImageView: ImageView = binding.litterSitePhotoImageview

        override fun toString(): String {
            return ""
//            return super.toString() + " '" + descriptionView.text + "'"
        }
    }

}