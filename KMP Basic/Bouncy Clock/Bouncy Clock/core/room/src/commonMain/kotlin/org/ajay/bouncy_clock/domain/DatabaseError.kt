package org.ajay.bouncy_clock.domain

sealed class DatabaseError : Error {
    data object NotFound : DatabaseError() // For cases like no drill found by ID
    data object InsertFailed : DatabaseError() // For insert failure (e.g., conflict ignored)
    data object DeleteFailed : DatabaseError() // For cases where delete affects 0 rows
    data class Unknown(val throwable: Throwable) : DatabaseError() // Catch-all for unexpected errors
}
