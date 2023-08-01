package com.litgo.ui.reward

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.FragmentManager
import com.example.litgotesting.viewModel.RewardUiState
import com.litgo.R
import com.litgo.databinding.FragmentRewardItemBinding
import com.litgo.viewModel.LitgoViewModel

/**
 * [RecyclerView.Adapter] that can display a [RewardFragment] that can be / has been claimed by the
 * user.
 */
class RewardRecyclerViewAdapter(
    private val viewModel: LitgoViewModel,
    private val values: List<RewardUiState>,
    private val supportFragmentManager: FragmentManager?
) : RecyclerView.Adapter<RewardRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentRewardItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reward = values[position]
        holder.rewardNameTextView.text = reward.name
        holder.rewardDescriptionTextView.text = reward.description
        holder.rewardPointsTextView.text = reward.cost.toString()
        holder.rewardExpiryDateTextView.text = "Offer valid through "

        holder.rewardLayout.setOnClickListener {
            // Navigate to different screen for user to confirm reward claim
//            val transaction = supportFragmentManager?.beginTransaction()
//            transaction?.addToBackStack("reward_fragment")
//            transaction?.replace(R.id.nav_host_fragment_content_main, RewardFragment(reward))
//            transaction?.commit()
            // Hide/Show the redemption and decline buttons for the reward
            toggleRewardActionsLayout(holder)
        }

        holder.rewardRedeemButton.setOnClickListener {
            viewModel.redeemReward(reward.id)
        }
        holder.rewardDeclineButton.setOnClickListener {
            toggleRewardActionsLayout(holder)
        }

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentRewardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val rewardNameTextView: TextView = binding.rewardNameTextview
        val rewardDescriptionTextView: TextView = binding.rewardDescriptionTextview
        val rewardExpiryDateTextView: TextView = binding.rewardExpiryDateTextview
        val rewardPointsTextView: TextView = binding.rewardPointsTextview
        val rewardLayout: LinearLayout = binding.rewardLayout
        val rewardRedeemButton: AppCompatButton = binding.rewardRedeemButton
        val rewardDeclineButton: AppCompatButton = binding.rewardDeclineButton
        val rewardActionsLayout: LinearLayout = binding.rewardActionsLayout
    }

    private fun toggleRewardActionsLayout(holder: ViewHolder) {
        when (holder.rewardActionsLayout.visibility) {
            View.GONE -> holder.rewardActionsLayout.visibility = View.VISIBLE
            else -> holder.rewardActionsLayout.visibility = View.GONE
        }
    }

}