package ui

import game.Board
import game.Mark
import game.Move


class UI(private val io: IO) {
  fun requestMove(board: Board, mark: Mark, invalidInput: InvalidInput? = null): Move {
    io.clearScreen()
    io.write(renderContent(board, invalidInput, mark))
    return parseMove(board, mark, io.read())
  }

  private fun renderContent(board: Board, invalidInput: InvalidInput?, mark: Mark) =
      listOfNotNull(
          renderBoard(board),
          renderInvalidInputMessage(invalidInput),
          renderMoveRequest(mark)
      ).joinToString("\n")

  private fun renderBoard(board: Board) =
      BoardRenderer.render(board)

  private fun renderInvalidInputMessage(invalidInput: InvalidInput?) =
      invalidInput?.message

  private fun renderMoveRequest(mark: Mark) =
      "$mark's turn! Choose your move: "

  private fun parseMove(board: Board, mark: Mark, input: String?) =
      MoveParser(board, mark)
          .parse(input)
          .fold({ this.requestMove(board, mark, it) }, { it })
}