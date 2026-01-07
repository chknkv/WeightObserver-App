package com.chknkv.coredesignsystem.typography

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.chknkv.coredesignsystem.theming.AcTokens
import com.chknkv.coredesignsystem.theming.getThemedColor

@Composable
fun Headline1(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextPrimary.getThemedColor(),
        fontSize = 30.sp,
        lineHeight = 36.sp,
        letterSpacing = -(0.048).em,
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Headline2(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextPrimary.getThemedColor(),
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = -(0.048).em,
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Headline3(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextPrimary.getThemedColor(),
        fontSize = 24.sp,
        lineHeight = 26.sp,
        letterSpacing = -(0.048).em,
        fontWeight = FontWeight.Bold
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Title1(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextPrimary.getThemedColor(),
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = -(0.048).em,
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Title2(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextPrimary.getThemedColor(),
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = -(0.048).em,
        fontWeight = FontWeight.Bold
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Body1(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextPrimary.getThemedColor(),
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = -(0.048).em,
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Body2(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextPrimary.getThemedColor(),
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = -(0.048).em,
        fontWeight = FontWeight.Bold
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Body3(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextPrimary.getThemedColor(),
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = -(0.048).em,
        fontWeight = FontWeight.Normal
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Footnote1(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextPrimary.getThemedColor(),
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = -(0.048).em,
        fontWeight = FontWeight.Normal
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Headline1Secondary(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextSecondary.getThemedColor(),
        fontSize = 30.sp,
        lineHeight = 36.sp,
        letterSpacing = -(0.048).em,
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Headline2Secondary(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextSecondary.getThemedColor(),
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = -(0.048).em,
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Headline3Secondary(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextSecondary.getThemedColor(),
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = -(0.048).em,
        fontWeight = FontWeight.Bold
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Title1Secondary(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextSecondary.getThemedColor(),
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = -(0.048).em,
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Title2Secondary(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextSecondary.getThemedColor(),
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = -(0.048).em,
        fontWeight = FontWeight.Bold
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Body1Secondary(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextSecondary.getThemedColor(),
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = -(0.048).em,
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Body2Secondary(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextSecondary.getThemedColor(),
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = -(0.048).em,
        fontWeight = FontWeight.Bold
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Body3Secondary(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextSecondary.getThemedColor(),
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = -(0.048).em,
        fontWeight = FontWeight.Normal
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Footnote1Secondary(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextSecondary.getThemedColor(),
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = -(0.048).em,
        fontWeight = FontWeight.Normal
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Headline1Brand(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextBrand.getThemedColor(),
        fontSize = 30.sp,
        lineHeight = 36.sp,
        letterSpacing = -(0.048).em,
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Headline2Brand(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextBrand.getThemedColor(),
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = -(0.048).em,
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Headline3Brand(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextBrand.getThemedColor(),
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = -(0.048).em,
        fontWeight = FontWeight.Bold
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Title1Brand(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextBrand.getThemedColor(),
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = -(0.048).em,
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Title2Brand(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextBrand.getThemedColor(),
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = -(0.048).em,
        fontWeight = FontWeight.Bold
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Body1Brand(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextBrand.getThemedColor(),
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = -(0.048).em,
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Body2Brand(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextBrand.getThemedColor(),
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = -(0.048).em,
        fontWeight = FontWeight.Bold
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Body3Brand(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextBrand.getThemedColor(),
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = -(0.048).em,
        fontWeight = FontWeight.Normal
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Footnote1Brand(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextBrand.getThemedColor(),
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = -(0.048).em,
        fontWeight = FontWeight.Normal
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Headline1Warning(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextWarning.getThemedColor(),
        fontSize = 30.sp,
        lineHeight = 36.sp,
        letterSpacing = -(0.048).em,
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Headline2Warning(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextWarning.getThemedColor(),
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = -(0.048).em,
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Headline3Warning(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextWarning.getThemedColor(),
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = -(0.048).em,
        fontWeight = FontWeight.Bold
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Title1Warning(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextWarning.getThemedColor(),
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = -(0.048).em,
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Title2Warning(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextWarning.getThemedColor(),
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = -(0.048).em,
        fontWeight = FontWeight.Bold
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Body1Warning(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextWarning.getThemedColor(),
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = -(0.048).em,
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Body2Warning(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextWarning.getThemedColor(),
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = -(0.048).em,
        fontWeight = FontWeight.Bold
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Body3Warning(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextWarning.getThemedColor(),
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = -(0.048).em,
        fontWeight = FontWeight.Normal
    ),
    overflow = TextOverflow.Ellipsis
)

@Composable
fun Footnote1Warning(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    maxLines = maxLines,
    textAlign = textAlign,
    style = TextStyle(
        color = AcTokens.TextWarning.getThemedColor(),
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = -(0.048).em,
        fontWeight = FontWeight.Normal
    ),
    overflow = TextOverflow.Ellipsis
)