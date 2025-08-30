package com.example.core.repository

import com.example.core.model.TradeRecord
import kotlinx.coroutines.delay

object TradingRepository {
    suspend fun getRecentTrades(): List<TradeRecord> {
        delay(600)
        return listOf(
            TradeRecord("买入", 10000.0, "2024-03-20 14:30", "已成交"),
            TradeRecord("卖出", 5000.0, "2024-03-20 11:15", "已成交"),
            TradeRecord("买入", 8000.0, "2024-03-19 15:45", "已撤单")
        )
    }
}



