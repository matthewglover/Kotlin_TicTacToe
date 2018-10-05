package game

import core.Board
import core.Mark

typealias BoardUpdateHandler = (Board) -> Unit

abstract class Player(private val mark: Mark) {

  internal abstract fun requestMoveWhenApplicable(board: Board, onUpdateBoard: BoardUpdateHandler)

  fun requestMove(board: Board, onUpdateBoard: BoardUpdateHandler) {
    if (isTurn(board)) requestMoveWhenApplicable(board, onUpdateBoard)
  }

  private fun isTurn(board: Board) = board.isCurrentMark(mark)
}

