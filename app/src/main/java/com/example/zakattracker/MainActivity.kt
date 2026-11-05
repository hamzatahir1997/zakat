package com.example.zakattracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.zakattracker.ui.ZakatScreen
import com.example.zakattracker.ui.theme.ZakatTrackerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZakatTrackerTheme {
                ZakatScreen()
            }
        }
    }
}
