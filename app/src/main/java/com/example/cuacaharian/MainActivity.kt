package com.example.cuacaharian

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.material3.SnackbarHostState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.remember
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import kotlinx.coroutines.coroutineScope
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.ExperimentalComposeUiApi
import com.example.cuacaharian.ui.theme.CuacaHarianTheme
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction


class MainActivity : ComponentActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CuacaHarianTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherApp(weatherViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WeatherApp(weatherViewModel: WeatherViewModel, modifier: Modifier = Modifier) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val density = LocalDensity.current.density
    val snackbarHostState = rememberSnackbarHostState()
    val weatherData by weatherViewModel.weatherData.observeAsState()

    var cityName by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        SearchBar(
            cityName = cityName,
            onCityNameChange = { cityName = it },
            onSearch = {
                if (cityName.isNotBlank()) {
                    weatherViewModel.fetchWeatherData(cityName)
                    keyboardController?.hide()
                } else {
                    keyboardController?.show()
                }
            }
        )

        // DisposableEffect to manage the keyboard controller
        DisposableEffect(Unit) {
            onDispose {
                // Cleanup code if needed
            }
        }

        // Weather Content
        Spacer(modifier = Modifier.height(16.dp))

        if (weatherData != null) {
            WeatherCard(weatherData!!)
        } else {
            CircularProgressIndicator(modifier = Modifier.size(50.dp))
        }

        // Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    cityName: String,
    onCityNameChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Launch a coroutine to show the snackbar
    LaunchedEffect(snackbarHostState) {
        val message = "Please enter a city name"
        if (cityName.isBlank()) {
            // Call showSnackbar inside the coroutine
            coroutineScope {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BasicTextField(
            value = cityName,
            onValueChange = { onCityNameChange(it) },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch()
                    // Optionally hide the keyboard here
                }
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = LocalContentColor.current),
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )

        IconButton(
            onClick = { onSearch() },
            modifier = Modifier
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        }
    }
}

@Composable
fun WeatherCard(weatherData: WeatherData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = weatherData.lokasi,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Condition: ${weatherData.cuaca}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Temperature: ${weatherData.suhu}°C",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }
}

@Composable
fun rememberSnackbarHostState(): SnackbarHostState {
    return remember { SnackbarHostState() }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun WeatherAppPreview() {
    CuacaHarianTheme {
        WeatherApp(WeatherViewModel())
    }
}