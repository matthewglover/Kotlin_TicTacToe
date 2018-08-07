package game

import arrow.core.getOrElse
import core.Board
import core.Mark

abstract class Player(private val ui: UI, private val mark: Mark) {

  internal abstract fun requestMoveWhenApplicable(board: Board, onUpdateBoard: (Board) -> Unit)

  fun notifyResult(board: Board, onGameOver: () -> Unit) {
    if (shouldNotifyResult(board)) {
      ui.notifyResult(board)
      onGameOver()
    }
  }

  fun requestMove(board: Board, onUpdateBoard: (Board) -> Unit) {
    if (isTurn(board)) requestMoveWhenApplicable(board, onUpdateBoard)
  }

  private fun isTurn(board: Board) = board.isCurrentMark(mark)

  private fun playerMadeDrawingMove(board: Board) = board.isComplete && board.wasLastMark(mark)

  private fun playerWon(board: Board) = board.winner == mark

  private fun shouldNotifyResult(board: Board) = playerWon(board) || playerMadeDrawingMove(board)
}

data class HumanPlayer(val ui: UI, val mark: Mark) : Player(ui, mark) {

  override fun requestMoveWhenApplicable(board: Board, onUpdateBoard: (Board) -> Unit) {
    onUpdateBoard(ui.requestMove(board))
  }
}

data class ComputerPlayer(private val ui: UI, private val mark: Mark, private val delayMove: DelayMove) : Player(ui, mark) {

  override fun requestMoveWhenApplicable(board: Board, onUpdateBoard: (Board) -> Unit) {
    ui.reportMoveRequired(board)
    delayMove.run()
    onUpdateBoard(nextBoard(board))
  }

  private fun nextBoard(board: Board) = board.takeTile(selectTileNumber(board)).getOrElse { board }

  private fun selectTileNumber(board: Board) = board.freeTileNumbers.first()
}
