package com.litgo

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
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
import com.litgo.viewModel.LitterSiteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {


    lateinit var viewModel: LitterSiteViewModel
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                //viewModel.uiState.collect {
                    // Update UI elements
                }
            }



        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAnchorView(R.id.fab)
//                .setAction("Action", null).show()
//        }


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
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}