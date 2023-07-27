package com.litgo.ui.reward

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.litgotesting.viewModel.RewardUiState
import com.litgo.databinding.FragmentRewardBinding

/**
 * [RecyclerView.Adapter] that can display a [RewardFragment] that can be / has been claimed by the
 * user.
 */
class RewardRecyclerViewAdapter(
    private val values: List<RewardUiState>
) : RecyclerView.Adapter<RewardRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentRewardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reward = values[position]
//        holder.idView.text = item.id
//        holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentRewardBinding) :
        RecyclerView.ViewHolder(binding.root) {
//        val idView: TextView = binding.itemNumber
//        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString()
//            return super.toString() + " '" + contentView.text + "'"
        }
    }

}