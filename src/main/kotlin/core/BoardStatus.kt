package core

interface BoardStatus {
  val isWinner: Boolean
  val winner: Mark?
}

class BoardStatusImpl(private val moves: List<Tile.Taken> = listOf()): BoardStatus {
  companion object {
    val lines = listOf(
        listOf(1, 2, 3),
        listOf(4, 5, 6),
        listOf(7, 8, 9),
        listOf(1, 4, 7),
        listOf(2, 5, 8),
        listOf(3, 6, 9),
        listOf(1, 5, 9),
        listOf(3, 5, 7)
    )
  }

  override val isWinner by lazy { winner != null }

  override val winner by lazy { Mark.values().find { hasWinningLine(it) } }

  private fun hasWinningLine(mark: Mark) =
      lines.any(moveNumbersFor(mark)::containsAll)

  private fun moveNumbersFor(mark: Mark) =
      moves
          .filter { it.mark == mark }
          .map { it.tileNumber }
}
