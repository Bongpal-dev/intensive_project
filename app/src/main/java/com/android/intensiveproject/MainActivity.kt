package com.android.intensiveproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.intensiveproject.adapter.ImageSearchAdapter
import com.android.intensiveproject.databinding.ActivityMainBinding
import com.android.intensiveproject.retrofit.ImageItemDetail

const val TAG = "project_test_log"

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var bottomNaviController: BottomNaviVisible? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setup()
    }

    private fun setup() {
        initBottomNavi()
    }

    private fun initBottomNavi() {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment

        binding.bottomNavi.setupWithNavController(navHost.navController)
    }
}

interface BottomNaviVisible{
    fun showBottomNavi()
}
