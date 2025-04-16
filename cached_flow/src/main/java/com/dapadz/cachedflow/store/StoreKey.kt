package com.dapadz.cachedflow.store

import kotlin.reflect.KClass

class StoreKey<T : Any>(
    private val name: String,
    private val type: KClass<T>
)