package com.gregkluska.minesweeper.presentation.ui

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RawRes
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gregkluska.minesweeper.R
import com.gregkluska.minesweeper.core.AppBarState
import com.gregkluska.minesweeper.presentation.animation.shakeKeyframes
import com.gregkluska.minesweeper.presentation.component.MinesweeperApp
import com.gregkluska.minesweeper.presentation.component.ProcessDialog
import com.gregkluska.minesweeper.presentation.navigation.Screen
import com.gregkluska.minesweeper.presentation.ui.gamescreen.GameScreen
import com.gregkluska.minesweeper.presentation.ui.gamescreen.GameUiEffect
import com.gregkluska.minesweeper.presentation.ui.gamescreen.GameUiEvent
import com.gregkluska.minesweeper.presentation.ui.gamescreen.GameViewModel
import com.gregkluska.minesweeper.presentation.ui.homescreen.HomeScreen
import com.gregkluska.minesweeper.presentation.ui.homescreen.HomeUiEvent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

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

            MinesweeperApp(
                appBarState = mainViewModel.appBarState.value
            ) { paddingValues ->

                NavHost(navController = navController, startDestination = Screen.Home.route) {

                    composable(route = Screen.Home.route) {
//                        val viewModel = viewModel<HomeViewModel>()
                        mainViewModel.setAppBar(AppBarState.Empty)

                        HomeScreen(
                            modifier = Modifier.padding(paddingValues),
                            onEvent = { event ->
                                when (event) {
                                    is HomeUiEvent.GameStart -> {
                                        navController.navigate("game/${event.width}/${event.height}/${event.mines}")
                                    }
                                }
                            }
                        )
                    }

                    composable(
                        route = Screen.Game.route + "/" + Screen.Game.arguments.joinToString("/") { "{${it.name}}" },
                        arguments = Screen.Game.arguments
                    ) {
                        val viewModel = viewModel<GameViewModel>()
                        val state = viewModel.state.value
                        val gameState = state.game.state.collectAsState().value

                        mainViewModel.setAppBar(AppBarState.Game(
                            onBack = { viewModel.handleEvent(GameUiEvent.BackButtonPressed { navController.popBackStack() }) },
                            startTime = gameState.startTime ?: 0L,
                            endTime = gameState.endTime,
                            onAction = {}
                        ))

                        BackHandler(true) {
                            viewModel.handleEvent(GameUiEvent.BackButtonPressed { navController.popBackStack() })
                        }

                        LaunchedEffect(Unit) {
                            viewModel.effect.onEach { effect ->
                                when (effect) {
                                    GameUiEffect.Vibrate -> {
                                        vibrate()
                                        viewModel.shakeOffset.animateTo(Offset.Zero, shakeKeyframes)
                                    }

                                    GameUiEffect.PlayLoseSound -> playSound(R.raw.lose)
                                    GameUiEffect.PlayWinSound -> playSound(R.raw.win)
                                }
                            }.launchIn(this)
                        }

                        state.dialogQueue.dialog.value?.let {
                            ProcessDialog(it)
                        }

                        GameScreen(
                            modifier = Modifier.padding(paddingValues),
                            state = viewModel.state.value,
                            shakeOffset = viewModel.shakeOffset.value,
                            onEvent = viewModel::handleEvent
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