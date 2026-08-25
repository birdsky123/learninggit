package com.example.myapplication.navigation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.core.navigation.AppDestination
import com.example.core.navigation.AppRoutes

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val destination: AppDestination
) {
    data object Home : BottomNavItem(
        AppRoutes.HOME, "首页", Icons.Default.Home, AppDestination.Home
    )
    data object Market : BottomNavItem(
        AppRoutes.MARKET, "行情", Icons.Default.ShowChart, AppDestination.Market
    )
    data object Trading : BottomNavItem(
        AppRoutes.TRADING, "交易", Icons.Default.SwapHoriz, AppDestination.Trading
    )
    data object Wealth : BottomNavItem(
        AppRoutes.WEALTH, "理财", Icons.Default.Wallet, AppDestination.Wealth
    )
    data object Profile : BottomNavItem(
        AppRoutes.PROFILE, "我的", Icons.Default.AccountCircle, AppDestination.Profile
    )
}
