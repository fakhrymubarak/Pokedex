package com.fakhry.pokedex.core.utils

import java.util.*

fun String.capitalized() =
    this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }