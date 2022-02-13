package com.simplifydvpn.android.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.google.android.material.snackbar.Snackbar
import com.simplifydvpn.android.R
import com.simplifydvpn.android.ui.LoadingDialog
import java.util.*
import java.util.regex.Pattern
import kotlin.math.roundToInt


fun Context.showToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(message: String?) {
    requireContext().showToast(message)
}

fun Context.getColorInt(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

fun Fragment.getColorInt(@ColorRes colorRes: Int): Int {
    return requireContext().getColorInt(colorRes)
}

fun Fragment.showLoadingDialog() {
    LoadingDialog().show(childFragmentManager, LoadingDialog.TAG)
}

fun Fragment.generateColor(any: Any): Int {
    val generator = ColorGenerator.MATERIAL
    return generator.getColor(any)
}

fun Fragment.hideLoadingDialog() {
    val fragment = childFragmentManager.findFragmentByTag(LoadingDialog.TAG)
    fragment?.let {
        (it as LoadingDialog).dismiss()
    }
}

fun String.isValidEmail(): Boolean {
    return this matches Patterns.EMAIL_ADDRESS.toRegex()
}

fun String.isValidCode(): Boolean {
    var status = false
    if (this.length < 4) {
    } else {
        status = true
    }

    return status
}


fun String.isValidPhoneNumber(): Boolean {
    val clearDigitsRegex = "[^\\d]"
    val phoneNumber = replace(clearDigitsRegex.toRegex(), "")

    val regInternationalised = "^234[7,8,9]\\d{9}\$"
    val regNorm = "^0[7,8,9]\\d{9}\$"

    val pattern: Pattern = Pattern.compile(regNorm)
    val internationalizedPattern: Pattern = Pattern.compile(regInternationalised)
    return pattern.matcher(phoneNumber).find()
        .or(internationalizedPattern.matcher(phoneNumber).find())
}


fun String.isValidDomain(): Boolean {
    return this matches Patterns.WEB_URL.toRegex()
}

@ExperimentalStdlibApi
fun String.getUrlHost(): String {
    val host = toUri().host ?: return this.split(".")[0].capitalize(Locale.getDefault())

    if (host.startsWith("www")) {
        return host.split(".")[1].capitalize(Locale.getDefault())
    }

    return host.split(".")[0].capitalize(Locale.getDefault())
}

fun Context.dpToPx(dp: Int): Int {
    val displayMetrics = resources.displayMetrics
    return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}

fun Activity.animateStatusBarColorChangeTo(endColor: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        animateBetweenColors(window.statusBarColor, endColor) {
            window.statusBarColor = it
        }
    }
}

fun animateBetweenColors(start: Int, end: Int, function: (animatedValue: Int) -> Unit) {
    val animator = ValueAnimator.ofObject(
        ArgbEvaluator(),
        start,
        end
    )
    animator.duration = 300 // milliseconds
    animator.addUpdateListener { valueAnimator ->
        function.invoke(valueAnimator.animatedValue as @kotlin.ParameterName(name = "animatedValue") Int)
    }
    animator.start()
}

fun Context.getColorHexString(@ColorRes resId: Int): String {
    val colorInt = ContextCompat.getColor(this, resId)
    return String.format("#%06X", 0xFFFFFF and colorInt)
}

fun TextView.setTopDrawable(@DrawableRes drawableRes: Int) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(
        null, ContextCompat.getDrawable(
            context,
            drawableRes
        ), null, null
    )
}

fun Fragment.showRetrySnackBar(message: String?, retryAction: (View) -> Unit) {
    val snackBar = Snackbar.make(requireView(), message ?: return, Snackbar.LENGTH_INDEFINITE)
        .setAction("Retry", retryAction)
        .setActionTextColor(getColorInt(R.color.colorWhite))

    val view = snackBar.view
    view.setBackgroundColor(getColorInt(R.color.colorDarkRed))

    snackBar.show()
}