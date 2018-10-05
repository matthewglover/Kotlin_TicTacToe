package game

import core.Board
import core.Mark

data class HumanPlayer(val ui: PlayerUI, val mark: Mark) : Player(mark) {

  override fun requestMoveWhenApplicable(board: Board, onUpdateBoard: BoardUpdateHandler) {
    onUpdateBoard(ui.requestMove(board))
  }
}