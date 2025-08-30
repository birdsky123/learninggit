package com.example.feature.wealth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.model.FinancialProduct
import com.example.core.repository.WealthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class WealthUiState(
    val isLoading: Boolean = false,
    val selectedTabIndex: Int = 0,
    val tabs: List<String> = listOf("理财", "基金", "保险"),
    val products: List<FinancialProduct> = emptyList(),
    val errorMessage: String? = null
)

class WealthViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<WealthUiState> = MutableStateFlow(WealthUiState(isLoading = true))
    val uiState: StateFlow<WealthUiState> = _uiState.asStateFlow()

    init { load() }

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTabIndex = index)
        load()
    }

    fun load() {
        val category = _uiState.value.tabs[_uiState.value.selectedTabIndex]
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                val list = WealthRepository.getProductsByCategory(category)
                _uiState.value = _uiState.value.copy(isLoading = false, products = list)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }
}








