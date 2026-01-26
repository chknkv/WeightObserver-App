package com.chknkv.weightobserver

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import com.arkivanov.decompose.retainedComponent
import com.chknkv.coresession.LanguageManager
import org.koin.android.ext.android.get
import org.koin.core.context.GlobalContext
import java.util.Locale

/**
 * Main android Activity of WeightObserver.
 */
class WeightObserverMainActivity : FragmentActivity() {

    private var lastUiModeNight: Int = Configuration.UI_MODE_NIGHT_UNDEFINED
    private lateinit var rootComponent: WeightObserverRootComponent

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

        lastUiModeNight = applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        rootComponent = retainedComponent {
            WeightObserverRootComponentImpl(
                componentContext = it,
                sessionRepository = get()
            )
        }
        setAppContent()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val newUiModeNight = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (newUiModeNight != lastUiModeNight) {
            lastUiModeNight = newUiModeNight
            setAppContent()
        }
    }

    private fun setAppContent() {
        val isDarkTheme = lastUiModeNight == Configuration.UI_MODE_NIGHT_YES
        setContent { WeightObserverAppUiRoot(rootComponent, darkTheme = isDarkTheme) }
    }
}
