package com.example.core.repository

import com.example.core.model.UserProfile
import kotlinx.coroutines.delay

object ProfileRepository {
    suspend fun getUserProfile(): UserProfile {
        delay(1000)
        return UserProfile(
            name = "张三",
            avatar = "",
            balance = 50000.0,
            totalAssets = 128500.0
        )
    }
}



