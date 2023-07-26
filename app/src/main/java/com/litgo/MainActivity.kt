package com.litgo

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.litgo.databinding.ActivityMainBinding
import com.litgo.ui.reward.RewardsFragment
import com.litgo.ui.user.UserProfileFragment
import com.litgo.ui.litterSite.LitterSiteReportsFragment
import com.litgo.ui.map.MapFragment
import com.litgo.viewModel.LitgoViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val viewModel: LitgoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        hideAppAndNavBars()

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {

                }
            }
        }

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val appBarTitleTextView = findViewById<TextView>(R.id.app_bar_title_textview)

        // Add actions to navbar icons
        binding.cameraNavBtn.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment_content_main, CameraFragment())
            transaction.commit()
            appBarTitleTextView.text = "Report"
        }
        binding.mapNavBtn.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment_content_main, MapFragment())
            transaction.commit()
            appBarTitleTextView.text = "Map"
        }
        binding.userProfileNavBtn.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment_content_main, UserProfileFragment())
            transaction.commit()
            appBarTitleTextView.text = "My Profile"
        }
        binding.userReportsNavBtn.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment_content_main, LitterSiteReportsFragment())
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

    fun setBackgroundColor(color: Int) {
        binding.mainActivityLayout.setBackgroundColor(resources.getColor(color))
    }

    fun showAppAndNavBars() {
        binding.appBarLayout.visibility = View.VISIBLE
        binding.navBottom.visibility = View.VISIBLE
    }

    fun hideAppAndNavBars() {
        binding.appBarLayout.visibility = View.GONE
        binding.navBottom.visibility = View.GONE
    }

}