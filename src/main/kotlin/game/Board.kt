package game

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

  fun equals(other: Board) =
      other.moves == moves

  fun make(move: Move) = Board(moves + listOf(move))

  fun tile(number: Int): Tile = moves.find { it.number == number } ?: FreeTile(number)

  fun isTileOutOfBounds(tileNumber: Int) = !isTileInBounds(tileNumber)

  fun isTileTaken(tileNumber: Int) = !isTileFree(tileNumber)

  private fun isTileFree(tileNumber: Int) = moves.none { it.number == tileNumber }

  private fun isTileInBounds(tileNumber: Int) = tileNumber in 0..totalSquares

  private fun movesBy(mark: Mark) = moves.filter { it.mark == mark }.size
}