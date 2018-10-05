package game

import arrow.core.getOrElse
import core.Board
import core.Mark

data class ComputerPlayer(private val ui: PlayerUI, private val mark: Mark, private val delayMove: DelayMove) : Player(mark) {

  override fun requestMoveWhenApplicable(board: Board, onUpdateBoard: BoardUpdateHandler) {
    ui.reportMoveRequired(board)
    delayMove.run()
    onUpdateBoard(nextBoard(board))
  }

  private fun nextBoard(board: Board) = board.takeTile(selectTileNumber(board)).getOrElse { board }

  private fun selectTileNumber(board: Board) = board.freeTileNumbers.first()
}