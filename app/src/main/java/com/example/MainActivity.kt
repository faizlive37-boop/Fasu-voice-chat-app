package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.MainDashboard
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.FasuViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                // Initialize the main core ViewModel
                val viewModel: FasuViewModel = viewModel()
                val isLoggedIn by viewModel.isLoggedIn.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Crossfade(
                        targetState = isLoggedIn,
                        animationSpec = tween(500),
                        label = "app_auth_transition"
                    ) { loggedIn ->
                        if (loggedIn) {
                            MainDashboard(
                                viewModel = viewModel,
                                onLogoutClick = { viewModel.logout() }
                            )
                        } else {
                            LoginScreen(
                                onLoginSuccess = { type, nickname, avatarIdx ->
                                    viewModel.loginWithSocial(type, nickname, avatarIdx)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
