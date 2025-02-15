package org.ajay.rickMorty.rick_morty.domain

import kotlinx.serialization.Serializable

@Serializable
data class CharacterDetail(
    val id:String,
    val name:String,
    val image:String,
    val status:String,
    val species:String,
    val gender:String,
    val origin: String,
    val location: String,
    val episodes: List<String>
)
