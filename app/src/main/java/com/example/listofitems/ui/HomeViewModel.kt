package com.example.listofitems.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.listofitems.HomeApplication
import com.example.listofitems.data.HomeRepository
import com.example.listofitems.model.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.listofitems.data.Result
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


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


class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    private var _uiState = MutableStateFlow(HomeUiState( loading = true))
//    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val uiState: StateFlow<ItemUiState> = _uiState.map(HomeUiState::toItemUiState)
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _uiState.value.toItemUiState()
    )

    init {
        getItems()
    }


    private fun getItems() {
        _uiState.value.loading = true
        viewModelScope.launch {
            val result = repository.getItems()
            _uiState.update { uiState ->

                Log.d("hasNoItems", "HasNoError: ${uiState.errorMessage}")
                when (result) {
                    is Result.Success -> {
                        Log.d("data", "getItems: ${result.data}")
                        val items = filterItems()
                        uiState.copy(items = items, loading = false)
                    }
                    is Result.Error -> {
                        Log.d("error", "error: ${uiState.errorMessage}")
                        val error = result.exception.message ?: "Failed to load data"
                        uiState.copy(errorMessage = error, loading = false)
                    }
                }
            }
        }
    }

    private fun filterItems(): List<Item> {
        return _uiState.value.items.filter { item ->
            item.name != null && item.name != ""
        }.sortedBy { it.listId }

    }
    // Factory for PostViewModel that takes repository as a dependency
    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HomeApplication)
                val repository = application.container.repository
                HomeViewModel(repository = repository)
            }
        }
    }
}

data class HomeUiState(
    var loading: Boolean = false,
    val items: List<Item> = emptyList(),
    val errorMessage: String? = null
) {
    fun toItemUiState() : ItemUiState =
        if(items.isEmpty()) {
            ItemUiState.HasNoItems(
                loading = loading,
                errorMessage = errorMessage ?: "Failed to load data"
            )
        } else {
            ItemUiState.HasItems(
                items = items,
                loading = loading,
                errorMessage = errorMessage ?:""
            )
        }

}
//data class HomeUiState(
//    var loading: Boolean = false,
//    val items: List<Item> = emptyList(),
//    val errorMessage : String =""
//)


