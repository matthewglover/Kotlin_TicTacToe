package game

import ui.UI

class Player(private val ui: UI, private val mark: Mark) {

  override fun equals(other: Any?) =
      other is Player &&
          other.ui == ui &&
          other.mark == mark

  fun notifyResult(board: Board) {
    if (shouldNotify(board)) ui.notifyResult(board)
  }

  fun requestMove(board: Board, cb: (Board) -> Unit) {
    if (isTurn(board)) {
      cb(ui.requestMove(board, mark))
    }
  }

  private fun isTurn(board: Board) = board.currentMark == mark

  private fun shouldNotify(board: Board) = playerWon(board) || playerMadeDrawingMove(board)

  private fun playerWon(board: Board) = board.winner == mark

  private fun playerMadeDrawingMove(board: Board) = board.isComplete && (board.lastMark == mark)
}
