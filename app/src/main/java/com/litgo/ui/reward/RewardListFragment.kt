package com.litgo.ui.reward

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.litgotesting.viewModel.LitgoUiState
import com.example.litgotesting.viewModel.RewardUiState
import com.litgo.R
import com.litgo.databinding.FragmentRewardListBinding
import com.litgo.viewModel.LitgoViewModel
import kotlinx.coroutines.launch

class RewardListFragment : Fragment() {

    private val viewModel: LitgoViewModel by activityViewModels()
    private var _binding: FragmentRewardListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRewardListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.observeState().collect {
                renderState(it)
            }
        }
    }

    private fun renderState(it: LitgoUiState) {
        viewModel.getEligibleRewards()

        binding.userPointsTextview.text = it.userUiState.points.toString()
        // TESTING
//        binding.rewardRecyclerView.adapter = RewardRecyclerViewAdapter(viewModel, testRewards(), activity?.supportFragmentManager)
        binding.rewardRecyclerView.adapter = RewardRecyclerViewAdapter(viewModel, it.userUiState.eligibleRewards, activity?.supportFragmentManager)
    }

    /**
     * THIS IS A METHOD FOR TESTING PURPOSES
     * Generates a list of eligible rewards
     */
    private fun testRewards(): List<RewardUiState> {
        return listOf(
            RewardUiState(
                "",
                "$10.00 Starbucks Gift Card",
                250,
                "Enjoy your favourite beverage, on us!"
            ),
            RewardUiState(
                "",
                "$5 Tim Hortons Gift Card",
                150,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Elementum sagittis vitae et leo duis. Aliquet eget sit amet tellus cras adipiscing. Sed felis eget velit aliquet sagittis id consectetur. Scelerisque viverra mauris in aliquam. Nunc congue nisi vitae suscipit tellus mauris. Enim nunc faucibus a pellentesque sit amet. Egestas erat imperdiet sed euismod. Id velit ut tortor pretium viverra suspendisse potenti. Eu non diam phasellus vestibulum lorem. In dictum non consectetur a erat nam at. Odio ut sem nulla pharetra diam sit amet nisl. Et netus et malesuada fames ac.\n" +
                        "Nullam eget felis eget nunc lobortis mattis. Sed lectus vestibulum mattis ullamcorper velit sed ullamcorper morbi tincidunt. Nulla aliquet porttitor lacus luctus accumsan tortor posuere ac. Urna neque viverra justo nec ultrices dui. Aliquam ut porttitor leo a diam sollicitudin. Facilisis volutpat est velit egestas dui id ornare arcu odio. Vulputate eu scelerisque felis imperdiet proin. Massa sapien faucibus et molestie ac feugiat. Elit sed vulputate mi sit. Tellus at urna condimentum mattis pellentesque id nibh tortor. Consequat interdum varius sit amet. Aliquam sem et tortor consequat. Facilisi nullam vehicula ipsum a arcu cursus vitae congue mauris. Lacus laoreet non curabitur gravida arcu ac tortor dignissim convallis. Accumsan lacus vel facilisis volutpat est velit egestas. Facilisi cras fermentum odio eu feugiat pretium nibh. At lectus urna duis convallis convallis tellus. Aliquam purus sit amet luctus.\n" +
                        "Vitae proin sagittis nisl rhoncus mattis."
            ),
            RewardUiState(
                "",
                "10% Off Your Next Purchase at Domino's",
                200,
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Elementum sagittis vitae et leo duis. Aliquet eget sit amet tellus cras adipiscing. Sed felis eget velit aliquet sagittis id consectetur. Scelerisque viverra mauris in aliquam. Nunc congue nisi vitae suscipit tellus mauris. Enim nunc faucibus a pellentesque sit amet. Egestas erat imperdiet sed euismod. Id velit ut tortor pretium viverra suspendisse potenti. Eu non diam phasellus vestibulum lorem. In dictum non consectetur a erat nam at. Odio ut sem nulla pharetra diam sit amet nisl. Et netus et malesuada fames ac.\n" +
                        "Nullam eget felis eget nunc lobortis mattis. Sed lectus vestibulum mattis ullamcorper velit sed ullamcorper morbi tincidunt. Nulla aliquet porttitor lacus luctus accumsan tortor posuere ac. Urna neque viverra justo nec ultrices dui. Aliquam ut porttitor leo a diam sollicitudin. Facilisis volutpat est velit egestas dui id ornare arcu odio. Vulputate eu scelerisque felis imperdiet proin. Massa sapien faucibus et molestie ac feugiat. Elit sed vulputate mi sit. Tellus at urna condimentum mattis pellentesque id nibh tortor. Consequat interdum varius sit amet. Aliquam sem et tortor consequat. Facilisi nullam vehicula ipsum a arcu cursus vitae congue mauris. Lacus laoreet non curabitur gravida arcu ac tortor dignissim convallis. Accumsan lacus vel facilisis volutpat est velit egestas. Facilisi cras fermentum odio eu feugiat pretium nibh. At lectus urna duis convallis convallis tellus. Aliquam purus sit amet luctus.\n" +
                        "Vitae proin sagittis nisl rhoncus mattis. Tortor aliquam nulla facilisi cras fermentum odio eu feugiat pretium. Sem et tortor consequat id. Imperdiet dui accumsan sit amet nulla facilisi morbi tempus. Magna fringilla urna porttitor rhoncus dolor purus non. Hac habitasse platea dictumst quisque sagittis purus sit. Tristique risus nec feugiat in fermentum. Integer malesuada nunc vel risus. Nec tincidunt praesent semper feugiat nibh sed pulvinar. Libero id faucibus nisl tincidunt eget nullam non nisi. Id cursus metus aliquam eleifend mi in.\n" +
                        "Nullam non nisi est sit amet facilisis magna etiam tempor. Quisque egestas diam in arcu. Sit amet purus gravida quis blandit turpis cursus in hac. Scelerisque felis imperdiet proin fermentum leo vel orci. Sagittis nisl rhoncus mattis rhoncus urna neque viverra justo. Sit amet risus nullam eget felis eget nunc. Fermentum odio eu feugiat pretium. Ornare quam viverra orci sagittis eu volutpat. Leo urna molestie at elementum eu facilisis sed odio morbi. Integer feugiat scelerisque varius morbi enim nunc faucibus a pellentesque. Vestibulum sed arcu non odio euismod. Sit amet consectetur adipiscing elit pellentesque habitant morbi tristique senectus. Diam vel quam elementum pulvinar etiam non.\n" +
                        "Eget magna fermentum iaculis eu non diam."
            ),
            RewardUiState(
                "",
                "$15.00 Starbucks Gift Card",
                500,
                ""
            )
        )
    }

}