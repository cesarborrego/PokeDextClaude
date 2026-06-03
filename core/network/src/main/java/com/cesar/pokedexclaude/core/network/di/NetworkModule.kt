package com.cesar.pokedexclaude.core.network.di

import com.cesar.pokedexclaude.core.network.interceptor.ErrorInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Koin module providing network infrastructure dependencies.
 *
 * This module configures and provides:
 * - JSON serialization configuration
 * - OkHttpClient with logging and error handling
 * - Retrofit instance configured for the PokeAPI
 */
val networkModule = module {

    // JSON configuration for serialization/deserialization
    single {
        Json {
            ignoreUnknownKeys = true // Ignore unknown JSON fields
            isLenient = true // Accept non-compliant JSON
            coerceInputValues = true // Use default values for null fields
            prettyPrint = true // Format JSON output (useful for debugging)
        }
    }

    // HTTP logging interceptor for debugging network calls
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // Error interceptor for centralized error handling
    single {
        ErrorInterceptor()
    }

    // OkHttp client with interceptors and timeouts
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(get<ErrorInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // Retrofit instance configured for PokeAPI
    single {
        val json = get<Json>()
        val contentType = "application/json".toMediaType()

        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .client(get())
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
}
