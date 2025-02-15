package org.ajay.rickMorty.rick_morty.ui

sealed interface ListScreenAction {
    data class OnClickAction(val code:String): ListScreenAction
}