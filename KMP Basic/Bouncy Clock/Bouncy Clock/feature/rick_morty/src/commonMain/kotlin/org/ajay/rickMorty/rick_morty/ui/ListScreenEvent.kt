package org.ajay.rickMorty.rick_morty.ui

import org.ajay.bouncy_clock.domain.DataError


sealed interface ListScreenEvent {
    data class Error(val error: DataError) : ListScreenEvent
}