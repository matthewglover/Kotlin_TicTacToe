package core

import arrow.core.Either

data class Board(private val moves: List<Move> = listOf()) {

  val currentMark by lazy { if (equalMoves) Mark.ONE else Mark.TWO }

  val freeTileNumbers by lazy { (1..totalSquares).filter { tile(it) is FreeTile } }

  val isComplete by lazy { isFull || isWinner }

  val isWinner by lazy { status.isWinner }

  val winner by lazy { status.winner }

  private val equalMoves by lazy { movesBy(Mark.ONE) == movesBy(Mark.TWO) }

  private val lastMark by lazy { moves.lastOrNull()?.mark }

  private val isFull by lazy { moves.size == totalSquares }

  private val size = 3

  private val status by lazy { BoardStatus(moves) }

  private val totalSquares by lazy { size * size }

  fun isCurrentMark(mark: Mark?) =
      if (isComplete) false else currentMark == mark

  fun make(move: Move) =
      when {
        isMoveOutOfTurn(move) -> Either.Left(MoveOutOfTurn)
        isTileTaken(move) -> Either.Left(MoveTaken)
        isTileOutOfBounds(move) -> Either.Left(MoveOutOfBounds)
        else -> Either.Right(Board(moves + listOf(move)))
      }

  fun tile(tileNumber: Int): Tile = moves.find { it.tileNumber == tileNumber } ?: FreeTile(tileNumber)

  fun wasLastMark(mark: Mark) = lastMark == mark

  private fun isMoveOutOfTurn(move: Move) = !isCurrentMark(move.mark)

  private fun isTileInBounds(move: Move) = move.tileNumber in 0..totalSquares

  private fun isTileOutOfBounds(move: Move) = !isTileInBounds(move)

  private fun isTileTaken(move: Move) = moves.any { it.tileNumber == move.tileNumber }

  private fun movesBy(mark: Mark) = moves.filter { it.mark == mark }.size
}
