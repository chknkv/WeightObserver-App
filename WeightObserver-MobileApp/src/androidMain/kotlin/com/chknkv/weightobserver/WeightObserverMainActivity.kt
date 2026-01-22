package com.chknkv.weightobserver

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.retainedComponent
import com.chknkv.coresession.LanguageManager
import org.koin.android.ext.android.get
import org.koin.core.context.GlobalContext
import java.util.Locale

/**
 * Main android Activity of WeightObserver.
 */
class WeightObserverMainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        val context = try {
            val languageManager = GlobalContext.get().get<LanguageManager>()
            val languageCode = languageManager.getCurrentLanguageCode()
            val locale = Locale(languageCode)
            Locale.setDefault(locale)

            val config = newBase.resources.configuration
            config.setLocale(locale)
            config.setLayoutDirection(locale)
            newBase.createConfigurationContext(config)
        } catch (_: Exception) {
            newBase
        }
        super.attachBaseContext(context)
    }

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
