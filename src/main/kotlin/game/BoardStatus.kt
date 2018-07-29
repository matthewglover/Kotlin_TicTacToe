package game

typealias Line = List<TileNumber>
typealias TileNumber = Int

class BoardStatus(private val moves: List<Move> = listOf()) {

  val isWinner by lazy { winner != null }

  val winner: Mark? by lazy { Mark.values().find { isWinningLine(movesBy(it)) } }

  private val lines: List<Line> by lazy {
    listOf(
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

  private fun movesBy(mark: Mark) =
      moves.filter { it.mark == mark }
          .map { it.number }

  private fun isWinningLine(moveNumbers: List<Int>) =
      lines.any(moveNumbers::containsAll)
}