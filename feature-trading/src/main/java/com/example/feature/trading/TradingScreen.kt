package com.example.feature.trading

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.model.StockInfo
import com.example.core.model.TradeRecord
import com.example.core.model.Position

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradingScreen(viewModel: TradingViewModel = viewModel()) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("交易中心") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        TradingContent(
            uiState = uiState.value,
            onSearchQueryChange = viewModel::updateSearchQuery,
            onStockSelect = viewModel::selectStock,
            onTradeAmountChange = viewModel::updateTradeAmount,
            onTradeTypeChange = viewModel::setTradeType,
            onExecuteTrade = viewModel::executeTrade,
            onClearError = viewModel::clearError,
            paddingValues = padding
        )
    }
}

@Composable
private fun TradingContent(
    uiState: TradingUiState,
    onSearchQueryChange: (String) -> Unit,
    onStockSelect: (StockInfo) -> Unit,
    onTradeAmountChange: (String) -> Unit,
    onTradeTypeChange: (TradeType) -> Unit,
    onExecuteTrade: () -> Unit,
    onClearError: () -> Unit,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 搜索栏
        item {
            SearchSection(
                query = uiState.searchQuery,
                onQueryChange = onSearchQueryChange,
                searchResults = uiState.searchResults,
                onStockSelect = onStockSelect
            )
        }

        // 交易表单
        item {
            if (uiState.selectedStock != null) {
                TradingForm(
                    selectedStock = uiState.selectedStock,
                    tradeAmount = uiState.tradeAmount,
                    tradeType = uiState.tradeType,
                    onAmountChange = onTradeAmountChange,
                    onTypeChange = onTradeTypeChange,
                    onExecute = onExecuteTrade,
                    isLoading = uiState.isLoading
                )
            }
        }

        // 错误提示
        item {
            uiState.errorMessage?.let { error ->
                ErrorMessage(
                    message = error,
                    onDismiss = onClearError
                )
            }
        }

        // 成功提示
        item {
            if (uiState.isTradeSuccessful) {
                SuccessMessage()
            }
        }

        // 持仓列表
        item {
            PositionsSection(positions = uiState.positions)
        }

        // 最近交易记录
        item {
            RecentTradesSection(trades = uiState.recentTrades)
        }
    }
}

@Composable
private fun SearchSection(
    query: String,
    onQueryChange: (String) -> Unit,
    searchResults: List<StockInfo>,
    onStockSelect: (StockInfo) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "搜索股票",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                label = { Text("输入股票代码或名称") },
                leadingIcon = { Icon(Icons.Default.Search, "搜索") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            if (searchResults.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "搜索结果",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(searchResults) { stock ->
                        StockChip(
                            stock = stock,
                            onClick = { onStockSelect(stock) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StockChip(
    stock: StockInfo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stock.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = stock.code,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "¥${stock.price}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (stock.changePercent >= 0) Color.Green else Color.Red
            )
        }
    }
}

@Composable
private fun TradingForm(
    selectedStock: StockInfo,
    tradeAmount: String,
    tradeType: TradeType,
    onAmountChange: (String) -> Unit,
    onTypeChange: (TradeType) -> Unit,
    onExecute: () -> Unit,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "交易信息",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // 选中的股票信息
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = selectedStock.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = selectedStock.code,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "¥${selectedStock.price}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (selectedStock.changePercent >= 0) Color.Green else Color.Red
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 交易类型选择
            Text(
                text = "交易类型",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TradeType.values().forEach { type ->
                    val isSelected = tradeType == type
                    val backgroundColor = if (isSelected) {
                        if (type == TradeType.BUY) Color.Green else Color.Red
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                    val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                    
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onTypeChange(type) },
                        colors = CardDefaults.cardColors(containerColor = backgroundColor)
                    ) {
                        Text(
                            text = if (type == TradeType.BUY) "买入" else "卖出",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            color = textColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 交易金额输入
            OutlinedTextField(
                value = tradeAmount,
                onValueChange = onAmountChange,
                label = { Text("交易金额 (元)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 执行交易按钮
            Button(
                onClick = onExecute,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && tradeAmount.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (tradeType == TradeType.BUY) Color.Green else Color.Red
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = if (tradeType == TradeType.BUY) "确认买入" else "确认卖出",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ErrorMessage(
    message: String,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Red.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message,
                color = Color.Red,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onDismiss) {
                Text("关闭", color = Color.Red)
            }
        }
    }
}

@Composable
private fun SuccessMessage() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Green.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "✅ 交易成功！",
                color = Color.Green,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun PositionsSection(positions: List<Position>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "当前持仓",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            if (positions.isEmpty()) {
                Text(
                    text = "暂无持仓",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                // 列表式持仓显示，支持横向和竖向滑动
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(positions) { position ->
                        PositionListItem(position = position)
                    }
                }
            }
        }
    }
}

@Composable
private fun PositionListItem(position: Position) {
    Card(
        modifier = Modifier
            .width(320.dp)
            .clickable { /* 可以添加点击事件 */ },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 第一行：名称/市值
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = position.stock.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = position.stock.code,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "¥${String.format("%.0f", position.marketValue)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 第二行：现价/成本
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "现价",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "¥${position.stock.price}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (position.stock.changePercent >= 0) Color.Green else Color.Red
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "成本",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "¥${position.costPrice}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 第三行：持仓/可用
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "持仓",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${position.quantity}股",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "可用",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${position.availableQuantity}股",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 第四行：盈亏/盈亏率
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "盈亏",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "¥${String.format("%.0f", position.profitLoss)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (position.profitLoss >= 0) Color.Green else Color.Red
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "盈亏率",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${String.format("%.2f", position.profitLossRate)}%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (position.profitLossRate >= 0) Color.Green else Color.Red
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentTradesSection(trades: List<TradeRecord>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "最近交易记录",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            if (trades.isEmpty()) {
                Text(
                    text = "暂无交易记录",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                trades.forEach { trade ->
                    TradeRecordItem(trade = trade)
                    if (trade != trades.last()) {
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TradeRecordItem(trade: TradeRecord) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = trade.type,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (trade.type == "买入") Color.Green else Color.Red
            )
            Text(
                text = trade.time,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "¥${trade.amount}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = trade.status,
                fontSize = 12.sp,
                color = if (trade.status == "成功") Color.Green else Color.Red
            )
        }
    }
}
