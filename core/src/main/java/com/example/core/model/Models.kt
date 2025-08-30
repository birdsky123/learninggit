package com.example.core.model

data class StockInfo(
    val name: String,
    val code: String,
    val price: Double,
    val changePercent: Double
)

data class FinancialProduct(
    val name: String,
    val expectedReturn: Double,
    val period: String,
    val riskLevel: String
)

data class TradeRecord(
    val type: String,
    val amount: Double,
    val time: String,
    val status: String
)

data class UserProfile(
    val name: String,
    val avatar: String,
    val balance: Double,
    val totalAssets: Double
)



