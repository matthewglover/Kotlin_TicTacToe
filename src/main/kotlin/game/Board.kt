package game

class Board(private val moves: List<Move> = listOf()) {

  val currentMark
    get() =
      if (equalMoves())
        Mark.ONE
      else
        Mark.TWO

  fun make(move: Move) =
      Board(moves + listOf(move))

  fun tile(number: Int): Tile =
      moves.find { it.number == number } ?: BlankTile(number)

  fun isTileFree(number: Int) =
      moves.none { it.number == number }

  private fun equalMoves() =
      movesBy(Mark.ONE) == movesBy(Mark.TWO)

  private fun movesBy(mark: Mark) =
      moves.filter { it.mark == mark }
}