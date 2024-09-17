package com.tgyuu.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
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

                LaunchedEffect(Unit) {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.mainSideEffect.collect {
                            when (it) {
                                is MainSideEffect.ShowToast -> Toast.makeText(
                                    this@MainActivity,
                                    it.msg,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CalculationScreen(
                        mainState = mainState,
                        event = viewModel::event,
                        sideEffect = viewModel::sideEffect,
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
    event: (MainIntent) -> Unit,
    sideEffect: (MainSideEffect) -> Unit,
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
                    onClick = { event(MainIntent.Plus) },
                    modifier = Modifier.height(40.dp)
                        .weight(1f)
                ) {
                    Text(text = "+")
                }

                Button(
                    onClick = { event(MainIntent.Minus) },
                    modifier = Modifier.height(40.dp)
                        .weight(1f)
                ) {
                    Text(text = "-")
                }
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(mainState.articles) { article ->
                    Text(text = article)
                }
            }

            Button(
                onClick = { event(MainIntent.GetArticles) },
                modifier = Modifier.height(40.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "+")
            }
        }
    }
}