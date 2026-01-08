package com.chknkv.weightobserver

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.chknkv.coresession.SessionRepository
import com.chknkv.coresession.coreSessionModule
import com.chknkv.coresession.coreStorageModule
import com.chknkv.feature.main.di.featureMainModule
import com.chknkv.feature.welcome.di.featureWelcomeModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin

/**
 * iOS ViewController of WeightObserver.
 * The main and only entry point to the application.
 */
fun WeightObserverMainViewController() = ComposeUIViewController(configure = { startKoinDI() }) {
    val root = remember {
        Napier.base(DebugAntilog())

        val sessionRepository = object : KoinComponent { val repo: SessionRepository = get() }.repo

        WeightObserverRootComponentImpl(
            componentContext = DefaultComponentContext(LifecycleRegistry()),
            sessionRepository = sessionRepository
        )
    }
    WeightObserverAppUiRoot(root)
}

/** Initialization Koin */
private fun startKoinDI() = startKoin {
    modules(
        coreSessionModule,
        coreStorageModule,
        featureWelcomeModule,
        featureMainModule
    )
}
