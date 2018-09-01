package core

import arrow.core.Either

data class Board(private val takenTiles: List<TakenTile> = listOf()) :
    BoardStatus by BoardStatusImpl(takenTiles) {

  val currentMark by lazy { if (equalMoves) Mark.ONE else Mark.TWO }

  val freeTileNumbers by lazy { allSquares.filter { tileAt(it) is FreeTile } }

  val isComplete by lazy { isFull || isWinner }

  private val allSquares by lazy { 1..totalSquares }

  private val equalMoves by lazy { totalMovesBy(Mark.ONE) == totalMovesBy(Mark.TWO) }

  private val lastMark by lazy { takenTiles.lastOrNull()?.mark }

  private val isFull by lazy { takenTiles.size == totalSquares }

  private val isIncomplete by lazy { !isComplete }

  private val size = 3

  private val totalSquares by lazy { size * size }

  fun isCurrentMark(mark: Mark) = isIncomplete && currentMark == mark

  fun takeTile(move: Int) =
      when {
        isTaken(move) -> Either.Left(MoveTaken)
        isOutOfBounds(move) -> Either.Left(MoveOutOfBounds)
        else -> Either.Right(boardWith(move))
      }

  fun tileAt(tileNumber: Int) = takenTiles.find { it.tileNumber == tileNumber } ?: FreeTile(tileNumber)

  fun wasLastMark(mark: Mark) = lastMark == mark

  private fun boardWith(move: Int) = Board(takenTiles + listOf(TakenTile(move, currentMark)))

  private fun isOutOfBounds(move: Int) = move !in allSquares

  private fun isTaken(move: Int) = takenTiles.any { it.tileNumber == move }

  private fun totalMovesBy(mark: Mark) = takenTiles.filter { it.mark == mark }.size
}
