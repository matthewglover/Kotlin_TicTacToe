package game

import core.Board
import core.Mark

data class HumanPlayer(val ui: UI, val mark: Mark) : Player(ui, mark) {

  override fun requestMoveWhenApplicable(board: Board, onUpdateBoard: BoardUpdateHandler) {
    onUpdateBoard(ui.requestMove(board))
  }
}