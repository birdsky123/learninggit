package com.example.feature.wealth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.model.FinancialProduct
import com.example.core.navigation.AppDestination
import com.example.core.navigation.AppRouter

private val UpRed = Color(0xFFE53935)
private val PageBg = Color(0xFFF5F5F5)

@Composable
fun WealthScreen(viewModel: WealthViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg)
    ) {
        WealthTopBar()
        when {
            uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = UpRed)
            }
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item { LoginAssetBanner() }
                item { WealthEntryGrid() }
                item { FeaturedFundCard() }
                item {
                    SmartFundSection(
                        products = uiState.products.ifEmpty {
                            listOf(
                                FinancialProduct("安信活期宝", 1.2760, "货币型", "低风险"),
                                FinancialProduct("稳健理财A", 4.5, "固收+", "低风险"),
                                FinancialProduct("成长基金B", 8.2, "混合型", "中风险")
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun WealthTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("理财", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Text(
            "自选",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier
                .clickable { }
                .padding(horizontal = 8.dp)
        )
        IconButton(onClick = { AppRouter.navigate(AppDestination.Market) }) {
            Icon(Icons.Default.Search, "搜索")
        }
        IconButton(onClick = { }) {
            Icon(Icons.Default.Message, "消息")
        }
    }
}

@Composable
private fun LoginAssetBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("登录查看我的资产详情", fontSize = 14.sp)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(UpRed)
                    .clickable { AppRouter.navigate(AppDestination.Profile) }
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text("立即登录", color = Color.White, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun WealthEntryGrid() {
    val entries = listOf(
        "高端理财" to Icons.Default.Wallet,
        "基金专区" to Icons.Default.TrendingUp,
        "定期产品" to Icons.Default.Savings,
        "基金排行" to Icons.Default.Leaderboard,
        "活期+" to Icons.Default.AccountBalanceWallet,
        "自选基金" to Icons.Default.Star
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            entries.forEach { (title, icon) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(64.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(UpRed.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, title, tint = UpRed, modifier = Modifier.size(22.dp))
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(title, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun FeaturedFundCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(listOf(Color(0xFFE53935), Color(0xFFFF7043))),
                    RoundedCornerShape(12.dp)
                )
                .padding(20.dp)
        ) {
            Column {
                Text("安信新价值混合A", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(6.dp))
                Text("把握成长机遇 · 精选优质标的", color = Color.White.copy(alpha = 0.9f), fontSize = 13.sp)
                Spacer(Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .clickable { }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("立即查看", color = UpRed, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun SmartFundSection(products: List<FinancialProduct>) {
    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
        Text("智选基金", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(vertical = 8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(products) { product ->
                Card(
                    modifier = Modifier.width(160.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(product.name, fontWeight = FontWeight.Medium, fontSize = 14.sp, maxLines = 1)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "${product.expectedReturn}%",
                            color = UpRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text("7日年化", fontSize = 11.sp, color = Color.Gray)
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "${product.period} · ${product.riskLevel}",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}
