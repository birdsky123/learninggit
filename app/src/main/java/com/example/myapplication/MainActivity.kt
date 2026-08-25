package com.example.myapplication
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.core.navigation.AppDestination
import com.example.core.navigation.AppRouter
import com.example.core.navigation.AppRoutes
import com.example.feature.home.HomeScreen
import com.example.feature.market.MarketScreen
import com.example.feature.profile.ProfileScreen
import com.example.feature.trading.TradingScreen
import com.example.feature.wealth.WealthScreen
import com.example.myapplication.navigation.BottomNavItem
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainScreen()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        MyApplicationTheme { MainScreen() }
    }

    @Composable
    fun MainScreen() {
        val navController = rememberNavController()
        BindAppRouter(navController)
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                BottomNavigationBar(navController)
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = AppRoutes.HOME,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(AppRoutes.HOME) { HomeScreen() }
                composable(AppRoutes.MARKET) { MarketScreen() }
                composable(
                    route = AppRoutes.TRADING_PATTERN,
                    arguments = listOf(
                        navArgument("code") { type = NavType.StringType; defaultValue = "" },
                        navArgument("name") { type = NavType.StringType; defaultValue = "" }
                    )
                ) { entry ->
                    val code = entry.arguments?.getString("code").orEmpty()
                    val rawName = entry.arguments?.getString("name").orEmpty()
                    val name = runCatching {
                        URLDecoder.decode(rawName, StandardCharsets.UTF_8.toString())
                    }.getOrDefault(rawName)
                    TradingScreen(
                        stockCode = code.takeIf { it.isNotBlank() },
                        stockName = name.takeIf { it.isNotBlank() }
                    )
                }
                composable(AppRoutes.WEALTH) { WealthScreen() }
                composable(AppRoutes.PROFILE) { ProfileScreen() }
            }
        }
    }

    @Composable
    private fun BindAppRouter(navController: NavHostController) {
        LaunchedEffect(navController) {
            AppRouter.commands.collect { destination ->
                val route = AppRoutes.of(destination)
                val keepTabState = destination !is AppDestination.TradeStock
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = keepTabState }
                    launchSingleTop = true
                    restoreState = keepTabState
                }
            }
        }
    }

    @Composable
    fun BottomNavigationBar(navController: NavHostController) {
        val items = listOf(
            BottomNavItem.Home,
            BottomNavItem.Market,
            BottomNavItem.Trading,
            BottomNavItem.Wealth,
            BottomNavItem.Profile
        )
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentBase = AppRoutes.baseRoute(navBackStackEntry?.destination?.route)
        val red = MaterialTheme.colorScheme.primary

        NavigationBar(containerColor = Color.White) {
            items.forEach { item ->
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.title) },
                    label = { Text(item.title) },
                    selected = currentBase == item.route,
                    onClick = { AppRouter.navigate(item.destination) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = red,
                        selectedTextColor = red,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        }
    }
}
