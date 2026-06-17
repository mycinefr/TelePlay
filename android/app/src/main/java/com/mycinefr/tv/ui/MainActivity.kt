package com.mycinefr.tv.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.mycinefr.tv.data.repository.AuthRepository
import com.mycinefr.tv.ui.navigation.NavGraph
import com.mycinefr.tv.ui.theme.TelePlayTheme
import com.mycinefr.tv.ui.theme.TVBackground
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Main Activity for TelePlay.
 * Serves as the entry point and hosts the Compose navigation.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TelePlayTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = TVBackground
                ) {
                    val navController = rememberNavController()
                    NavGraph(
                        navController = navController,
                        authRepository = authRepository
                    )
                }
            }
        }
    }
}
