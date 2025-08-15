package com.dapadz.cachedflow.cache.keys

import com.dapadz.cachedflow.store.Store
import com.dapadz.cachedflow.store.StoreKey
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

/**
 * The caching key for the [Boolean] type.
 *
 * @param name key name
 * @see BooleanKey
 */
fun booleanCacheKey(name: String):BooleanKey = BooleanKey(name)

/**
 * A class for saving an item of type [Boolean] to the cache.
 *
 * @param name key name
 * @see Key
 * @see booleanCacheKey
 */
class BooleanKey(name: String): Key<Boolean>(name) {
    override fun isTypeOf(valueClass: KClass<*>): Boolean = valueClass == Int::class
    override suspend fun getFromStore(store: Store): Flow<Boolean?> = store.get(StoreKey(name, Boolean::class))
    override suspend fun saveToStore(item: Boolean, store: Store) = store.save(StoreKey(name, Boolean::class), item)
}