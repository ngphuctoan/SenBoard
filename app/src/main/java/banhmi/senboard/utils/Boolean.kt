package banhmi.senboard.utils

fun Boolean.toInt() = if (this) 1 else 0

operator fun Boolean.plus(other: Boolean) = this.toInt() + other.toInt()

operator fun Boolean.plus(other: Int) = this.toInt() + other

operator fun Int.plus(other: Boolean) = this + other.toInt()
