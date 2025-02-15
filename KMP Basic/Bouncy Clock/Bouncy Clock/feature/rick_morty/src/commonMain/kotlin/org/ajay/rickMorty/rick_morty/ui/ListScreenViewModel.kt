package org.ajay.rickMorty.rick_morty.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ajay.bouncy_clock.domain.onError
import org.ajay.bouncy_clock.domain.onSuccess
import org.ajay.rickMorty.rick_morty.domain.RickMortyDataClient
import org.ajay.rickMorty.rick_morty.ui.ListScreenAction
import org.ajay.rickMorty.rick_morty.ui.ListScreenEvent
import org.ajay.rickMorty.rick_morty.ui.ListScreenState

class ListScreenViewModel(private val client: RickMortyDataClient) : ViewModel() {
    private val _screenState = MutableStateFlow(ListScreenState())
    val screenState = _screenState
        .onStart {
            loadCharacters()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ListScreenState())

    private val _event = Channel<ListScreenEvent>()
    val event = _event.receiveAsFlow()

    fun screenAction(action: ListScreenAction) {
        when (action) {
            is ListScreenAction.OnClickAction -> {
                selectCharacter(action.code)
            }
        }
    }

    private fun selectCharacter(code: String) {
        viewModelScope.launch(Dispatchers.IO) {
            client.getCharacterDetail(code)
                .onSuccess { characterDetail ->
                    _screenState.update {
                        it.copy(selectedCharacter = characterDetail)
                    }
                }
                .onError { error ->
                    _event.send(ListScreenEvent.Error(error))
                }
        }

    }

    private fun loadCharacters() {
        viewModelScope.launch(Dispatchers.IO) {
            _screenState.update {
                it.copy(isLoading = true)
            }
            client.getCharacters()
                .onSuccess { result ->
                    _screenState.update {
                        it.copy(data = result, isLoading = false)
                    }
                }
                .onError { error ->
                    _screenState.update {
                        it.copy(isLoading = false)
                    }
                    _event.send(ListScreenEvent.Error(error))
                }
        }
    }
}