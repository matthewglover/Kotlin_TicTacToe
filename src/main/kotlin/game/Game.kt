package game

class Game(
    private val p1: Player,
    private val p2: Player,
    private val board: Board
) {

  val isOver by lazy { board.isComplete }

  private val currentPlayer by lazy {
    when (board.currentMark) {
      Mark.ONE -> p1
      Mark.TWO -> p2
    }
  }

  fun next() = Game(p1, p2, nextBoard())

  fun notifyResult() {
    if (isOver) {
      p1.notifyResult(board)
      p2.notifyResult(board)
    }
  }

  private fun nextBoard() = board.make(nextMove())

  private fun nextMove() = currentPlayer.requestMove(board)
}
