package com.oilsmart.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.oilsmart.app.ui.navigation.OilSmartNavGraph
import com.oilsmart.app.ui.theme.NeutralBackground
import com.oilsmart.app.ui.theme.OilSmartTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OilSmartTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = NeutralBackground
                ) {
                    OilSmartNavGraph()
                }
            }
        }
    }
}
