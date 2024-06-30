package com.example.rango.utils

import android.content.Context
import android.util.TypedValue
import java.text.NumberFormat
import java.util.Locale

enum class LoadingStyle { DEFAULT, LIST }

fun floatToDp(context: Context, value: Float): Float {
    val metrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics)
}

fun Double?.convertToCurrency(locale: Locale? = Locale.forLanguageTag("pt-BR")): String {
    val localization = locale ?: Locale.forLanguageTag("pt-BR")
    return NumberFormat.getCurrencyInstance(localization).format(this ?: 0.0)
}