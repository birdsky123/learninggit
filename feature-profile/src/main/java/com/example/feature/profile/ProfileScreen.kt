package com.example.feature.profile

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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg)
    ) {
        when {
            uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = UpRed)
            }
            uiState.errorMessage != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(uiState.errorMessage!!, color = UpRed)
            }
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item { ProfileHeader(uiState.profile?.name ?: "用户") }
                item { StatsRow() }
                item { WarningBanner() }
                item { LoginAssetBanner() }
                item { PromoBanner() }
                item { BusinessGrid() }
            }
        }
    }
}

@Composable
private fun ProfileHeader(name: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(UpRed)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { }) {
                Icon(Icons.Default.SupportAgent, "客服", tint = Color.White)
            }
            IconButton(onClick = { }) {
                Icon(Icons.Default.Settings, "设置", tint = Color.White)
            }
            IconButton(onClick = { }) {
                Icon(Icons.Default.Message, "消息", tint = Color.White)
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(32.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("199****5997", color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    .clickable { }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Edit, null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("资料管理", color = Color.White, fontSize = 12.sp)
                }
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun StatsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(UpRed)
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        listOf("待办" to "0", "点赞" to "8", "收藏" to "4").forEach { (label, value) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(label, color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun WarningBanner() {
    Text(
        "当前系统清算中，资产及盈亏数据可能存在不准确的情况",
        color = Color(0xFF8D6E63),
        fontSize = 12.sp,
        modifier = Modifier
            .fillMaxWidth()
            .background(WarningBg)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    )
}

@Composable
private fun LoginAssetBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
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
            Text("登录后可查看我的总资产", color = Color.White, fontSize = 14.sp)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .clickable { }
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text("登录", color = UpRed, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun PromoBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clickable { AppRouter.navigate(AppDestination.Trading) },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("融资融券预约开户", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text("专属客户经理一对一服务", fontSize = 12.sp, color = Color.Gray)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
        }
    }
}

@Composable
private fun BusinessGrid() {
    val items = listOf(
        "北交所权限" to Icons.Default.VerifiedUser,
        "风险测评" to Icons.Default.Security,
        "重置手机号码" to Icons.Default.PhoneAndroid,
        "港股通权限" to Icons.Default.Fingerprint,
        "修改密码" to Icons.Default.Lock,
        "科创板权限" to Icons.Default.Key,
        "可转债权限" to Icons.Default.VerifiedUser,
        "创业板权限" to Icons.Default.Security,
        "新三板权限" to Icons.Default.Key,
        "电子签名业务" to Icons.Default.Edit
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
                Text("业务办理", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("更多 >", color = Color.Gray, fontSize = 13.sp)
            }
            Spacer(Modifier.height(16.dp))
            items.chunked(5).forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    row.forEach { (title, icon) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .width(64.dp)
                                .clickable {
                                    when {
                                        title.contains("港股") -> AppRouter.navigate(AppDestination.Trading)
                                        title.contains("风险") -> AppRouter.navigate(AppDestination.Trading)
                                        else -> { }
                                    }
                                }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(UpRed.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(icon, title, tint = UpRed, modifier = Modifier.size(20.dp))
                            }
                            Spacer(Modifier.height(6.dp))
                            Text(title, fontSize = 11.sp, maxLines = 1)
                        }
                    }
                    repeat(5 - row.size) {
                        Spacer(modifier = Modifier.width(64.dp))
                    }
                }
            }
        }
    }
}
