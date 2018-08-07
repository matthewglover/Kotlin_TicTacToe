package game

import core.Board

class UI(private val io: IO) {

  fun notifyResult(board: Board) = ResultNotifier(io, board).run()

  fun requestMove(board: Board) = MoveRequester(io, board).run()

  fun reportMoveRequired(board: Board) { MoveReporter(io, board).run() }
}
