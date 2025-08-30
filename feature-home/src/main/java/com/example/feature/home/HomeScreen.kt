package com.example.feature.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.model.FinancialProduct
import com.example.core.model.StockInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("金融助手") },
                actions = {
                    IconButton(onClick = { }) { Icon(Icons.Default.Search, "搜索") }
                    IconButton(onClick = { }) { Icon(Icons.Default.Notifications, "通知") }
                }
            )
        }
    ) { padding ->
        HomeContent(uiState = uiState.value, paddingValues = padding)
    }
}

@Composable
private fun HomeContent(uiState: HomeUiState, paddingValues: PaddingValues = PaddingValues(0.dp)) {
    if (uiState.isLoading) {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }
        return
    }
    uiState.errorMessage?.let { message ->
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) { Text(text = message, color = Color.Red) }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                androidx.compose.foundation.layout.Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("总资产", fontSize = 14.sp)
                    Text("¥128,500.00", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    androidx.compose.foundation.layout.Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                    ) {
                        AssetItem("可用资金", "50,000.00")
                        AssetItem("持仓市值", "78,500.00")
                    }
                }
            }
        }
        item {
            SectionTitle("热门行情")
            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
                items(uiState.stocks) { stock -> StockCard(stock) }
            }
        }
        item {
            SectionTitle("推荐理财")
            androidx.compose.foundation.layout.Column(Modifier.padding(horizontal = 16.dp)) {
                uiState.products.forEach { product -> ProductCard(product) }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(16.dp),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun AssetItem(title: String, amount: String) {
    androidx.compose.foundation.layout.Column {
        Text(title, fontSize = 12.sp, color = Color.Gray)
        Text("¥$amount", fontSize = 16.sp)
    }
}

@Composable
private fun StockCard(stock: StockInfo) {
    Card(
        modifier = Modifier
            .padding(end = 8.dp)
            .padding(bottom = 4.dp).width(10.dp)
    ) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(stock.name, fontWeight = FontWeight.Bold)
            Text(stock.code, fontSize = 12.sp, color = Color.Gray)
            Text(
                "¥${stock.price}",
                fontSize = 16.sp,
                color = if (stock.changePercent >= 0) Color(0xFF00C853) else Color(0xFFD32F2F)
            )
            Text(
                "${if (stock.changePercent >= 0) "+" else ""}${stock.changePercent}%",
                color = if (stock.changePercent >= 0) Color(0xFF00C853) else Color(0xFFD32F2F)
            )
        }
    }
}

@Composable
private fun ProductCard(product: FinancialProduct) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 4.dp)
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.foundation.layout.Column {
                Text(product.name, fontWeight = FontWeight.Bold)
                Text(
                    "${product.period} | ${product.riskLevel}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            androidx.compose.foundation.layout.Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${product.expectedReturn}%",
                    color = Color(0xFFE65100),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("预期年化", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}








