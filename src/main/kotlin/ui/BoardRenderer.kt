package ui

import game.BlankTile
import game.Board
import game.Move
import util.intersperse

object BoardRenderer {
  private const val ROW_DIVIDER = "-----------"

  fun render(board: Board) =
      renderRows(board)
          .intersperse(ROW_DIVIDER)
          .joinToString("\n")

  private fun renderRows(board: Board) =
      renderTiles(board)
          .chunked(3)
          .map { " ${it.joinToString(" | ")}" }

  private fun renderTiles(board: Board) =
      (1..9).map { renderTile(board, it) }

  private fun renderTile(board: Board, number: Int) =
      with(board.tile(number)) {
        when (this) {
          is BlankTile -> this.number.toString()
          is Move -> this.mark.toString()
        }
      }
}