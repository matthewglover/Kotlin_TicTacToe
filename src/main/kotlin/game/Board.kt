package game

import arrow.core.Either

class Board(private val moves: List<Move> = listOf()) {

  val currentMark by lazy { if (equalMoves) Mark.ONE else Mark.TWO }

  val isComplete by lazy { isFull || isWinner }

  val isWinner by lazy { status.isWinner }

  val lastMark by lazy { moves.lastOrNull()?.mark }

  val winner by lazy { status.winner }

  private val equalMoves by lazy { movesBy(Mark.ONE) == movesBy(Mark.TWO) }

  private val isFull by lazy { moves.size == totalSquares }

  private val size = 3

  private val status by lazy { BoardStatus(moves) }

  private val totalSquares by lazy { size * size }

  override fun equals(other: Any?): Boolean =
      other is Board && other.moves == moves

  fun make(move: Move) =
      if (isTileTaken(move.number)) {
        Either.Left(InvalidInput.MOVE_TAKEN)
      } else if (isTileOutOfBounds(move.number)) {
        Either.Left(InvalidInput.OUT_OF_BOUNDS)
      } else {
        Either.Right(Board(moves + listOf(move)))
      }

  fun tile(number: Int): Tile = moves.find { it.number == number } ?: FreeTile(number)

  override fun toString() =
    "Board<moves=\"$moves\">"

  private fun isTileOutOfBounds(tileNumber: Int) = !isTileInBounds(tileNumber)

  private fun isTileTaken(tileNumber: Int) = !isTileFree(tileNumber)

  private fun isTileFree(tileNumber: Int) = moves.none { it.number == tileNumber }

  private fun isTileInBounds(tileNumber: Int) = tileNumber in 0..totalSquares

  private fun movesBy(mark: Mark) = moves.filter { it.mark == mark }.size
}