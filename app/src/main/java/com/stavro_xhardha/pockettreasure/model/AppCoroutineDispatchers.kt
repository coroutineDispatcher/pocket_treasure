package com.stavro_xhardha.pockettreasure.model

import kotlinx.coroutines.CoroutineDispatcher

class AppCoroutineDispatchers(
    val ioDispatcher: CoroutineDispatcher,
    val unconfirmedDispatcher: CoroutineDispatcher,
    val mainDispatcher: CoroutineDispatcher,
    val defaultDispatcher: CoroutineDispatcher
)