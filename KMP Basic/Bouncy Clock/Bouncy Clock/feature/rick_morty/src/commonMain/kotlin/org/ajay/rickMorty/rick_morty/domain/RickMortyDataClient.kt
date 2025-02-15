package org.ajay.rickMorty.rick_morty.domain

import org.ajay.bouncy_clock.domain.DataError
import org.ajay.bouncy_clock.domain.Result


interface RickMortyDataClient {
    suspend fun getCharacters(): Result<List<SimpleCharacter>, DataError.Remote>
    suspend fun getCharacterDetail(characterId:String): Result<CharacterDetail?, DataError.Remote>
}