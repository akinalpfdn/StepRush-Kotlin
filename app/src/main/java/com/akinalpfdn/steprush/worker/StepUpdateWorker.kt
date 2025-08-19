package com.akinalpfdn.steprush.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.akinalpfdn.steprush.data.HealthConnectManager

class StepUpdateWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val healthConnectManager = HealthConnectManager(applicationContext)

    override suspend fun doWork(): Result {
        return try {
            Log.d("StepUpdateWorker", "Background step update başlatıldı")
            
            if (!healthConnectManager.hasAllPermissions()) {
                Log.w("StepUpdateWorker", "Health Connect izni yok")
                return Result.failure()
            }

            val todaySteps = healthConnectManager.getTodaySteps()
            val totalSteps = healthConnectManager.getTotalSteps()
            
            Log.d("StepUpdateWorker", "Günlük adım: $todaySteps, Toplam adım: $totalSteps")
            
            // Burada verileri SharedPreferences veya Room database'e kaydedebilirsiniz
            // Örnek: SharedPreferences'a kaydetme
            val prefs = applicationContext.getSharedPreferences("step_data", Context.MODE_PRIVATE)
            prefs.edit()
                .putInt("today_steps", todaySteps)
                .putInt("total_steps", totalSteps)
                .putLong("last_update", System.currentTimeMillis())
                .apply()
            
            Result.success()
        } catch (e: Exception) {
            Log.e("StepUpdateWorker", "Background step update hatası: ${e.message}")
            Result.retry()
        }
    }
}