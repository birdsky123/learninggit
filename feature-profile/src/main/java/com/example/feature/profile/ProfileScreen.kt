package com.example.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Support
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.core.model.UserProfile
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val uiState = viewModel.uiState.collectAsState()
    Scaffold(topBar = {
        TopAppBar(title = { Text("我的") }, actions = { IconButton(onClick = { }) { Icon(Icons.Default.Settings, "设置") } })
    }) { padding ->
        ProfileContent(uiState = uiState.value, paddingValues = padding)
    }
}

@Composable
private fun UserInfoCard(profile: UserProfile) {
    Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            androidx.compose.material3.Surface(
                modifier = Modifier.size(60.dp).clip(shape = MaterialTheme.shapes.medium),
                color = MaterialTheme.colorScheme.primary
            ) { Icon(Icons.Default.Person, contentDescription = "头像", modifier = Modifier.padding(8.dp), tint = Color.White) }
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(8.dp))
            Column { Text(profile.name, fontSize = 20.sp, fontWeight = FontWeight.Bold); Text("普通会员", fontSize = 14.sp, color = Color.Gray) }
        }
    }
}

@Composable
private fun AssetInfoCard(profile: UserProfile) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("总资产", fontSize = 14.sp, color = Color.Gray)
            Text("¥${profile.totalAssets}", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                AssetItem("可用资金", profile.balance)
                AssetItem("持仓市值", profile.totalAssets - profile.balance)
            }
        }
    }
}

@Composable
private fun AssetItem(title: String, amount: Double) {
    Column { Text(title, fontSize = 12.sp, color = Color.Gray); Text("¥$amount", fontSize = 16.sp) }
}

@Composable
private fun FunctionList() {
    Column(modifier = Modifier.padding(16.dp)) {
        FunctionGroup(title = "交易管理", items = listOf(FunctionItem("交易记录", Icons.Default.History), FunctionItem("资金明细", Icons.Default.CreditCard), FunctionItem("银行卡管理", Icons.Default.CreditCard)))
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(8.dp))
        FunctionGroup(title = "安全中心", items = listOf(FunctionItem("实名认证", Icons.Default.VerifiedUser), FunctionItem("修改密码", Icons.Default.Lock), FunctionItem("风险评测", Icons.Default.Security)))
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(8.dp))
        FunctionGroup(title = "其他服务", items = listOf(FunctionItem("在线客服", Icons.Default.Support), FunctionItem("关于我们", Icons.Default.Info), FunctionItem("设置", Icons.Default.Settings)))
    }
}

@Composable
private fun FunctionGroup(title: String, items: List<FunctionItem>) {
    Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))
    Card { Column { items.forEachIndexed { index, item -> FunctionRow(item); if (index < items.size - 1) androidx.compose.material3.Divider() } } }
}

@Composable
private fun FunctionRow(item: FunctionItem) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(item.icon, contentDescription = item.title, tint = MaterialTheme.colorScheme.primary)
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(8.dp))
            Text(item.title)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = "进入", tint = Color.Gray)
    }
}

private data class FunctionItem(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
private fun ProfileContent(uiState: ProfileUiState, paddingValues: PaddingValues) {
    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }
    uiState.errorMessage?.let { message ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) { Text(message, color = Color.Red) }
        return
    }
    val profile = uiState.profile ?: return
    LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
        item { UserInfoCard(profile) }
        item { AssetInfoCard(profile) }
        item { FunctionList() }
    }
}








