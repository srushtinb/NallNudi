package com.nallanudi.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.nallanudi.ai.navigation.NavGraph
import com.nallanudi.ai.ui.theme.NallaNudiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NallaNudiTheme {
                val viewModel: com.nallanudi.ai.presentation.viewmodel.MainViewModel = hiltViewModel()
                NavGraph(viewModel = viewModel)
            }
        }
    }
}
