package com.gorman.pexelsappkmp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gorman.pexelsappkmp.domain.viewmodels.HomeViewModel
import com.gorman.pexelsappkmp.ui.HomeScreen
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: HomeViewModel = koinViewModel()
            HomeScreen(viewModel)
        }
    }
}