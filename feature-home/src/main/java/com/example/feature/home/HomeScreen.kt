package com.example.feature.home
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.navigation.AppDestination
import com.example.core.navigation.AppRouter

private val UpRed = Color(0xFFE53935)
private val DownGreen = Color(0xFF26A69A)
private val PageBg = Color(0xFFF5F5F5)

private data class QuickEntry(
    val title: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg)
    ) {
        HomeTopBar()
        when {
            uiState.isLoading -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(color = UpRed) }
            uiState.errorMessage != null -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { Text(text = uiState.errorMessage!!, color = UpRed) }
            else -> HomeBody()
        }
    }
}

@Composable
private fun HomeTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(UpRed)
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Row(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(alpha = 0.2f))
                .clickable { AppRouter.navigate(AppDestination.Market) }
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "股票 | 理财 | 功能", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
        }
        IconButton(onClick = { }) {
            Icon(Icons.Default.Settings, contentDescription = "设置", tint = Color.White)
        }
        IconButton(onClick = { }) {
            Icon(Icons.Default.Message, contentDescription = "消息", tint = Color.White)
        }
    }
}

@Composable
private fun HomeBody() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item { QuickEntryGrid() }
        item { PromoBanner() }
        item { MarketOverviewCard() }
        item { HotSectorsCard() }
        item { MarketMovesCard() }
    }
}

@Composable
private fun QuickEntryGrid(
    entries: List<QuickEntry> = defaultQuickEntries,
    columnsPerRow: Int = 5
) {
    val rows = remember(entries, columnsPerRow) {
        entries.chunked(columnsPerRow.coerceAtLeast(1))
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        rows.forEach { row ->
            QuickEntryRow(entries = row, columns = columnsPerRow)
        }
    }
}

@Composable
private fun QuickEntryRow(entries: List<QuickEntry>, columns: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        entries.forEach { entry ->
            QuickEntryItem(
                entry = entry,
                modifier = Modifier.weight(1f)
            )
        }
        // 最后一行不足 columns 时补空位，保持列宽与上行一致
        val emptySlots = (columns - entries.size).coerceAtLeast(0)
        repeat(emptySlots) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun QuickEntryItem(entry: QuickEntry, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable {
                when (entry.title) {
                    "业务办理" -> AppRouter.navigate(AppDestination.Profile)
                    "账户分析", "走势预测", "云参选股", "趋势九转" ->
                        AppRouter.navigate(AppDestination.Trading)
                    "全部", "投教专区", "投顾专区" ->
                        AppRouter.navigate(AppDestination.Market)
                    else -> AppRouter.navigate(AppDestination.Trading)
                }
            }
            .padding(horizontal = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(entry.color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                entry.icon,
                contentDescription = entry.title,
                tint = entry.color,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = entry.title,
            fontSize = 11.sp,
            color = Color(0xFF424242),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private val defaultQuickEntries = listOf(
    QuickEntry("闪电开户", Icons.Default.Bolt, Color(0xFFFF7043)),
    QuickEntry("业务办理", Icons.Default.BusinessCenter, Color(0xFF42A5F5)),
    QuickEntry("投顾专区", Icons.Default.People, Color(0xFF5C6BC0)),
    QuickEntry("趋势九转", Icons.Default.ShowChart, Color(0xFFEF5350)),
    QuickEntry("投教专区", Icons.Default.School, Color(0xFF26A69A)),
    QuickEntry("账户分析", Icons.Default.Analytics, Color(0xFF26C6DA)),
    QuickEntry("走势预测", Icons.Default.Timeline, Color(0xFF66BB6A)),
    QuickEntry("云参选股", Icons.Default.Cloud, Color(0xFFAB47BC)),
    QuickEntry("全部", Icons.Default.MoreHoriz, Color(0xFF78909C))
)

@Composable
private fun PromoBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clickable { AppRouter.navigate(AppDestination.Trading) },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "融资融券", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF5D4037))
                Text(text = "预约开户享专属服务", fontSize = 13.sp, color = Color(0xFF8D6E63))
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(UpRed)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(text = "去开户", color = Color.White, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun MarketOverviewCard() {
    val indices = listOf(
        Triple("上证指数", "3,892.35", "+0.82%"),
        Triple("深证成指", "12,456.78", "-0.35%"),
        Triple("创业板指", "2,567.89", "+1.24%")
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "大盘行情", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    text = "查看详情 >",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { AppRouter.navigate(AppDestination.Market) }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                indices.forEach { (name, value, change) ->
                    val up = change.startsWith("+")
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = name, fontSize = 12.sp, color = Color.Gray)
                        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text(text = change, color = if (up) UpRed else DownGreen, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun HotSectorsCard() {
    val sectors = listOf(
        "人工智能" to "+3.2%",
        "新能源车" to "+2.1%",
        "半导体" to "-0.8%",
        "白酒" to "+1.5%"
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "热点板块", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                sectors.forEach { (name, change) ->
                    val up = change.startsWith("+")
                    Column(
                        modifier = Modifier
                            .width(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF5F5F5))
                            .clickable { AppRouter.navigate(AppDestination.Market) }
                            .padding(12.dp)
                    ) {
                        Text(text = name, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        Text(
                            text = change,
                            color = if (up) UpRed else DownGreen,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MarketMovesCard() {
    val moves = listOf(
        "贵州茅台拉升涨超2%" to "14:32",
        "宁德时代放量下跌" to "14:28",
        "沪指重回3900点" to "14:15"
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "大盘异动", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = UpRed, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            moves.forEach { (title, time) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            AppRouter.navigate(AppDestination.TradeStock("600519", "贵州茅台"))
                        }
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = title, fontSize = 14.sp, modifier = Modifier.weight(1f))
                    Text(text = time, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}
