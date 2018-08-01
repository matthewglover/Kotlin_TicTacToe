package ui

import game.Board
import game.Mark

class UI(private val io: IO) {

  fun notifyResult(board: Board) =
      ResultNotifier(io, board).run()

  fun requestMove(board: Board, mark: Mark) =
      MoveRequester(io, board, mark).run()
}
