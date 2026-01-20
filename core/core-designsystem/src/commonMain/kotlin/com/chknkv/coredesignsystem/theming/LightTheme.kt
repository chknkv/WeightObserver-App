package com.chknkv.coredesignsystem.theming

import androidx.compose.ui.graphics.Color
import com.chknkv.coredesignsystem.*

/** Colors for light theme */
fun lightThemeTokensMap(): Map<AcTokens, Color> = mapOf(
    AcTokens.AppBarNavigationButton to Black1,

    AcTokens.Background0 to White3,

    AcTokens.BottomSheetHook to Graphite2,

    AcTokens.ButtonDisabled to Gray1,
    AcTokens.ButtonStandard to Black1,
    AcTokens.ButtonTransparent to Transparent,
    AcTokens.ButtonWarning to Orange1,

    AcTokens.Divider to Gray2,

    AcTokens.FinanceCardDebts to Red6,
    AcTokens.FinanceCardGoals to Graphite6,
    AcTokens.FinanceCardSavings to Green11,
    AcTokens.FinanceCardTotalBalance to Graphite6,

    AcTokens.IconAddTransactionButton to White1,
    AcTokens.IconPrimary to Black1,
    AcTokens.IconSecondary to Gray2,
    AcTokens.IconTrendDown to Green11,
    AcTokens.IconTrendUp to Red6,
    AcTokens.IconTrendFlat to Black2,
    AcTokens.IconWarning to Orange1,

    AcTokens.Module to White1,

    AcTokens.NavigationBarColor to White2,
    AcTokens.NavigationBarSelectedColor to Black1,
    AcTokens.NavigationBarUnselectedColor to Black2,

    AcTokens.ProgressBar to Black1,

    AcTokens.PullToRefreshBackground to Black1,
    AcTokens.PullToRefreshProgress to White1,

    AcTokens.ShimmerBase to Gray3,
    AcTokens.ShimmerHighlight to Gray4,

    AcTokens.SnackBarCardContainerPrimary to Graphite1,
    AcTokens.SnackBarCardContentPrimary to White1,

    AcTokens.TextBrand to Beige2,
    AcTokens.TextButtonDisabled to Graphite1,
    AcTokens.TextButtonStandard to White1,
    AcTokens.TextButtonTransparent to Black1,
    AcTokens.TextButtonTransparentNegative to Orange1,
    AcTokens.TextButtonWarning to White1,
    AcTokens.TextInverse to White1,
    AcTokens.TextPrimary to Black1,
    AcTokens.TextSecondary to Black2,
    AcTokens.TextWarning to Orange1,

    AcTokens.TextFieldBackground to Gray2,
    AcTokens.TextFieldCursorColor to Black1,
    AcTokens.TextFieldIcon to Graphite2,
    AcTokens.TextFieldIconDisabled to Graphite3,
    AcTokens.TextFieldLabelDisabled to Graphite2,
    AcTokens.TextFieldLabelFocused to Graphite2,
    AcTokens.TextFieldLabelUnfocused to Graphite2,
    AcTokens.TextFieldSubtitle to Graphite2,
    AcTokens.TextFieldTextDisabled to Graphite2,
    AcTokens.TextFieldWarningBackground to Orange2,
    AcTokens.TextFieldWarningCursorColor to Orange2,
    AcTokens.TextFieldWarningIconStyle to Orange2,
    AcTokens.TextFieldWarningLabelFocused to Orange3,
    AcTokens.TextFieldWarningLabelUnfocused to Orange3,
    AcTokens.TextFieldWarningSubtitle to Orange2,
    AcTokens.TextFieldWarningTextDisabled to Orange3,

    AcTokens.Transparent to Transparent
)
