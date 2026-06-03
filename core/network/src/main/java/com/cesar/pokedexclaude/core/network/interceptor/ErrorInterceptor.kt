package com.cesar.pokedexclaude.core.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * OkHttp interceptor for centralized error handling.
 *
 * This interceptor catches network errors and provides consistent error messages
 * across the application. It transforms HTTP error codes into meaningful exceptions.
 */
class ErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        return try {
            val response = chain.proceed(request)

            // Handle HTTP error codes
            when (response.code) {
                in 200..299 -> {
                    // Success - return response as is
                    response
                }
                401 -> {
                    throw IOException("Unauthorized: Authentication required")
                }
                403 -> {
                    throw IOException("Forbidden: Access denied")
                }
                404 -> {
                    throw IOException("Not Found: Resource does not exist")
                }
                500 -> {
                    throw IOException("Server Error: Please try again later")
                }
                503 -> {
                    throw IOException("Service Unavailable: Server is temporarily unavailable")
                }
                else -> {
                    throw IOException("Network error: ${response.code} ${response.message}")
                }
            }
        } catch (e: IOException) {
            // Network connectivity issues
            throw IOException("Connection error: ${e.message}", e)
        }
    }
}
