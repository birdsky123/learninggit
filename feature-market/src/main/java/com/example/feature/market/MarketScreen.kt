package com.example.feature.market
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.StockInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen() {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("沪深", "港股", "美股")

    val mockStocks = listOf(
        StockInfo("贵州茅台", "600519", 1789.99, 2.8),
        StockInfo("宁德时代", "300750", 156.78, -1.5),
        StockInfo("招商银行", "600036", 34.56, 0.8),
        StockInfo("比亚迪", "002594", 245.67, 3.2),
        StockInfo("中国平安", "601318", 45.89, -0.7)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("行情") },
                actions = { IconButton(onClick = { }) { Icon(Icons.Default.Search, "搜索") } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            MarketIndexRow()
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(selected = selectedTabIndex == index, onClick = { selectedTabIndex = index }, text = { Text(title) })
                }
            }
            LazyColumn {
                item { ListHeader() }
                items(mockStocks) { stock -> StockListItem(stock) }
            }
        }
    }
}

@Composable
private fun MarketIndexRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IndexCard("上证指数", "3,250.21", 0.82)
        IndexCard("深证成指", "12,789.45", -0.35)
        IndexCard("创业板指", "2,567.89", 1.24)
    }
}

@Composable
private fun IndexCard(name: String, value: String, change: Double) {
    Column {
        Text(name, fontSize = 12.sp, color = Color.Gray)
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(
            "${if (change >= 0) "+" else ""}${change}%",
            color = if (change >= 0) Color(0xFF00C853) else Color(0xFFD32F2F),
            fontSize = 14.sp
        )
    }
}

@Composable
private fun ListHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("名称", color = Color.Gray, fontSize = 12.sp)
        Text("最新价", color = Color.Gray, fontSize = 12.sp)
        Text("涨跌幅", color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
private fun StockListItem(stock: StockInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column { Text(stock.name, fontWeight = FontWeight.Bold); Text(stock.code, fontSize = 12.sp, color = Color.Gray) }
        Text("¥${stock.price}", fontSize = 16.sp)
        Text(
            "${if (stock.changePercent >= 0) "+" else ""}${stock.changePercent}%",
            color = if (stock.changePercent >= 0) Color(0xFF00C853) else Color(0xFFD32F2F)
        )
    }
}








