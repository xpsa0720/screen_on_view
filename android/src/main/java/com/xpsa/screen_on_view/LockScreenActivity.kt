package com.xpsa.screen_on_view

import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity

class LockScreenActivity : FlutterActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    companion object{
        fun withCachedEngine(cachedEngineId: String): CachedEngineIntentBuilder {
            return CachedEngineIntentBuilder(LockScreenActivity::class.java, cachedEngineId)
        }
    }
}