package com.aura.tv.ui.home

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aura.tv.data.apps.AppRepository
import com.aura.tv.domain.models.InstalledApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val appRepository = AppRepository(application)

    private val _currentTime = MutableLiveData<String>()
    val currentTime: LiveData<String> = _currentTime

    private val _currentDate = MutableLiveData<String>()
    val currentDate: LiveData<String> = _currentDate

    private val _isWifiConnected = MutableLiveData<Boolean>()
    val isWifiConnected: LiveData<Boolean> = _isWifiConnected

    private val _capsuleStatus = MutableLiveData<String>()
    val capsuleStatus: LiveData<String> = _capsuleStatus

    private val _installedApps = MutableLiveData<List<InstalledApp>>()
    val installedApps: LiveData<List<InstalledApp>> = _installedApps

    init {
        startTimeUpdates()
        checkNetworkStatus()
        loadApps()
    }

    private fun startTimeUpdates() {
        viewModelScope.launch {
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val dateFormat = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
            while (isActive) {
                val now = Date()
                _currentTime.value = timeFormat.format(now)
                _currentDate.value = dateFormat.format(now)
                _capsuleStatus.value = "AURA OS • Smooth 60fps"
                delay(1000)
            }
        }
    }

    fun checkNetworkStatus() {
        val cm = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val activeNetwork = cm?.activeNetwork
        val caps = cm?.getNetworkCapabilities(activeNetwork)
        val isConnected = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        _isWifiConnected.value = isConnected
    }

    fun loadApps() {
        viewModelScope.launch {
            val apps = appRepository.loadInstalledApps()
            _installedApps.value = apps
        }
    }

    fun launchApp(app: InstalledApp): Boolean {
        return appRepository.launchApp(app)
    }
}
