package game

class Game(
    private val p1: Player,
    private val p2: Player,
    private val board: Board
) {

  val isOver by lazy { board.isComplete }

  private val currentPlayer by lazy {
    if (p1.has(board.currentMark)) p1 else p2
  }

  override fun equals(other: Any?) =
      other is Game &&
          other.board == board &&
          other.p1 == p1 &&
          other.p2 == p2

  fun next() = Game(p1, p2, nextMove())

  fun notifyResult() {
    if (isOver) {
      p1.notifyResult(board)
      p2.notifyResult(board)
    }
  }

  private fun nextMove() = currentPlayer.requestMove(board)
}
