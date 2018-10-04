package game

import core.Board
import core.Tile
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
        .chunked(board.size)
        .map { " ${it.joinToString(" | ")}" }
  }

  private val renderedTiles by lazy { board.allSquares.map(::renderTile) }

  private fun renderTile(tileNumber: Int) = with(board.tileAt(tileNumber)) {
    when (this) {
      is Tile.Free -> number.toString()
      is Tile.Taken -> mark.toString()
    }
  }
}
