package com.litgo.ui.reports

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.litgo.ui.reports.PlaceholderContent.PlaceholderItem
import com.litgo.databinding.FragmentReportItemBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class ReportRecyclerViewAdapter(
    private val values: List<PlaceholderItem>
) : RecyclerView.Adapter<ReportRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentReportItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val report = values[position]

//        // TODO: Set the status icon for the report accordingly
//        holder.dateCreatedView.text = report.date_created
//        holder.descriptionView.text = report.description
//        // TODO: Show as many points icons as deemed appropriate by the app
//        holder.locationView.text = report.location
//        // TODO: Show the first 1-2 images of the report
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentReportItemBinding) : RecyclerView.ViewHolder(binding.root) {
//        val statusView: ImageView = binding.reportStatusImageviewIcon
//        val dateCreatedView: TextView = binding.reportDateCreatedTextview
//        val descriptionView: TextView = binding.reportDescriptionTextview
//        val pointsView: ImageView = binding.reportPointsImageview
//        val locationView: TextView = binding.reportLocationTextview
//        val imagesView: ImageView = binding.reportImageview

        override fun toString(): String {
            return ""
//            return super.toString() + " '" + descriptionView.text + "'"
        }
    }

}