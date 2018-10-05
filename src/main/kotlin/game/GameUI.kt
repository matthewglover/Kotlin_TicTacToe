package game

import core.Board

class PlayerUI(private val io: IO) {

  fun requestMove(board: Board) = MoveRequester(io, board).run()

  fun reportMoveRequired(board: Board) { MoveReporter(io, board).run() }
}

class GameUI(private val io: IO) {

  fun notifyResult(game: Game) = ResultNotifier(io, game.board).run()
}
