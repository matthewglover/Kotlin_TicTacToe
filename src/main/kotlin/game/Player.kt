package game

import ui.UI

class Player(private val ui: UI, private val mark: Mark) {

  fun notifyResult(board: Board) {
    if (shouldNotify(board)) ui.notifyResult(board)
  }

  fun requestMove(board: Board) = ui.requestMove(board, mark)

  private fun shouldNotify(board: Board) = playerWon(board) || isDraw(board)

  private fun playerWon(board: Board) = board.winner == mark

  private fun isDraw(board: Board) = board.isComplete && (board.lastMark == mark)
}
