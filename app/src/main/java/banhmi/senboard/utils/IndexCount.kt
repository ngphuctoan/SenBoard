package banhmi.senboard.utils

/* This has 2 use cases:
1. For settings menu - index tells the settings the current position of the menu in the group,
and the count indicates the number of menus in the group
2. For keyboard layout - index/count is the layout's row-to-key index/number of keys in the same row */
data class IndexCount(
    val index: Int,
    val count: Int,
) {
    fun isFirst() = index == 0

    fun isLast() = index == count - 1
}

// I guess a more "written language" way of defining IndexCount :b
infix fun Int.outOf(
    count: Int,
) = IndexCount(this, count)
