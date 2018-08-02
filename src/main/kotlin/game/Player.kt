package game

import ui.UI

class Player(private val ui: UI, private val mark: Mark) {

  override fun equals(other: Any?) =
      other is Player &&
          other.ui == ui &&
          other.mark == mark

  fun has(comparison: Mark) = mark == comparison

  fun notifyResult(board: Board) {
    if (shouldNotify(board)) ui.notifyResult(board)
  }

  fun requestMove(board: Board) = ui.requestMove(board, mark)

  private fun shouldNotify(board: Board) = playerWon(board) || isDraw(board)

  private fun playerWon(board: Board) = board.winner == mark

  private fun isDraw(board: Board) = board.isComplete && (board.lastMark == mark)
}
