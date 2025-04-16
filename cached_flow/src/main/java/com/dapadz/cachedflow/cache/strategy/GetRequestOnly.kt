package com.dapadz.cachedflow.cache.strategy

import com.dapadz.cachedflow.cache.keys.Key
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import com.dapadz.cachedflow.cache.Cache


/**
 * Will return only the request data
 */
class GetRequestOnly <T> (
    key: Key<T>,
    cachedAfterLoad: Boolean
): CacheStrategy<T>(key, cachedAfterLoad) {

    override suspend fun execute(currentFlow: Flow<T>): Flow<T> = currentFlow.onEach {
        if (cachedAfterLoad) {
            Cache.saveToCache(key, it)
        }
    }
}