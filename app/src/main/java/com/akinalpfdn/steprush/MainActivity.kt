package com.akinalpfdn.steprush

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.akinalpfdn.steprush.ui.StepRushApp
import com.akinalpfdn.steprush.ui.theme.StepRushTheme
import com.akinalpfdn.steprush.viewmodel.ActivityViewModel
import com.akinalpfdn.steprush.worker.StepUpdateWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Background adım güncelleme işini başlat
        setupStepUpdateWorker()
        
        setContent {
            StepRushTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StepRushApp()
                }
            }
        }
    }
    
    private fun setupStepUpdateWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()
        
        val stepUpdateRequest = PeriodicWorkRequestBuilder<StepUpdateWorker>(
            15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()
        
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "step_update_work",
            ExistingPeriodicWorkPolicy.KEEP,
            stepUpdateRequest
        )
    }
}