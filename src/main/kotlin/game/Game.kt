package game

import core.Board

data class Game(
    private val p1: Player,
    private val p2: Player,
    private val board: Board
) {

  private val players = listOf(p1, p2)

  fun next(onGameUpdate: (Game) -> Unit, onGameOver: () -> Unit) {
    if (board.isComplete) {
      notifyResult(onGameOver)
    } else {
      requestMove(onGameUpdate)
    }
  }

  private fun notifyResult(onGameOver: () -> Unit) {
    players.forEach { it.notifyResult(board, onGameOver) }
  }

  private fun requestMove(onGameUpdate: (Game) -> Unit) {
    val onBoardUpdate = { nextBoard: Board -> onGameUpdate(Game(p1, p2, nextBoard)) }

    players.forEach { it.requestMove(board, onBoardUpdate) }
  }
}
