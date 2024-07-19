package com.example.listofitems.ui

import android.util.Log
import androidx.lifecycle.ViewModel
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
import kotlinx.coroutines.launch
import com.example.listofitems.data.Result
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.io.IOException


// this interface  set the status of the data coming from the remote server
sealed interface ItemUiState{
    val loading: Boolean
    val errorMessage: String

    data class HasItems(
        val items: List<Item>,
        override val loading: Boolean,
        override val errorMessage: String
    ): ItemUiState

    data class HasNoItems(
        override val loading: Boolean,
        override val errorMessage: String
    ): ItemUiState

}


class ItemsViewModel(private val repository: ItemsRepository) : ViewModel() {

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

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = repository.getItems()
            _uiState.update { uiState ->
                when (result) {
                    is Result.Success -> {
                        Log.d("Error In result.success ", " Error: ${uiState.errorMessage}")
                        Log.d("data", "getItems: ${result.data}")
                        val items = filterItems(result.data)
                        uiState.copy(items = items, isLoading = false)
                    }
                    is Result.Error -> {
                        if( result.exception is IOException) {
                            Log.d("Error in result.error", "Error: ${uiState.errorMessage}")
                            val error = "No internet connection, please try again"
                            uiState.copy(errorMessage = error, isLoading = false)
                        } else {
                            val error = "Failed to load data"
                            uiState.copy(errorMessage = error, isLoading = false)
                        }
                    }
                }
            }
        }
    }

    fun updateErrorMessage() {
        _uiState.update {
            it.copy(errorMessage = "")
        }
    }

    private fun filterItems(items: List<Item>): List<Item> {
        return items.filter { item ->
            item.name != null && item.name != ""
        }.sortedBy { it.listId }
    }

//    private fun filterItems(): List<Item> {
//        return _uiState.value.items.filter { item ->
//            item.name != null && item.name != ""
//        }.sortedBy { it.listId }
//    }

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
                errorMessage = errorMessage ?: ""
            )
        } else {
            ItemUiState.HasItems(
                items = items,
                loading = isLoading,
                errorMessage = errorMessage ?:""
            )
        }

}



