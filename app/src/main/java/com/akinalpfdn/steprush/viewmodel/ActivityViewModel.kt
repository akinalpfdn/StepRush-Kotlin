package com.akinalpfdn.steprush.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.akinalpfdn.steprush.data.HealthConnectManager
import com.akinalpfdn.steprush.data.StepData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActivityViewModel(application: Application) : AndroidViewModel(application) {
    
    private val healthConnectManager = HealthConnectManager(application)
    
    private val _stepData = MutableStateFlow(
        StepData(
            todaySteps = 0,
            totalSteps = 0,
            weeklySteps = List(7) { 0 }
        )
    )
    val stepData: StateFlow<StepData> = _stepData.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _hasPermissions = MutableStateFlow(false)
    val hasPermissions: StateFlow<Boolean> = _hasPermissions.asStateFlow()
    
    init {
        checkPermissionsAndLoadData()
    }
    
    private fun checkPermissionsAndLoadData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                // Önce Health Connect kullanılabilirliğini kontrol et
                val isAvailable = healthConnectManager.isHealthConnectAvailable()
                if (!isAvailable) {
                    _hasPermissions.value = false
                    _stepData.value = StepData(
                        todaySteps = 0,
                        totalSteps = 0,
                        weeklySteps = List(7) { 0 }
                    )
                    return@launch
                }
                
                val hasPerms = healthConnectManager.hasAllPermissions()
                _hasPermissions.value = hasPerms
                
                if (hasPerms) {
                    loadStepData()
                    startPeriodicUpdates()
                }
            } catch (e: Exception) {
                // Hata durumunda default data kullan
                _hasPermissions.value = false
                _stepData.value = StepData(
                    todaySteps = 0,
                    totalSteps = 0,
                    weeklySteps = List(7) { 0 }
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun onPermissionsGranted() {
        viewModelScope.launch {
            _hasPermissions.value = true
            loadStepData()
            startPeriodicUpdates()
        }
    }
    
    fun onPermissionsDenied() {
        _hasPermissions.value = false
        // İzin verilmezse default/mock data kullan
        _stepData.value = StepData(
            todaySteps = 0,
            totalSteps = 0,
            weeklySteps = List(7) { 0 }
        )
    }
    
    fun checkPermissionsOnResume() {
        viewModelScope.launch {
            try {
                val hasPerms = healthConnectManager.hasAllPermissions()
                _hasPermissions.value = hasPerms
                
                if (hasPerms) {
                    loadStepData()
                    startPeriodicUpdates()
                }
            } catch (e: Exception) {
                _hasPermissions.value = false
            }
        }
    }
    
    private suspend fun loadStepData() {
        try {
            val todaySteps = healthConnectManager.getTodaySteps()
            val totalSteps = healthConnectManager.getTotalSteps()
            val weeklySteps = healthConnectManager.getWeeklySteps()
            
            _stepData.value = StepData(
                todaySteps = todaySteps,
                totalSteps = totalSteps,
                weeklySteps = weeklySteps
            )
        } catch (e: Exception) {
            // Hata durumunda mevcut veriyi koru
        }
    }
    
    private fun startPeriodicUpdates() {
        viewModelScope.launch {
            healthConnectManager.getStepsFlow().collect { newData ->
                _stepData.value = newData
            }
        }
    }
    
    fun refreshData() {
        viewModelScope.launch {
            _isLoading.value = true
            loadStepData()
            _isLoading.value = false
        }
    }
    
    fun calculateStreak(weeklySteps: List<Int>, goal: Int = 10000): Int {
        var streak = 0
        for (steps in weeklySteps.reversed()) {
            if (steps >= goal) {
                streak++
            } else {
                break
            }
        }
        return streak
    }
}