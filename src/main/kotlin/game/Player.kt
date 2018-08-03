package game

import ui.UI

class Player(private val ui: UI, private val mark: Mark) {

  override fun equals(other: Any?) =
      other is Player &&
          other.ui == ui &&
          other.mark == mark

  fun notifyResult(board: Board) {
    if (shouldNotifyResult(board)) {
      ui.notifyResult(board)
    }
  }

  fun requestMove(board: Board, onUpdateBoard: (Board) -> Unit) {
    if (isTurn(board)) {
      onUpdateBoard(ui.requestMove(board, mark))
    }
  }

  private fun isTurn(board: Board) = board.isCurrentMark(mark)

  private fun playerMadeDrawingMove(board: Board) = board.isComplete && board.wasLastMark(mark)

  private fun shouldNotifyResult(board: Board) = playerWon(board) || playerMadeDrawingMove(board)

  private fun playerWon(board: Board) = board.winner == mark
}
