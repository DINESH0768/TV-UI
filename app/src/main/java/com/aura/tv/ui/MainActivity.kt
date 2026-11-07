package com.aura.tv.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aura.tv.R
import com.aura.tv.databinding.ActivityMainBinding
import com.aura.tv.ui.home.AppAdapter
import com.aura.tv.ui.home.HomeViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var appAdapter: AppAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkNetworkStatus()
        viewModel.loadApps()
    }

    private fun setupRecyclerView() {
        appAdapter = AppAdapter { app ->
            val success = viewModel.launchApp(app)
            if (!success) {
                Toast.makeText(this, "Unable to launch ${app.label}", Toast.LENGTH_SHORT).show()
            }
        }
        binding.rvApps.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = appAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupListeners() {
        binding.btnSettings.setOnClickListener {
            openSystemSettings()
        }

        binding.cardFeatured.setOnClickListener {
            Toast.makeText(this, "AURA TV: Dark Liquid Glass Dashboard", Toast.LENGTH_SHORT).show()
        }

        binding.dynamicCapsule.setOnClickListener {
            Toast.makeText(this, "Dynamic Island Capsule active", Toast.LENGTH_SHORT).show()
        }

        // Focus animation for featured card and settings button
        binding.cardFeatured.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v.animate().scaleX(1.03f).scaleY(1.03f).setDuration(150).start()
            } else {
                v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
            }
        }

        binding.btnSettings.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v.animate().scaleX(1.15f).scaleY(1.15f).setDuration(150).start()
            } else {
                v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
            }
        }

        binding.dynamicCapsule.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                v.animate().scaleX(1.06f).scaleY(1.06f).setDuration(150).start()
            } else {
                v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start()
            }
        }
    }

    private fun openSystemSettings() {
        try {
            val intent = Intent(Settings.ACTION_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Settings unavailable", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        viewModel.currentTime.observe(this) { time ->
            binding.tvHeaderTime.text = time
            binding.tvHeroClock.text = time
        }

        viewModel.currentDate.observe(this) { date ->
            binding.tvHeroDate.text = date
        }

        viewModel.isWifiConnected.observe(this) { isConnected ->
            binding.ivWifiStatus.alpha = if (isConnected) 1.0f else 0.4f
        }

        viewModel.capsuleStatus.observe(this) { status ->
            binding.tvCapsuleText.text = status
        }

        viewModel.installedApps.observe(this) { apps ->
            appAdapter.submitList(apps)
            if (apps.isNotEmpty() && !binding.rvApps.hasFocus() && !binding.btnSettings.hasFocus()) {
                binding.rvApps.post {
                    binding.rvApps.findViewHolderForAdapterPosition(0)?.itemView?.requestFocus()
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // TV remote navigation fallback handling
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                // Prevent exiting launcher on back press; remain on launcher
                return true
            }
            KeyEvent.KEYCODE_HOME -> {
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
