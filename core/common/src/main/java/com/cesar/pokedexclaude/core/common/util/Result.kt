package com.cesar.pokedexclaude.core.common.util

/**
 * A generic wrapper for handling success and error states in domain operations.
 *
 * This sealed interface provides type-safe result handling for operations that can fail,
 * eliminating the need for exception-based error handling in the domain layer.
 *
 * @param T The type of successful result data
 */
sealed interface Result<out T> {
    /**
     * Represents a successful operation with resulting data.
     *
     * @param data The successful result data
     */
    data class Success<T>(val data: T) : Result<T>

    /**
     * Represents a failed operation with an associated error.
     *
     * @param exception The exception that caused the failure
     * @param message Optional user-friendly error message
     */
    data class Error(
        val exception: Throwable,
        val message: String? = exception.message
    ) : Result<Nothing>

    /**
     * Represents an operation that is currently loading/in-progress.
     */
    data object Loading : Result<Nothing>
}

/**
 * Extension function to check if the result is successful.
 */
fun <T> Result<T>.isSuccess(): Boolean = this is Result.Success

/**
 * Extension function to check if the result is an error.
 */
fun <T> Result<T>.isError(): Boolean = this is Result.Error

/**
 * Extension function to check if the result is loading.
 */
fun <T> Result<T>.isLoading(): Boolean = this is Result.Loading

/**
 * Extension function to get data from a successful result, or null if not successful.
 */
fun <T> Result<T>.getOrNull(): T? = when (this) {
    is Result.Success -> data
    else -> null
}

/**
 * Extension function to get data from a successful result, or a default value if not successful.
 */
fun <T> Result<T>.getOrDefault(defaultValue: T): T = when (this) {
    is Result.Success -> data
    else -> defaultValue
}

/**
 * Extension function to execute a block on successful result.
 */
inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) {
        action(data)
    }
    return this
}

/**
 * Extension function to execute a block on error result.
 */
inline fun <T> Result<T>.onError(action: (Throwable) -> Unit): Result<T> {
    if (this is Result.Error) {
        action(exception)
    }
    return this
}

/**
 * Extension function to execute a block on loading result.
 */
inline fun <T> Result<T>.onLoading(action: () -> Unit): Result<T> {
    if (this is Result.Loading) {
        action()
    }
    return this
}
