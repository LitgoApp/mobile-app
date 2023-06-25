package com.litgo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

        }
    }

    // RETRIEVED & ADAPTED FROM: https://github.com/Kilo-Loco/content/tree/main/android/camera-jetpack-compose
    // requests the user for permission to use the Camera if they have not already done so

    override fun onDestroy() {
        super.onDestroy()
    }
}