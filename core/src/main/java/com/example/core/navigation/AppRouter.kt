package com.example.core.navigation
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * 跨模块路由总线。
 *
 * feature 调用 [navigate] 发出意图；:app 在 NavHost 侧 collect [commands] 并执行真正跳转。
 * 这样 feature 之间零依赖，也无需持有 NavController。
 */
object AppRouter {
    private val _commands = MutableSharedFlow<AppDestination>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val commands: SharedFlow<AppDestination> = _commands.asSharedFlow()

    fun navigate(destination: AppDestination) {
        _commands.tryEmit(destination)
    }
}
