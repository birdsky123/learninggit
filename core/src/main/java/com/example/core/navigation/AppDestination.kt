package com.example.core.navigation

/**
 * 跨 feature 导航的「能力目的地」。
 * feature 只描述“要去做什么”，不关心具体 route 字符串，也不依赖其他 feature。
 */
sealed class AppDestination {
    data object Home : AppDestination()
    data object Market : AppDestination()
    data object Trading : AppDestination()
    data object Wealth : AppDestination()
    data object Profile : AppDestination()

    /** 携带股票参数进入交易页 */
    data class TradeStock(
        val code: String,
        val name: String = ""
    ) : AppDestination()
}
