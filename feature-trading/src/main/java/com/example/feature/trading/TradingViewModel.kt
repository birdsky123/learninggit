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
    val positions: List<Position> = emptyList(),
    // 分页相关状态
    val isPositionsLoading: Boolean = false,
    val hasMorePositions: Boolean = true,
    val positionsPage: Int = 0,
    val positionsPageSize: Int = 3
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

    // 分页加载持仓
    fun loadMorePositions() {
        if (_uiState.value.isPositionsLoading || !_uiState.value.hasMorePositions) {
            return
        }
        
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isPositionsLoading = true)
                
                // 模拟网络延迟
                kotlinx.coroutines.delay(500)
                
                val nextPage = _uiState.value.positionsPage + 1
                val newPositions = loadPositionsPage(nextPage, _uiState.value.positionsPageSize)
                
                if (newPositions.isNotEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        positions = _uiState.value.positions + newPositions,
                        positionsPage = nextPage,
                        hasMorePositions = newPositions.size >= _uiState.value.positionsPageSize,
                        isPositionsLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        hasMorePositions = false,
                        isPositionsLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isPositionsLoading = false,
                    errorMessage = "加载持仓失败: ${e.message}"
                )
            }
        }
    }

    // 刷新持仓列表
    fun refreshPositions() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isPositionsLoading = true,
                    positions = emptyList(),
                    positionsPage = 0,
                    hasMorePositions = true
                )
                
                // 模拟网络延迟
                kotlinx.coroutines.delay(500)
                
                val initialPositions = loadPositionsPage(0, _uiState.value.positionsPageSize)
                
                _uiState.value = _uiState.value.copy(
                    positions = initialPositions,
                    positionsPage = 0,
                    hasMorePositions = initialPositions.size >= _uiState.value.positionsPageSize,
                    isPositionsLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isPositionsLoading = false,
                    errorMessage = "刷新持仓失败: ${e.message}"
                )
            }
        }
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

    // 分页加载持仓数据
    private fun loadPositionsPage(page: Int, pageSize: Int): List<Position> {
        // 模拟分页数据，实际项目中这里会调用API
        val allMockPositions = listOf(
            // 第一页数据
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
            ),
            // 第二页数据
            Position(
                stock = StockInfo("腾讯控股", "00700", 320.50, 1.8),
                costPrice = 300.0,
                quantity = 200,
                availableQuantity = 200,
                marketValue = 64100.0,
                profitLoss = 4100.0,
                profitLossRate = 6.83
            ),
            Position(
                stock = StockInfo("阿里巴巴", "09988", 78.90, -0.5),
                costPrice = 85.0,
                quantity = 800,
                availableQuantity = 800,
                marketValue = 63120.0,
                profitLoss = -4880.0,
                profitLossRate = -7.18
            ),
            Position(
                stock = StockInfo("美团", "03690", 125.60, 2.1),
                costPrice = 110.0,
                quantity = 400,
                availableQuantity = 400,
                marketValue = 50240.0,
                profitLoss = 6240.0,
                profitLossRate = 14.18
            ),
            // 第三页数据
            Position(
                stock = StockInfo("京东", "09618", 45.20, -1.2),
                costPrice = 50.0,
                quantity = 1000,
                availableQuantity = 1000,
                marketValue = 45200.0,
                profitLoss = -4800.0,
                profitLossRate = -9.6
            ),
            Position(
                stock = StockInfo("小米集团", "01810", 18.50, 0.8),
                costPrice = 17.0,
                quantity = 2000,
                availableQuantity = 2000,
                marketValue = 37000.0,
                profitLoss = 3000.0,
                profitLossRate = 8.82
            ),
            Position(
                stock = StockInfo("网易", "09999", 95.30, 1.5),
                costPrice = 88.0,
                quantity = 600,
                availableQuantity = 600,
                marketValue = 57180.0,
                profitLoss = 4380.0,
                profitLossRate = 8.30
            )
        )
        
        val startIndex = page * pageSize
        val endIndex = minOf(startIndex + pageSize, allMockPositions.size)
        
        return if (startIndex < allMockPositions.size) {
            allMockPositions.subList(startIndex, endIndex)
        } else {
            emptyList()
        }
    }
}
