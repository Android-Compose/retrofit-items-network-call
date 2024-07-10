package com.example.listofitems.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listofitems.data.HomeRepository
import com.example.listofitems.model.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.listofitems.data.Result


class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    private var _uiState = MutableStateFlow(HomeUiState( loading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()


    init {
        getItems()
    }

    private fun getItems() {
        _uiState.value.loading = true
        viewModelScope.launch {
            val result = repository.getItems()
            _uiState.update { uiState ->
                when (result) {
                    is Result.Success -> {
                        Log.d("data", "getItems: ${result.data}")
                        uiState.copy(items = result.data)
                    }
                    is Result.Error -> { uiState.copy( errorMessage = result.exception.message ?: "Error")}
                }
            }
        }
    }
}


data class HomeUiState(
    var loading: Boolean = false,
    val items: List<Item> = emptyList(),
    val errorMessage : String? = null
)

