package com.example.cuacaharian

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException


class WeatherViewModel : ViewModel() {
    private val _weatherData = MutableLiveData<WeatherData>()
    val weatherData: LiveData<WeatherData> get() = _weatherData

    fun fetchWeatherData(cityName: String) {
        viewModelScope.launch {
            try {
                val newData = fetchDataFromApi(cityName)
                withContext(Dispatchers.Main) {
                    _weatherData.value = newData
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Tangani kesalahan, misalnya tampilkan pesan kesalahan atau logging
            }
        }
    }

    private suspend fun fetchDataFromApi(cityName: String): WeatherData {
        return try {
            // Panggilan jaringan menggunakan Retrofit
            val response = RetrofitInstance.apiService.getWeatherData(cityName, "YOUR_API_KEY")

            // Tampilkan isi dari response
            println("Response: $response")

            WeatherData(response.weather, response.weather, response.t)
        } catch (e: HttpException) {
            // Tangani kesalahan HTTP
            throw Exception("Failed to fetch weather data: ${e.message()}")
        } catch (e: Exception) {
            // Tangani kesalahan umum
            throw Exception("Failed to fetch weather data: ${e.message}")
        }
    }
}