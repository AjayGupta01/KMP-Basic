package org.ajay.rickMorty.rick_morty.ui


import org.ajay.rickMorty.rick_morty.domain.CharacterDetail
import org.ajay.rickMorty.rick_morty.domain.SimpleCharacter


data class ListScreenState(
    val isLoading: Boolean = false,
    val data: List<SimpleCharacter> = emptyList(),
    val selectedCharacter: CharacterDetail? = null
)