package com.example.core.repository

import com.example.core.model.StockInfo
import kotlinx.coroutines.delay

object MarketRepository {
    suspend fun getStocksByMarket(market: String): List<StockInfo> {
        delay(900)
        return when (market) {
            "沪深" -> listOf(
                StockInfo("贵州茅台", "600519", 1789.99, 2.8),
                StockInfo("宁德时代", "300750", 156.78, -1.5),
                StockInfo("招商银行", "600036", 34.56, 0.8)
            )
            "港股" -> listOf(
                StockInfo("腾讯控股", "0700", 320.8, -1.2),
                StockInfo("阿里巴巴-SW", "9988", 78.5, 1.4)
            )
            else -> listOf(
                StockInfo("苹果", "AAPL", 190.3, 0.4),
                StockInfo("特斯拉", "TSLA", 175.9, -2.2)
            )
        }
    }
}



