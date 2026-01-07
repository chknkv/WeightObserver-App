package com.chknkv.weightobserver

import android.app.Application
import com.chknkv.coresession.coreSessionModule
import com.chknkv.coresession.coreStorageModule
import com.chknkv.feature.main.di.featureMainModule
import com.chknkv.feature.welcome.di.featureWelcomeModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Android application of WeightObserver.
 * The main and only entry point to the application.
 */
class WeightObserverApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        com.chknkv.coreutils.appContext = this
        Napier.base(DebugAntilog())

        startKoin {
            androidContext(this@WeightObserverApplication)
            modules(
                coreSessionModule,
                coreStorageModule,
                featureWelcomeModule,
                featureMainModule
            )
        }
    }
}
