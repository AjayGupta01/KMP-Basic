package org.ajay.bouncy_clock.ui.drill_list


import org.ajay.bouncy_clock.domain.DatabaseError

sealed interface DrillListEvent {
    data class Error(val error: DatabaseError): DrillListEvent
    data class Success(val message: String): DrillListEvent
}