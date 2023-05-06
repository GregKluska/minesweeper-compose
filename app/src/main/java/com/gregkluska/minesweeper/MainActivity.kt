package com.gregkluska.minesweeper

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RawRes
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gregkluska.minesweeper.core.Screen
import com.gregkluska.minesweeper.presentation.component.ScreenUI
import com.gregkluska.minesweeper.presentation.ui.gamescreen.GameEvent
import com.gregkluska.minesweeper.presentation.ui.gamescreen.GameScreen
import com.gregkluska.minesweeper.presentation.ui.gamescreen.GameViewModel
import com.gregkluska.minesweeper.presentation.ui.homescreen.HomeEvent
import com.gregkluska.minesweeper.presentation.ui.homescreen.HomeScreen
import com.gregkluska.minesweeper.presentation.ui.homescreen.HomeViewModel

class MainActivity : ComponentActivity() {

    // Get Vibrator service
    private val vibrator: Vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            ScreenUI { paddingValues ->
                NavHost(navController = navController, startDestination = Screen.Game.route) {

                    composable(route = Screen.Home.route) {
                        val viewModel = viewModel<HomeViewModel>()

                        HomeScreen(
                            modifier = Modifier.padding(paddingValues),
                            state = viewModel.state.value,
                            onEvent = { event ->
                                when (event) {
                                    HomeEvent.ButtonClick -> {
                                        navController.navigate(Screen.Game.route)
                                    }

                                    else -> (viewModel::handleEvent)(event)
                                }
                            }
                        )
                    }

                    composable(route = Screen.Game.route) {
                        val viewModel = viewModel<GameViewModel>()

                        GameScreen(
                            modifier = Modifier.padding(paddingValues),
                            state = viewModel.gameState.value,
                            onEvent = { event ->
                                when (event) {
                                    is GameEvent.PlaySound -> playSound(event.sound)
                                    GameEvent.Vibrate -> vibrate()
                                    else -> (viewModel::handleEvent)(event)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun playSound(@RawRes sound: Int) {
        val mp = MediaPlayer.create(this@MainActivity, sound)
        mp.start()
    }

    private fun vibrate() {
        vibrator.vibrate(
            VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE)
        )
    }
}