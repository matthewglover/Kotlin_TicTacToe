package game

import core.Board

typealias GameUpdateHandler = (Game) -> Unit

data class Game(
    private val p1: Player,
    private val p2: Player,
    val board: Board
) {

  private val players = listOf(p1, p2)

  fun next(onGameUpdate: GameUpdateHandler, onGameOver: GameUpdateHandler) {
    if (board.isComplete) {
      onGameOver(this)
    } else {
      requestMove(onGameUpdate)
    }
  }

  private fun requestMove(onGameUpdate: (Game) -> Unit) {
    val onBoardUpdate = { nextBoard: Board -> onGameUpdate(Game(p1, p2, nextBoard)) }

    players.forEach { it.requestMove(board, onBoardUpdate) }
  }
}
