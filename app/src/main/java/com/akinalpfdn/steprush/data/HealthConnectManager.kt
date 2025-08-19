package com.akinalpfdn.steprush.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class HealthConnectManager(private val context: Context) {
    
    private val healthConnectClient by lazy { HealthConnectClient.getOrCreate(context) }
    private val prefs: SharedPreferences = context.getSharedPreferences("step_rush_prefs", Context.MODE_PRIVATE)
    
    val permissions = setOf(
        HealthPermission.getReadPermission(StepsRecord::class)
    )
    
    suspend fun isHealthConnectAvailable(): Boolean {
        return try {
            when (HealthConnectClient.getSdkStatus(context)) {
                HealthConnectClient.SDK_AVAILABLE -> true
                HealthConnectClient.SDK_UNAVAILABLE -> {
                    Log.w("HealthConnect", "Health Connect SDK kullanılamıyor")
                    false
                }
                HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED -> {
                    Log.w("HealthConnect", "Health Connect güncellemesi gerekiyor")
                    false
                }
                else -> false
            }
        } catch (e: Exception) {
            Log.e("HealthConnect", "Health Connect durumu kontrol edilemedi: ${e.message}")
            false
        }
    }
    
    suspend fun hasAllPermissions(): Boolean {
        return try {
            if (!isHealthConnectAvailable()) return false
            healthConnectClient.permissionController.getGrantedPermissions()
                .containsAll(permissions)
        } catch (e: Exception) {
            Log.e("HealthConnect", "İzin kontrolü hatası: ${e.message}")
            false
        }
    }
    
    suspend fun checkPermissionsAndGetStepData(): StepData {
        val granted = healthConnectClient.permissionController.getGrantedPermissions()
        return if (granted.containsAll(permissions)) {
            // İzinler varsa gerçek veriyi çek
            try {
                val todaySteps = getTodaySteps()
                val totalSteps = getTotalSteps()
                val weeklySteps = getWeeklySteps()
                StepData(todaySteps, totalSteps, weeklySteps)
            } catch (e: Exception) {
                Log.e("HealthConnect", "Veri çekme hatası: ${e.message}")
                getDefaultStepData()
            }
        } else {
            // İzin yoksa default data
            getDefaultStepData()
        }
    }
    
    private fun getDefaultStepData() = StepData(
        todaySteps = 0,
        totalSteps = 0,
        weeklySteps = List(7) { 0 }
    )
    
    suspend fun getTodaySteps(): Int {
        return try {
            val today = LocalDateTime.now()
            val startOfDay = today.truncatedTo(ChronoUnit.DAYS)
            val endOfDay = startOfDay.plusDays(1)
            
            val response = healthConnectClient.readRecords(
                ReadRecordsRequest(
                    recordType = StepsRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(
                        startOfDay.atZone(ZoneId.systemDefault()).toInstant(),
                        endOfDay.atZone(ZoneId.systemDefault()).toInstant()
                    )
                )
            )
            
            response.records.sumOf { it.count.toInt() }
        } catch (e: Exception) {
            Log.e("HealthConnect", "Günlük adım çekme hatası: ${e.message}")
            0
        }
    }
    
    suspend fun getWeeklySteps(): List<Int> {
        return try {
            val today = LocalDateTime.now()
            val weeklySteps = mutableListOf<Int>()
            
            for (i in 6 downTo 0) {
                val date = today.minusDays(i.toLong())
                val startOfDay = date.truncatedTo(ChronoUnit.DAYS)
                val endOfDay = startOfDay.plusDays(1)
                
                val response = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        recordType = StepsRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(
                            startOfDay.atZone(ZoneId.systemDefault()).toInstant(),
                            endOfDay.atZone(ZoneId.systemDefault()).toInstant()
                        )
                    )
                )
                
                val daySteps = response.records.sumOf { it.count.toInt() }
                weeklySteps.add(daySteps)
            }
            
            weeklySteps
        } catch (e: Exception) {
            Log.e("HealthConnect", "Haftalık adım çekme hatası: ${e.message}")
            List(7) { 0 }
        }
    }
    
    suspend fun getTotalSteps(): Int {
        return try {
            val currentTotalSteps = prefs.getInt("total_steps", 0)
            val lastDailySteps = prefs.getInt("last_daily_steps", 0)
            val todaySteps = getTodaySteps()
            
            // Eğer bugünkü adımlar önceki kayıtlı günlük adımlardan farklıysa güncelle
            if (todaySteps != lastDailySteps) {
                val difference = todaySteps - lastDailySteps
                val newTotal = currentTotalSteps + difference
                
                // Negatif değer kontrolü (gece yarısı reset durumu)
                val finalTotal = if (difference < 0) {
                    // Büyük ihtimalle yeni gün başladı, sadece bugünkü adımları ekle
                    currentTotalSteps + todaySteps
                } else {
                    newTotal
                }
                
                // SharedPreferences'a kaydet
                prefs.edit()
                    .putInt("total_steps", finalTotal)
                    .putInt("last_daily_steps", todaySteps)
                    .putString("last_update_date", LocalDateTime.now().toLocalDate().toString())
                    .apply()
                
                Log.d("StepTracking", "Total steps updated: $finalTotal (difference: $difference)")
                return finalTotal
            }
            
            currentTotalSteps
        } catch (e: Exception) {
            Log.e("HealthConnect", "Toplam adım çekme hatası: ${e.message}")
            prefs.getInt("total_steps", 0)
        }
    }
    
    // İlk kurulum için toplam adım sıfırlama fonksiyonu
    fun resetTotalSteps() {
        prefs.edit()
            .putInt("total_steps", 0)
            .putInt("last_daily_steps", 0)
            .remove("last_update_date")
            .apply()
        Log.d("StepTracking", "Total steps reset to 0")
    }
    
    // Debug için toplam adım bilgilerini göster
    fun getTotalStepsInfo(): String {
        val totalSteps = prefs.getInt("total_steps", 0)
        val lastDaily = prefs.getInt("last_daily_steps", 0)
        val lastUpdate = prefs.getString("last_update_date", "Never")
        return "Total: $totalSteps, Last Daily: $lastDaily, Last Update: $lastUpdate"
    }
    
    fun getStepsFlow(): Flow<StepData> = flow {
        while (true) {
            try {
                val todaySteps = getTodaySteps()
                val totalSteps = getTotalSteps()
                val weeklySteps = getWeeklySteps()
                
                emit(
                    StepData(
                        todaySteps = todaySteps,
                        totalSteps = totalSteps,
                        weeklySteps = weeklySteps
                    )
                )
                
                kotlinx.coroutines.delay(60000) // Her dakika güncelle
            } catch (e: Exception) {
                Log.e("HealthConnect", "Adım veri akışı hatası: ${e.message}")
                emit(StepData(0, 0, List(7) { 0 }))
            }
        }
    }
}

data class StepData(
    val todaySteps: Int,
    val totalSteps: Int,
    val weeklySteps: List<Int>
)