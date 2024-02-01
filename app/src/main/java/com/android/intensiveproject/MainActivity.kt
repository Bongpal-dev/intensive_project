package com.android.intensiveproject

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.android.intensiveproject.databinding.ActivityMainBinding
import com.android.intensiveproject.extention.moveWithAnimation

const val TAG = "project_test_log"

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setup()
    }

    private fun setup() {
        initBottomNavi()
        mainViewModel.toolBarState.observe(this) { visible ->
            if (visible) {
                binding.bottomNavi.moveWithAnimation(0f)
            } else {
                binding.bottomNavi.moveWithAnimation(60f)
            }
        }
    }

    private fun initBottomNavi() {
        binding.bottomNavi.setOnItemSelectedListener {
            Log.i(TAG, "it: ${it}")
            return@setOnItemSelectedListener false
        }
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        binding.bottomNavi.setupWithNavController(navHost.navController)
    }
}

class MyApp : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}

