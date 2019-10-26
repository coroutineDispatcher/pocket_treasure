package com.stavro_xhardha.core_module.core_dependencies

import kotlinx.coroutines.CoroutineDispatcher

class AppCoroutineDispatchers(
    val ioDispatcher: CoroutineDispatcher,
    val mainDispatcher: CoroutineDispatcher
)