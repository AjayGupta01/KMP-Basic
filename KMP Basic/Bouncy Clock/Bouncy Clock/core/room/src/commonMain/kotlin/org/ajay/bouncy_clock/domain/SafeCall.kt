package org.ajay.bouncy_clock.domain

suspend fun <T> safeCall(execute: suspend () -> T): Result<T, DatabaseError> {
    return try {
        val result = execute()
        Result.Success(result)
    } catch (e: Exception) {
        Result.Error(DatabaseError.Unknown(e))
    }
}

