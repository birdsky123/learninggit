package com.example.feature.home
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.model.FinancialProduct
import com.example.core.model.StockInfo
import com.example.core.repository.StockRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val stocks: List<StockInfo> = emptyList(),
    val products: List<FinancialProduct> = emptyList(),
    val errorMessage: String? = null
)

class HomeViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()  // 外部

    init { load() }

    fun load() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                val stocksDeferred = async { StockRepository.getHotStocks() }
                val productsDeferred = async { StockRepository.getRecommendedProducts() }

                val stocksResult = runCatching { stocksDeferred.await() }.getOrElse { emptyList() }
                val productsResult = runCatching { productsDeferred.await() }.getOrElse { emptyList() }

                _uiState.value = HomeUiState(
                    isLoading = false,
                    stocks = stocksResult,
                    products = productsResult,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = HomeUiState(
                    isLoading = false,
                    stocks = emptyList(),
                    products = emptyList(),
                    errorMessage = e.message ?: "加载失败"
                )
            }
        }
    }
}



