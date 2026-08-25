package com.example.feature.market

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.StockInfo
import com.example.core.navigation.AppDestination
import com.example.core.navigation.AppRouter

private val UpRed = Color(0xFFE53935)
private val DownGreen = Color(0xFF26A69A)

@Composable
fun MarketScreen() {
    var topTab by remember { mutableIntStateOf(1) }
    var subTab by remember { mutableIntStateOf(0) }

    val watchlist = listOf(
        StockInfo("天龙股份", "300063", 12.86, 4.21),
        StockInfo("上证指数", "000001", 3892.35, 0.82),
        StockInfo("深证成指", "399001", 12456.78, -0.35),
        StockInfo("创业板指", "399006", 2567.89, 1.24),
        StockInfo("贵州茅台", "600519", 1789.99, 2.80),
        StockInfo("宁德时代", "300750", 156.78, -1.50),
        StockInfo("招商银行", "600036", 34.56, 0.80),
        StockInfo("比亚迪", "002594", 245.67, 3.20)
    )
    val holdings = listOf(
        StockInfo("贵州茅台", "600519", 1789.99, 2.80),
        StockInfo("比亚迪", "002594", 245.67, 3.20)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        MarketHeader(topTab) { topTab = it }
        IndexStrip()
        SubTabs(subTab) { subTab = it }
        ListHeader()
        HorizontalDivider(color = Color(0xFFEEEEEE))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            val data = if (subTab == 0) watchlist else holdings
            items(data) { stock ->
                StockRow(stock) {
                    AppRouter.navigate(AppDestination.TradeStock(stock.code, stock.name))
                }
                HorizontalDivider(color = Color(0xFFF5F5F5))
            }
        }
    }
}

@Composable
private fun MarketHeader(selected: Int, onSelect: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(UpRed)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf("自选股", "行情").forEachIndexed { index, title ->
            Text(
                text = title,
                color = if (selected == index) Color.White else Color.White.copy(alpha = 0.7f),
                fontWeight = if (selected == index) FontWeight.Bold else FontWeight.Normal,
                fontSize = if (selected == index) 18.sp else 16.sp,
                modifier = Modifier
                    .clickable { onSelect(index) }
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { }) {
            Icon(Icons.Default.Search, contentDescription = "搜索", tint = Color.White)
        }
    }
}

@Composable
private fun IndexStrip() {
    val indices = listOf(
        Triple("沪", "3892.35", "+0.82%"),
        Triple("深", "12456.78", "-0.35%"),
        Triple("创", "2567.89", "+1.24%")
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(UpRed)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        indices.forEach { (name, value, change) ->
            val up = change.startsWith("+")
            Column {
                Text(
                    text = "$name $value",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = change,
                    color = if (up) Color(0xFFFFCDD2) else Color(0xFFB2DFDB),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun SubTabs(selected: Int, onSelect: (Int) -> Unit) {
    val tabs = listOf("自选股", "持仓股")
    TabRow(
        selectedTabIndex = selected,
        containerColor = Color.White,
        contentColor = UpRed,
        indicator = { positions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(positions[selected]),
                color = UpRed
            )
        }
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selected == index,
                onClick = { onSelect(index) },
                text = {
                    Text(
                        text = title,
                        fontWeight = if (selected == index) FontWeight.Bold else FontWeight.Normal,
                        color = if (selected == index) UpRed else Color.Gray
                    )
                }
            )
        }
    }
}

@Composable
private fun ListHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = "名称代码", modifier = Modifier.weight(1.2f), color = Color.Gray, fontSize = 12.sp)
        Text(
            text = "最新",
            modifier = Modifier.weight(1f),
            color = Color.Gray,
            fontSize = 12.sp,
            textAlign = TextAlign.End
        )
        Text(
            text = "幅度",
            modifier = Modifier.weight(1f),
            color = Color.Gray,
            fontSize = 12.sp,
            textAlign = TextAlign.End
        )
        Text(
            text = "涨跌",
            modifier = Modifier.weight(1f),
            color = Color.Gray,
            fontSize = 12.sp,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun StockRow(stock: StockInfo, onClick: () -> Unit) {
    val up = stock.changePercent >= 0
    val color = if (up) UpRed else DownGreen
    val changeAmt = stock.price * stock.changePercent / 100
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1.2f)) {
            Text(text = stock.name, fontWeight = FontWeight.Medium, fontSize = 15.sp)
            Text(text = stock.code, fontSize = 12.sp, color = Color.Gray)
        }
        Text(
            text = "%.2f".format(stock.price),
            modifier = Modifier.weight(1f),
            color = color,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.End
        )
        Text(
            text = "${if (up) "+" else ""}%.2f%%".format(stock.changePercent),
            modifier = Modifier.weight(1f),
            color = color,
            fontSize = 14.sp,
            textAlign = TextAlign.End
        )
        Text(
            text = "${if (up) "+" else ""}%.2f".format(changeAmt),
            modifier = Modifier.weight(1f),
            color = color,
            fontSize = 14.sp,
            textAlign = TextAlign.End
        )
    }
}
