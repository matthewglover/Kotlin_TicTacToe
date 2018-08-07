package core

import arrow.core.Either

data class Board(private val takenTiles: List<TakenTile> = listOf()) {

  val currentMark by lazy { if (equalMoves) Mark.ONE else Mark.TWO }

  val freeTileNumbers by lazy { (1..totalSquares).filter { tile(it) is FreeTile } }

  val isComplete by lazy { isFull || isWinner }

  val isWinner by lazy { status.isWinner }

  val winner by lazy { status.winner }

  private val equalMoves by lazy { movesBy(Mark.ONE) == movesBy(Mark.TWO) }

  private val lastMark by lazy { takenTiles.lastOrNull()?.mark }

  private val isFull by lazy { takenTiles.size == totalSquares }

  private val size = 3

  private val status by lazy { BoardStatus(takenTiles) }

  private val totalSquares by lazy { size * size }

  fun isCurrentMark(mark: Mark?) =
      if (isComplete) false else currentMark == mark

  fun takeTile(tileNumber: Int) = makeMove(TakenTile(tileNumber, currentMark))

  fun tile(tileNumber: Int): Tile = takenTiles.find { it.tileNumber == tileNumber } ?: FreeTile(tileNumber)

  fun wasLastMark(mark: Mark) = lastMark == mark

  private fun makeMove(move: TakenTile) =
      when {
        isMoveOutOfTurn(move) -> Either.Left(MoveOutOfTurn)
        isTileTaken(move) -> Either.Left(MoveTaken)
        isTileOutOfBounds(move) -> Either.Left(MoveOutOfBounds)
        else -> Either.Right(Board(takenTiles + listOf(move)))
      }

  private fun isMoveOutOfTurn(move: TakenTile) = !isCurrentMark(move.mark)

  private fun isTileInBounds(move: TakenTile) = move.tileNumber in 0..totalSquares

  private fun isTileOutOfBounds(move: TakenTile) = !isTileInBounds(move)

  private fun isTileTaken(move: TakenTile) = takenTiles.any { it.tileNumber == move.tileNumber }

  private fun movesBy(mark: Mark) = takenTiles.filter { it.mark == mark }.size
}
