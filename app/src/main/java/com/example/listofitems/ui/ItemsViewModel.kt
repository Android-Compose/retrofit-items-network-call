package com.example.listofitems.ui


import android.net.http.HttpException
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.listofitems.ItemsApplication
import com.example.listofitems.data.ItemsRepository
import com.example.listofitems.model.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import com.example.listofitems.utils.ContentManagerViewModel
import com.example.listofitems.data.Result
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.io.IOException


// this interface  set the status of the data coming from the remote server
sealed interface ItemUiState{
    var loading: Boolean
    val errorMessage: String

    data class HasItems(
        val items: List<Item>,
        override var loading: Boolean,
        override val errorMessage: String,
    ): ItemUiState


    data class HasNoItems(
        override var loading: Boolean,
        override val errorMessage: String,
    ): ItemUiState
}


class ItemsViewModel(private val repository: ItemsRepository): ContentManagerViewModel() {

    private var _uiState = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<ItemUiState> = _uiState.map(HomeUiState::toItemUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = _uiState.value.toItemUiState()
        )

    init {
        getItems()
    }

    fun getItems() {
        _uiState.update { it.copy(isLoading = true)} // initializing loading to true

        launchCatching(
            block = {
                val response = repository.getItems() // return Result<List<Items>>
                _uiState.update { currentState ->
                    when(response) {
                        is Result.Success -> { // Result.Success from the repository
                            val data = filterItems(response.data)
                            currentState.copy(
                                items =  data,
                                isLoading = false
                            )
                        }
                        is Result.Error -> { // Result.Error from the repository
                            val errorType = when(response.exception) {
                                is IOException -> "No internet connection\nPlease Check your internet connection"
                                is retrofit2.HttpException -> "Oops, we encountered a problem while processing your request. Please try again"
                                else -> "Failed to load data"
                            }
                            Log.d("Error from ItemsViewModel", "${response.exception.message}")
                            currentState.copy(errorMessage = errorType, isLoading = false)
                        }
                    }
                }
            },
            onError = { throwable ->
                // this catches unexpected error
                Log.d("serious error occurred in ItemsViewModel", "Unexpected error in launchCatching", throwable)
            }
        )
    }

    fun updateErrorMessage() {
        _uiState.update { it.copy(errorMessage = "") }
    }

    fun updateIsLoading( isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun updateItems(items: List<Item>) {
        _uiState.update { it.copy(items = items) }
    }

    private fun filterItems(items: List<Item>): List<Item> {
        return items.filter { item ->
            item.name != null && item.name != ""
        }.sortedBy { it.listId }
    }

    // Factory for PostViewModel that takes repository as a dependency
    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ItemsApplication)
                val repository = application.container.repository
                ItemsViewModel(repository = repository)
            }
        }
    }
}

data class HomeUiState(
    var isLoading: Boolean = false,
    val items: List<Item> = emptyList(),
    val errorMessage: String? = null
) {
    fun toItemUiState() : ItemUiState =
        if(items.isEmpty()) {
            ItemUiState.HasNoItems(
                loading = isLoading,
                errorMessage = errorMessage ?: "No data found"
            )
        } else {
            ItemUiState.HasItems(
                items = items,
                loading = isLoading,
                errorMessage = "" // no error message on Success
            )
        }
}