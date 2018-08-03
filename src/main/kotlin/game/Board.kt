package game

import arrow.core.Either

class Board(private val moves: List<Move> = listOf()) {

  val isComplete by lazy { isFull || isWinner }

  val isWinner by lazy { status.isWinner }

  val winner by lazy { status.winner }

  private val currentMark by lazy { if (equalMoves) Mark.ONE else Mark.TWO }

  private val equalMoves by lazy { movesBy(Mark.ONE) == movesBy(Mark.TWO) }

  private val lastMark by lazy { moves.lastOrNull()?.mark }

  private val isFull by lazy { moves.size == totalSquares }

  private val size = 3

  private val status by lazy { BoardStatus(moves) }

  private val totalSquares by lazy { size * size }

  override fun equals(other: Any?): Boolean = other is Board && other.moves == moves

  fun isCurrentMark(mark: Mark) = currentMark == mark

  fun make(move: Move) =
      when {
        isTileTaken(move) -> Either.Left(MoveTaken)
        isTileOutOfBounds(move) -> Either.Left(MoveOutOfBounds)
        else -> Either.Right(Board(moves + listOf(move)))
      }

  fun tile(tileNumber: Int): Tile = moves.find { it.tileNumber == tileNumber } ?: FreeTile(tileNumber)

  fun wasLastMark(mark: Mark) = lastMark == mark

  override fun toString() = "Board<moves=$moves>"

  private fun isTileInBounds(move: Move) = move.tileNumber in 0..totalSquares

  private fun isTileOutOfBounds(move: Move) = !isTileInBounds(move)

  private fun isTileTaken(move: Move) = moves.any { it.tileNumber == move.tileNumber }

  private fun movesBy(mark: Mark) = moves.filter { it.mark == mark }.size
}
