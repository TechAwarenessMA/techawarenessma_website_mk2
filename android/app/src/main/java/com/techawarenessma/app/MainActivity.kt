package com.techawarenessma.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import com.techawarenessma.app.ui.TaaApp
import com.techawarenessma.app.ui.theme.TaaColors
import com.techawarenessma.app.ui.theme.TaaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cream = TaaColors.Cream.toArgb()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(cream, cream),
            navigationBarStyle = SystemBarStyle.light(cream, cream),
        )
        val store = (application as TaaApplication).certificationStore
        setContent {
            TaaTheme {
                TaaApp(certificationStore = store)
            }
        }
    }
}
