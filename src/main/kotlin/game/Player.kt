package game

import ui.UI

data class Player(private val ui: UI, private val mark: Mark) {

  fun notifyResult(board: Board, onGameOver: () -> Unit) {
    if (shouldNotifyResult(board)) {
      ui.notifyResult(board)
      onGameOver()
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
