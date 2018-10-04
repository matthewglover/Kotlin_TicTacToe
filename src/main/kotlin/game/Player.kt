package game

import core.Board
import core.Mark

typealias BoardUpdateHandler = (Board) -> Unit

abstract class Player(private val ui: UI, private val mark: Mark) {

  internal abstract fun requestMoveWhenApplicable(board: Board, onUpdateBoard: (Board) -> Unit)

  fun notifyResult(game: Game, onGameOver: GameUpdateHandler) {
    if (shouldNotifyResult(game.board)) {
      ui.notifyResult(game.board)
      onGameOver(game)
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

