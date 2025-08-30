package com.example.core.repository

import com.example.core.model.FinancialProduct
import kotlinx.coroutines.delay

object WealthRepository {
    suspend fun getProductsByCategory(category: String): List<FinancialProduct> {
        delay(700)
        return listOf(
            FinancialProduct("稳健理财A", 4.5, "3个月", "低风险"),
            FinancialProduct("成长基金B", 8.2, "6个月", "中风险"),
            FinancialProduct("高收益C", 12.0, "12个月", "高风险")
        )
    }
}



