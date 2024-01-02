package com.example.contextualtriggers.context.contextData

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper

import java.net.HttpURLConnection
import java.net.URL

data class WeatherResponse(
    @JsonProperty("name") val cityName: String,
    @JsonProperty("weather") val weather: List<Weather>,
    @JsonProperty("main") val main: Main
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Unknown
}

data class Weather(
    @JsonProperty("main") val main: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("icon") val icon: String,
    @JsonProperty("id") val id: Int
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Unknown
}

data class Main @JsonCreator constructor(
    @JsonProperty("temp") val temperature: Double,
)
interface WeatherCallback {
    fun onWeatherResponse(weather: String)
}



// Function to fetch current weather using Callback
fun getCurrentWeather(apiKey: String, latitude: Double, longitude: Double, callback: (String) -> Unit) {
    Thread {
        val url = URL("https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$apiKey&units=metric")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val inputStream = connection.inputStream
        val responseBody = inputStream.bufferedReader().use { it.readText() }

        val mapper = ObjectMapper().apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        val weatherResponse = mapper.readValue(responseBody, WeatherResponse::class.java)
        val weatherMain = weatherResponse.weather[0].main
        callback(weatherMain)
    }.start()
}

fun getCurrentTemperature(apiKey: String, latitude: Double, longitude: Double, callback: (Double) -> Unit) {
    Thread {
        val url = URL("https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$apiKey&units=metric")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        val inputStream = connection.inputStream
        val responseBody = inputStream.bufferedReader().use { it.readText() }

        val mapper = ObjectMapper().apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        val weatherResponse = mapper.readValue(responseBody, WeatherResponse::class.java)
        val weatherTemperature = weatherResponse.main.temperature
        callback(weatherTemperature)
    }.start()
}



