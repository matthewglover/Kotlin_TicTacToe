package util

fun <T : Any> List<T>.intersperse(item: T) =
    zip(listOfItem(item, size))
        .flatMap { (a, b) -> listOf(a, b) }
        .dropLast(1)

private fun <T : Any> listOfItem(item: T, size: Int) = generateSequence { item }
    .take(size)
    .toList()
