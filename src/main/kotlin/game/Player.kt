package game

import ui.UI

class Player(private val ui: UI) {
  fun requestMove(board: Board, mark: Mark) =
      ui.requestMove(board, mark)
}
