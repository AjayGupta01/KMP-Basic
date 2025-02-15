package org.ajay.bouncy_clock.domain

import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext



suspend inline fun <T> safeApiCall(
    execute: () -> T
): Result<T, DataError.Remote> {
    return try {
        val result = execute()
        Result.Success(result)
    }catch (e: Exception) {
        coroutineContext.ensureActive()
        return Result.Error(DataError.Remote.UNKNOWN)
    }
}