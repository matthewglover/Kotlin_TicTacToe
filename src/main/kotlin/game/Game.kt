package game

class Game(
    private val p1: Player,
    private val p2: Player,
    private val board: Board
) {

  val isOver by lazy { board.isComplete }

  override fun equals(other: Any?) =
      other is Game &&
          other.board == board &&
          other.p1 == p1 &&
          other.p2 == p2

  fun next(onGameUpdate: (Game) -> Unit) {
    val onBoardUpdate = { nextBoard: Board ->
      onGameUpdate(Game(p1, p2, nextBoard))
    }

    p1.requestMove(board, onBoardUpdate)
    p2.requestMove(board, onBoardUpdate)
  }

  fun notifyResult() {
    if (isOver) {
      p1.notifyResult(board)
      p2.notifyResult(board)
    }
  }
}
