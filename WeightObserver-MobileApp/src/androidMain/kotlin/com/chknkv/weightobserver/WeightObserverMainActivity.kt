package com.chknkv.weightobserver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.retainedComponent
import org.koin.android.ext.android.get

/**
 * Main android Activity of WeightObserver.
 */
class WeightObserverMainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val rootComponent = retainedComponent {
            WeightObserverRootComponentImpl(
                componentContext = it,
                sessionRepository = get()
            )
        }
        setContent { WeightObserverAppUiRoot(rootComponent) }
    }
}
