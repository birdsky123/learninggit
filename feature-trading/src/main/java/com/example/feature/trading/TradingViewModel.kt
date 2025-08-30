package com.example.feature.trading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.model.StockInfo
import com.example.core.model.TradeRecord
import com.example.core.model.Position
import com.example.core.repository.StockRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TradingUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val searchResults: List<StockInfo> = emptyList(),
    val selectedStock: StockInfo? = null,
    val tradeAmount: String = "",
    val tradeType: TradeType = TradeType.BUY,
    val recentTrades: List<TradeRecord> = emptyList(),
    val errorMessage: String? = null,
    val isTradeSuccessful: Boolean = false,
    val positions: List<Position> = emptyList()
)

enum class TradeType {
    BUY, SELL
}

class TradingViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<TradingUiState> = MutableStateFlow(TradingUiState(isLoading = true))
    val uiState: StateFlow<TradingUiState> = _uiState.asStateFlow()

    init {
        loadRecentTrades()
        loadPositions()
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        if (query.isNotEmpty()) {
            searchStocks(query)
        } else {
            _uiState.value = _uiState.value.copy(searchResults = emptyList())
        }
    }

    fun selectStock(stock: StockInfo) {
        _uiState.value = _uiState.value.copy(selectedStock = stock)
    }

    fun updateTradeAmount(amount: String) {
        _uiState.value = _uiState.value.copy(tradeAmount = amount)
    }

    fun setTradeType(type: TradeType) {
        _uiState.value = _uiState.value.copy(tradeType = type)
    }

    fun executeTrade() {
        val stock = _uiState.value.selectedStock
        val amount = _uiState.value.tradeAmount.toDoubleOrNull()
        
        if (stock == null || amount == null || amount <= 0) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "请选择股票并输入有效金额"
            )
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        
        viewModelScope.launch {
            try {
                // 模拟交易执行
                kotlinx.coroutines.delay(1000)
                
                val tradeRecord = TradeRecord(
                    type = if (_uiState.value.tradeType == TradeType.BUY) "买入" else "卖出",
                    amount = amount,
                    time = java.time.LocalDateTime.now().toString(),
                    status = "成功"
                )
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isTradeSuccessful = true,
                    recentTrades = listOf(tradeRecord) + _uiState.value.recentTrades,
                    tradeAmount = "",
                    selectedStock = null
                )
                
                // 重置成功状态
                kotlinx.coroutines.delay(2000)
                _uiState.value = _uiState.value.copy(isTradeSuccessful = false)
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "交易失败"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    private fun searchStocks(query: String) {
        viewModelScope.launch {
            try {
                val stocks = StockRepository.getHotStocks().filter { stock ->
                    stock.name.contains(query, ignoreCase = true) || 
                    stock.code.contains(query, ignoreCase = true)
                }
                _uiState.value = _uiState.value.copy(searchResults = stocks)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "搜索失败: ${e.message}"
                )
            }
        }
    }

    private fun loadRecentTrades() {
        viewModelScope.launch {
            try {
                val mockTrades = listOf(
                    TradeRecord("买入", 1000.0, "2024-01-15 14:30:00", "成功"),
                    TradeRecord("卖出", 500.0, "2024-01-14 15:20:00", "成功"),
                    TradeRecord("买入", 2000.0, "2024-01-13 10:15:00", "成功")
                )
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    recentTrades = mockTrades
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "加载交易记录失败"
                )
            }
        }
    }

    private fun loadPositions() {
        viewModelScope.launch {
            try {
                val mockPositions = listOf(
                    Position(
                        stock = StockInfo("贵州茅台", "600519", 1789.99, 2.8),
                        costPrice = 1650.0,
                        quantity = 100,
                        availableQuantity = 100,
                        marketValue = 178999.0,
                        profitLoss = 13999.0,
                        profitLossRate = 8.48
                    ),
                    Position(
                        stock = StockInfo("宁德时代", "300750", 156.78, -1.5),
                        costPrice = 180.0,
                        quantity = 500,
                        availableQuantity = 500,
                        marketValue = 78390.0,
                        profitLoss = -11610.0,
                        profitLossRate = -12.9
                    ),
                    Position(
                        stock = StockInfo("比亚迪", "002594", 245.67, 1.2),
                        costPrice = 220.0,
                        quantity = 300,
                        availableQuantity = 300,
                        marketValue = 73701.0,
                        profitLoss = 7701.0,
                        profitLossRate = 11.67
                    )
                )
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    positions = mockPositions
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "加载持仓失败"
                )
            }
        }
    }
}
