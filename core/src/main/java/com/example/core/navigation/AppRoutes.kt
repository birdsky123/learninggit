package com.example.core.navigation

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * NavHost 路由表与路径拼装。仅 app 组装图时与 [AppRouter] 映射时使用；
 * feature 侧优先使用 [AppDestination]，避免散落魔法字符串。
 */
object AppRoutes {
    const val HOME = "home"
    const val MARKET = "market"
    const val TRADING = "trading"
    const val WEALTH = "wealth"
    const val PROFILE = "profile"

    /** Trading 支持可选 query：code / name */
    const val TRADING_PATTERN = "trading?code={code}&name={name}"

    fun trading(code: String = "", name: String = ""): String {
        val encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString())
        return "trading?code=$code&name=$encodedName"
    }

    fun of(destination: AppDestination): String = when (destination) {
        AppDestination.Home -> HOME
        AppDestination.Market -> MARKET
        AppDestination.Trading -> trading()
        AppDestination.Wealth -> WEALTH
        AppDestination.Profile -> PROFILE
        is AppDestination.TradeStock -> trading(destination.code, destination.name)
    }

    fun baseRoute(route: String?): String? = route?.substringBefore("?")
}
