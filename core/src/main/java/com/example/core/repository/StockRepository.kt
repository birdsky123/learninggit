package com.example.core.repository

import com.example.core.model.FinancialProduct
import com.example.core.model.StockInfo
import kotlinx.coroutines.delay

object StockRepository {
    suspend fun getHotStocks(): List<StockInfo> {
        delay(1200)
        return listOf(
            StockInfo("阿里巴巴", "BABA", 88.5, 2.3),
            StockInfo("腾讯控股", "0700", 320.8, -1.2),
            StockInfo("美团", "3690", 120.5, 0.8)
        )
    }

    suspend fun getRecommendedProducts(): List<FinancialProduct> {
        delay(800)
        return listOf(
            FinancialProduct("稳健理财A", 4.5, "3个月", "低风险"),
            FinancialProduct("成长基金B", 8.2, "6个月", "中风险"),
            FinancialProduct("高收益C", 12.0, "12个月", "高风险")
        )
    }
}



