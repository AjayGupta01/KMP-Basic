package org.ajay.rickMorty.rick_morty.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import org.ajay.bouncy_clock.presentation.ObserveAsEvents
import org.ajay.rickMorty.rick_morty.domain.SimpleCharacter
import org.koin.compose.koinInject

@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    viewModel: ListScreenViewModel = koinInject(),
    onClick: (characterId: String) -> Unit
) {
    val hostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.event) { event ->
        when (event) {
            is ListScreenEvent.Error -> {
                scope.launch {
                    hostState.showSnackbar(event.error.toString())
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = hostState)
        }
    ) { _ ->
        BaseScreen(screenState = screenState, modifier = modifier) { action ->
            when (action) {
                is ListScreenAction.OnClickAction -> onClick(action.code)
            }
            viewModel.screenAction(action)
        }
    }

}


@Composable
private fun BaseScreen(
    modifier: Modifier = Modifier,
    screenState: ListScreenState,
    onAction: (ListScreenAction) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        when {
            screenState.isLoading -> {
                Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(8.dp),
                    verticalItemSpacing = 8.dp
                ) {

                    items(screenState.data) { character ->
                        CharacterItem(character) {
                            onAction(ListScreenAction.OnClickAction(character.id))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterItem(character: SimpleCharacter, onClick: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = character.name,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}