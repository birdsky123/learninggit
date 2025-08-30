package com.example.feature.wealth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WealthScreen(viewModel: WealthViewModel = viewModel()) {
    val uiState = viewModel.uiState.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("理财") }) }) { padding ->
        WealthContent(
            uiState = uiState.value,
            onTabSelected = { viewModel.selectTab(it) },
            paddingValues = padding
        )
    }
}

@Composable
private fun WealthOverview() {
    Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("总资产", fontSize = 14.sp, color = Color.Gray)
            Text("¥128,500.00", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                AssetItem("理财资产", "50,000.00")
                AssetItem("基金资产", "48,500.00")
                AssetItem("保险资产", "30,000.00")
            }
        }
    }
}

@Composable
private fun AssetItem(title: String, amount: String) {
    Column { Text(title, fontSize = 12.sp, color = Color.Gray); Text("¥$amount", fontSize = 16.sp) }
}

@Composable
private fun WealthProductCard(product: FinancialProduct) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(product.name, fontWeight = FontWeight.Bold)
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(top = 4.dp))
                Text("${product.period} | ${product.riskLevel}", fontSize = 12.sp, color = Color.Gray)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text("${product.expectedReturn}", fontSize = 24.sp, color = Color(0xFFE65100), fontWeight = FontWeight.Bold)
                        Text("%", fontSize = 12.sp, color = Color(0xFFE65100), modifier = Modifier.padding(bottom = 4.dp))
                    }
                    Text("预期年化", fontSize = 12.sp, color = Color.Gray)
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "查看详情", tint = Color.Gray)
                }
            }
        }
    }
}

@Composable
private fun WealthContent(
    uiState: WealthUiState,
    onTabSelected: (Int) -> Unit,
    paddingValues: PaddingValues
) {
    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }
    uiState.errorMessage?.let { message ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) { Text(message, color = Color.Red) }
        return
    }
    Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
        WealthOverview()
        TabRow(selectedTabIndex = uiState.selectedTabIndex) {
            uiState.tabs.forEachIndexed { index, title ->
                Tab(selected = uiState.selectedTabIndex == index, onClick = { onTabSelected(index) }, text = { Text(title) })
            }
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item { Text("热门产品", modifier = Modifier.padding(16.dp), fontSize = 18.sp, fontWeight = FontWeight.Bold) }
            items(uiState.products) { product -> WealthProductCard(product) }
        }
    }
}








