package com.litgo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.litgo.data.dataSources.DisposalSiteRemoteDataSource
import com.litgo.data.dataSources.LitterSiteRemoteDataSource
import com.litgo.data.dataSources.MunicipalityRemoteDataSource
import com.litgo.data.dataSources.RegionRemoteDataSource
import com.litgo.data.dataSources.RewardRemoteDataSource
import com.litgo.data.dataSources.UserRemoteDataSource
import com.litgo.data.dataSources.retrofit.DisposalSiteRetrofitApi
import com.litgo.data.dataSources.retrofit.LitterSiteRetrofitApi
import com.litgo.data.dataSources.retrofit.MunicipalityRetrofitApi
import com.litgo.data.dataSources.retrofit.RegionRetrofitApi
import com.litgo.data.dataSources.retrofit.RewardRetrofitApi
import com.litgo.data.dataSources.retrofit.UserRetrofitApi
import com.litgo.data.repositories.DisposalSiteRepository
import com.litgo.data.repositories.LitterSiteRepository
import com.litgo.data.repositories.MunicipalityRepository
import com.litgo.data.repositories.RegionRepository
import com.litgo.data.repositories.RewardRepository
import com.litgo.data.repositories.UserRepository
import com.litgo.databinding.ActivityMainBinding
import com.litgo.ui.RewardsFragment
import com.litgo.ui.profile.UserProfileFragment
import com.litgo.ui.reports.ReportsFragment
import com.litgo.viewModel.LitterSiteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://172.23.176.1:3001/%22")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val userRepo: UserRepository = UserRepository(
        UserRemoteDataSource(UserRetrofitApi(retrofit), Dispatchers.IO)
    )

    private val municipalityRepo: MunicipalityRepository = MunicipalityRepository(
        MunicipalityRemoteDataSource(MunicipalityRetrofitApi(retrofit), Dispatchers.IO)
    )

    private val litterSiteRepo: LitterSiteRepository = LitterSiteRepository(
        LitterSiteRemoteDataSource(LitterSiteRetrofitApi(retrofit), Dispatchers.IO)
    )

    private val regionRepo: RegionRepository = RegionRepository(
        RegionRemoteDataSource(RegionRetrofitApi(retrofit), Dispatchers.IO)
    )

    private val rewardRepo: RewardRepository = RewardRepository(
        RewardRemoteDataSource(RewardRetrofitApi(retrofit), Dispatchers.IO)
    )

    private val disposalSiteRepo: DisposalSiteRepository = DisposalSiteRepository(
        DisposalSiteRemoteDataSource(DisposalSiteRetrofitApi(retrofit), Dispatchers.IO)
    )

    private val viewModel: LitterSiteViewModel = LitterSiteViewModel(
        userRepo,
        municipalityRepo,
        litterSiteRepo,
        regionRepo,
        rewardRepo,
        disposalSiteRepo
    )

    override fun onCreate(savedInstanceState: Bundle?) {
//        WindowCompat.setDecorFitsSystemWindows(window, false)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                //viewModel.uiState.collect {
                // Update UI elements
//                }
            }
        }

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val appBarTitleTextView = findViewById<TextView>(R.id.app_bar_title_textview)

        // Add actions to navbar icons
        binding.cameraNavBtn.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment_content_main, CameraFragment())
            transaction.commit()
        }
        binding.mapNavBtn.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.commit()
        }
        binding.userProfileNavBtn.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment_content_main, UserProfileFragment())
            transaction.commit()
            appBarTitleTextView.text = "My Profile"
        }
        binding.userReportsNavBtn.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment_content_main, ReportsFragment())
            transaction.commit()
            appBarTitleTextView.text = "My Reports and Cleanups"
        }
        binding.rewardsNavBtn.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment_content_main, RewardsFragment())
            transaction.commit()
            appBarTitleTextView.text = "My Rewards"
        }
    }

    /**
     * DO NOT REMOVE BLOCK BELOW FOR NOW; May be required later
     */
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    /**
     * Handle scrolling up and down navigation
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }

}