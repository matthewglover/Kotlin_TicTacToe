package ui

import game.FreeTile
import game.Board
import game.Move
import util.intersperse

private const val ROW_DIVIDER = "-----------"

class BoardRenderer(private val board: Board) {

  val rendered by lazy {
    renderedRows
        .intersperse(ROW_DIVIDER)
        .joinToString("\n")
  }

  private val renderedRows by lazy {
    renderedTiles
        .chunked(3)
        .map { " ${it.joinToString(" | ")}" }
  }

  private val renderedTiles by lazy { (1..9).map(::renderTile) }

  private fun renderTile(tileNumber: Int) = with(board.tile(tileNumber)) {
    when (this) {
      is FreeTile -> this.number.toString()
      is Move -> this.mark.toString()
    }
  }
}