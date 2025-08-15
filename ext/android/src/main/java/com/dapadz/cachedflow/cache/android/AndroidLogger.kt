package com.dapadz.cachedflow.cache.android

import android.util.Log
import com.dapadz.cachedflow.logger.Logger

class AndroidLogger: Logger {
    override fun error(tag: String, message: String) {
        Log.e(tag, "⛔ $message")
    }

    override fun info(tag: String, message: String) {
        Log.i(tag, "ℹ️ $message")
    }
}