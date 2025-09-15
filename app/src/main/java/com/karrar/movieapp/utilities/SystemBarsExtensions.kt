package com.karrar.movieapp.utilities

import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.setSystemBarsColor(
    @ColorRes colorRes: Int,
) {
    val window = activity?.window ?: return

    val color = ContextCompat.getColor(requireContext(), colorRes)
    window.statusBarColor = color
    window.navigationBarColor = color
}
