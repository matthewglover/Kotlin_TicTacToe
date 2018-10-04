package game

import core.Board
import core.Mark

typealias BoardUpdateHandler = (Board) -> Unit

abstract class Player(private val ui: UI, private val mark: Mark) {

  internal abstract fun requestMoveWhenApplicable(board: Board, onUpdateBoard: (Board) -> Unit)

  fun notifyResult(board: Board, onGameOver: () -> Unit) {
    if (shouldNotifyResult(board)) {
      ui.notifyResult(board)
      onGameOver()
    }
  }

  fun requestMove(board: Board, onUpdateBoard: BoardUpdateHandler) {
    if (isTurn(board)) requestMoveWhenApplicable(board, onUpdateBoard)
  }

  private fun isTurn(board: Board) = board.isCurrentMark(mark)

  private fun playerMadeDrawingMove(board: Board) = board.isComplete && board.wasLastMark(mark)

  private fun playerWon(board: Board) = board.winner == mark

  private fun shouldNotifyResult(board: Board) = playerWon(board) || playerMadeDrawingMove(board)
}

