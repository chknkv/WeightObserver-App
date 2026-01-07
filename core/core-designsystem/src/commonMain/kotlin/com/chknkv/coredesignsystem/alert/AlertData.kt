package com.chknkv.coredesignsystem.alert

import androidx.compose.runtime.Stable
import com.chknkv.coredesignsystem.buttons.AcButtonStyle
import weightobserver_project.core.core_designsystem.generated.resources.Res
import weightobserver_project.core.core_designsystem.generated.resources.ill_256_warning
import org.jetbrains.compose.resources.DrawableResource

/**
 * @property title The main title text displayed at the top of the dialog.
 * @property subtitle The secondary text providing additional information or context.
 * @property image An optional image or icon displayed in the dialog.
 * @property firstButtonText The text label for the primary action button.
 * @property secondButtonText The optional text label for the secondary action button.
 * @property firstButtonStyle The visual style of the primary action button.
 * @property secondButtonStyle The visual style of the secondary action button.
 */
@Stable
data class AlertData(
    val title: String,
    val subtitle: String,
    val image: DrawableResource? = null,
    val firstButtonText: String,
    val secondButtonText: String? = null,
    val firstButtonStyle: AcButtonStyle = AcButtonStyle.Standard,
    val secondButtonStyle: AcButtonStyle = AcButtonStyle.Transparent,
) {

    companion object Companion {

        /**
         * Provides a default alert dialog configuration used for unexpected or general errors.
         *
         * @return A preconfigured [AlertData] instance for displaying error messages.
         */
        fun defaultThrowableAlertDialogData(): AlertData = AlertData(
            title = "Произошла ошибка",
            image = Res.drawable.ill_256_warning,
            subtitle = "Проверьте корректность введенных данных и сетевое подключение",
            firstButtonText = "Понятно"
        )
    }
}