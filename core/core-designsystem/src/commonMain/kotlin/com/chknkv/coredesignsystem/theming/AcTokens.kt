package com.chknkv.coredesignsystem.theming

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

enum class AcTokens {
    AppBarNavigationButton,
    AlertActionBackground,

    Background0,
    Background1,

    BottomSheetHook,
    BottomSheetScrim,

    ButtonDisabled,
    ButtonStandard,
    ButtonTransparent,
    ButtonWarning,

    Divider,

    FinanceCardDebts,
    FinanceCardGoals,
    FinanceCardSavings,
    FinanceCardTotalBalance,

    IconAddTransactionButton,
    IconPrimary,
    IconSecondary,
    IconTrendDown,
    IconTrendUp,
    IconTrendFlat,
    IconWarning,

    Module,

    NavigationBarColor,
    NavigationBarSelectedColor,
    NavigationBarUnselectedColor,

    PasscodeKey,
    ProgressBar,
    PullToRefreshBackground,
    PullToRefreshProgress,

    ShimmerBase,
    ShimmerHighlight,

    SnackBarCardContainerPrimary,
    SnackBarCardContentPrimary,

    TextBrand,
    TextButtonDisabled,
    TextButtonStandard,
    TextButtonTransparent,
    TextButtonTransparentNegative,
    TextButtonWarning,
    TextInverse,
    TextPrimary,
    TextSecondary,
    TextWarning,

    TextFieldBackground,
    TextFieldCursorColor,
    TextFieldIcon,
    TextFieldIconDisabled,
    TextFieldLabelDisabled,
    TextFieldLabelFocused,
    TextFieldLabelUnfocused,
    TextFieldSubtitle,
    TextFieldTextDisabled,
    TextFieldWarningBackground,
    TextFieldWarningCursorColor,
    TextFieldWarningIconStyle,
    TextFieldWarningLabelFocused,
    TextFieldWarningLabelUnfocused,
    TextFieldWarningSubtitle,
    TextFieldWarningTextDisabled,

    Transparent
}

/** Get color from theme */
@Composable
@ReadOnlyComposable
fun AcTokens.getThemedColor(): Color = AcTheme.tokens.getValue(this)

/** Color tokens */
internal val LocalTokens: ProvidableCompositionLocal<Map<AcTokens, Color>> =
    staticCompositionLocalOf { lightThemeTokensMap() }
