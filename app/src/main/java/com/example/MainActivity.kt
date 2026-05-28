package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AuraBrowserMain
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.BrowserViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val viewModel: BrowserViewModel = viewModel()
        Surface(modifier = Modifier.fillMaxSize()) {
          AuraBrowserMain(viewModel = viewModel)
        }
      }
    }
  }
}

