package com.example.feature.trading

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.navigation.AppDestination
import com.example.core.navigation.AppRouter

private val UpRed = Color(0xFFE53935)
private val PageBg = Color(0xFFF5F5F5)
private val WarningBg = Color(0xFFFFF8E1)

@Composable
fun TradingScreen(
    stockCode: String? = null,
    stockName: String? = null,
    viewModel: TradingViewModel = viewModel()
) {
    var accountTab by remember { mutableIntStateOf(0) }
    LaunchedEffect(stockCode, stockName) {
        viewModel.preselectFromRoute(stockCode, stockName)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg)
    ) {
        AccountTabs(accountTab) { accountTab = it }
        WarningBanner()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                if (!stockCode.isNullOrBlank()) {
                    SelectedStockBanner(stockCode, stockName.orEmpty())
                }
            }
            item { LoginCard() }
            item { TradeActionGrid() }
            item { AnalysisBanner() }
            item { FeatureTiles() }
            item { HkConnectRow() }
        }
    }
}

@Composable
private fun AccountTabs(selected: Int, onSelect: (Int) -> Unit) {
    val tabs = listOf("普通", "信用", "期权")
    TabRow(
        selectedTabIndex = selected,
        containerColor = Color.White,
        contentColor = UpRed,
        indicator = { positions ->
            TabRowDefaults.SecondaryIndicator(
                Modifier.tabIndicatorOffset(positions[selected]),
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
                        title,
                        fontWeight = if (selected == index) FontWeight.Bold else FontWeight.Normal,
                        color = if (selected == index) UpRed else Color.Gray
                    )
                }
            )
        }
    }
}

@Composable
private fun WarningBanner() {
    Text(
        text = "当前系统清算中，资产及盈亏数据可能存在不准确的情况",
        color = Color(0xFF8D6E63),
        fontSize = 12.sp,
        modifier = Modifier
            .fillMaxWidth()
            .background(WarningBg)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    )
}

@Composable
private fun SelectedStockBanner(code: String, name: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("已选标的：", fontSize = 13.sp, color = Color.Gray)
            Text(
                if (name.isBlank()) code else "$name ($code)",
                fontWeight = FontWeight.Bold,
                color = UpRed,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun LoginCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("登录查看资产持仓", fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Text(
                "立即登录 >",
                color = UpRed,
                fontSize = 14.sp,
                modifier = Modifier.clickable { AppRouter.navigate(AppDestination.Profile) }
            )
        }
    }
}

@Composable
private fun TradeActionGrid() {
    val actions = listOf(
        Triple("买", Icons.Default.ShoppingCart, UpRed),
        Triple("卖", Icons.Default.SwapHoriz, Color(0xFF26A69A)),
        Triple("撤", Icons.Default.Undo, Color(0xFF42A5F5)),
        Triple("持", Icons.Default.Inventory, Color(0xFFFFA726)),
        Triple("委托查询", Icons.Default.ListAlt, Color(0xFF5C6BC0)),
        Triple("成交查询", Icons.Default.ReceiptLong, Color(0xFF26C6DA)),
        Triple("综合查询", Icons.Default.QueryStats, Color(0xFF66BB6A)),
        Triple("银证转账", Icons.Default.AccountBalance, Color(0xFFEC407A))
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            actions.chunked(4).forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    row.forEach { (title, icon, color) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .width(72.dp)
                                .clickable {
                                    when (title) {
                                        "买", "卖", "撤", "持" -> { /* stay on trading */ }
                                        else -> AppRouter.navigate(AppDestination.Profile)
                                    }
                                }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(color.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(icon, title, tint = color, modifier = Modifier.size(22.dp))
                            }
                            Spacer(Modifier.height(6.dp))
                            Text(title, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnalysisBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { AppRouter.navigate(AppDestination.Profile) },
        colors = CardDefaults.cardColors(containerColor = UpRed),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("普通账户盈亏分析", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text("去看看 >", color = Color.White.copy(alpha = 0.9f), fontSize = 13.sp)
        }
    }
}

@Composable
private fun FeatureTiles() {
    val tiles = listOf(
        Triple("今日打新", "新股 1 · 新债 0", Icons.Default.ReceiptLong),
        Triple("智能条件单", "策略下单更高效", Icons.Default.SmartToy),
        Triple("通用回购", "利率 1.435%", Icons.Default.AccountBalance),
        Triple("T0策略交易", "盘中快进快出", Icons.Default.SwapHoriz)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tiles.chunked(2).forEach { col ->
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                col.forEach { (title, subtitle, icon) ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(icon, null, tint = UpRed, modifier = Modifier.size(28.dp))
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text(title, fontWeight = FontWeight.Medium, fontSize = 13.sp)
                                Text(subtitle, fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HkConnectRow() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { AppRouter.navigate(AppDestination.Market) },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("港股通交易", fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
        }
    }
}
