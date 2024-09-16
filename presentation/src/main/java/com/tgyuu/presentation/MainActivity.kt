package com.tgyuu.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tgyuu.presentation.ui.theme.MVIPracticeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MVIPracticeTheme {
                val mainState by viewModel.mainState.collectAsStateWithLifecycle()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalculationScreen(
                        mainState = mainState,
                        event = viewModel::event,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CalculationScreen(
    mainState: MainState,
    event: (MainEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        if (mainState.isLoading) {
            Text(
                text = "로딩중..",
                modifier = modifier
            )
        } else {
            Text(
                text = "${mainState.number}",
                modifier = modifier
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { event(MainEvent.Plus) },
                    modifier = Modifier.height(40.dp)
                        .weight(1f)
                ) {
                    Text(text = "+")
                }

                Button(
                    onClick = { event(MainEvent.Minus) },
                    modifier = Modifier.height(40.dp)
                        .weight(1f)
                ) {
                    Text(text = "-")
                }
            }
        }
    }
}