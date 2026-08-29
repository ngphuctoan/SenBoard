package banhmi.senboard.utils

/* Extension function to prepend an immutable list
Credit: Baeldung - Reference: https://www.baeldung.com/kotlin/list-prepend-item */
fun <T> List<T>.prepended(item: T) = buildList(this.size + 1) {
    add(item)
    addAll(this@prepended)
}
