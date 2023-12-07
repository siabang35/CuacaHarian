package com.example.cuacaharian

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface BmkgApiService {
    @GET("/api/cuaca/{id_lokasi}")
    suspend fun getWeatherData(
        @Path("id_lokasi") locationId: String,
        @Header("Apikey") apiKey: String
    ): BmkgWeatherResponse
}

data class LocationData(
    val id: String,
    val provinsi: String,
    val kabkota: String,
    val kecamatan: String,
    val waktu: String? = null,
    val cuaca: String? = null,
    val suhu: Double? = null,
    val kelembapan: Double? = null,
    val ws: Double? = null,
    val wd: String? = null,
    val weatherCode: String? = null
    // Tambahkan properti lain sesuai kebutuhan
)

data class BmkgWeatherResponse(
    val date: String,
    val weather: String,
    val t: Double,
    val tmin: Double,
    val tmax: Double,
    val hu: Double,
    val humin: Double,
    val humax: Double,
    val ws: Double,
    val wd: String
    // Tambahkan properti lain sesuai kebutuhan
)
