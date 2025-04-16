# CachedFlow

**A lightweight Kotlin Multiplatform caching library** designed to work using a key-based strategy system and flexible caching policies.

---

## Features

- Simple key-based cache access using typed keys
- Customizable caching strategies
- Supports suspending and flow-based data operations
- Pluggable storage backend (`Store`) for full platform flexibility
- Built-in logging interface
- Fully testable and decoupled from Android dependencies

---

## Installation

> Add the library to your Kotlin Multiplatform project (available soon via Maven Central / GitHub Packages).

<details>
<summary>Gradle (Kotlin DSL)</summary>

```kotlin
dependencies {
    implementation("com.dapadz:cachedflow:<version>")
}
```

</details>

---

## Getting Started

### 1. Create your own `Store` implementation

The `Store` interface abstracts the storage layer:

```kotlin
interface Store {
    suspend fun <T: Any> get(key: StoreKey<T>): Flow<T?>
    suspend fun <T: Any> save(key: StoreKey<T>, value: T)
    suspend fun <T: Any> delete(key: StoreKey<T>)
    suspend fun clear()
}
```

Implement it using your platform’s local storage (e.g. `DataStore`, `SharedPreferences`, `NSUserDefaults`, file system, etc).

---

### 2. Initialize the Cache

```kotlin
fun main() {
    val store: Store = MyMultiplatformStore()
    Cache.initialize(store)
}
```

Optionally, provide a custom `Logger`:

```kotlin
Cache.initialize(store, logger = MyLogger())
```

---

### 3. Define and Use Cache Keys

Use built-in key helpers or define your own:

```kotlin
val userKey = stringCacheKey("user_profile")
val ageKey = integerCacheKey("user_age")
```

---

### 4. Cache a Flow with Strategy

```kotlin
flow { emit(fetchUserProfileFromApi()) }
    .cache(userKey, CacheStrategyType.IF_HAVE)
    .collect { user -> println("User: $user") }
```

---

## Cache Strategies

Choose how the cache behaves during a Flow emission:

| Strategy            | Description                                                  |
|---------------------|--------------------------------------------------------------|
| `ONLY_CACHE`        | Always use the cache. Throws if no value exists.             |
| `ONLY_REQUEST`      | Always skip cache. Fetch fresh and optionally cache it.      |
| `IF_HAVE` *(default)* | Use cache if available, otherwise fallback to the flow.     |

#### Extend with Your Own CacheStrategy

Implement the `CacheStrategy<T>` interface for full control:

```kotlin
abstract class CacheStrategy <T> (
    protected val key: Key<T>,
    protected val cachedAfterLoad : Boolean
) {
    abstract suspend fun execute(currentFlow: Flow<T>): Flow<T>
}
```

---

## Keys

Use the following factory functions to quickly define typed keys for common primitive types:

| Key Type | Factory Function         | Example                                  |
|----------|--------------------------|------------------------------------------|
| `String` | `stringCacheKey(name)`   | `val key = stringCacheKey("username")`   |
| `Int`    | `integerCacheKey(name)`  | `val key = integerCacheKey("user_age")`  |
| `Float`  | `floatCacheKey(name)`    | `val key = floatCacheKey("user_score")`  |
| `Boolean`| `booleanCacheKey(name)`  | `val key = booleanCacheKey("is_logged")` |

These keys inherit from `Key<T>` and include built-in logic to handle type-safe caching operations.

#### Custom Key Example

You can also define your own cache key for complex or custom types:

```kotlin
class MyKey(name: String): Key<MyType>(name) {
    override fun isTypeOf(valueClass: KClass<*>) = valueClass == MyType::class
    override suspend fun getFromStore(store: Store): Flow<MyType?> = ...
    override suspend fun saveToStore(item: MyType, store: Store) = ...
}
```