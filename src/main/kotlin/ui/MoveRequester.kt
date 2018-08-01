package ui

import arrow.core.identity
import game.Board
import game.InvalidInput
import game.Mark

class MoveRequester(
  private val io: IO,
  private val board: Board,
  private val mark: Mark,
  private val invalidInput: InvalidInput? = null) {

  private val renderedBoard by lazy { BoardRenderer(board).rendered }

  private val renderedInvalidInputMessage by lazy { invalidInput?.message }

  private val renderedMoveRequest by lazy { "$mark's turn! Choose your move: " }

  private val renderedMoveState by lazy {
    listOfNotNull(
        renderedBoard,
        renderedInvalidInputMessage,
        renderedMoveRequest
    ).joinToString("\n")
  }

  fun run(): Board {
    io.clearScreen()
    io.write(renderedMoveState)
    return parseMove(io.readLine())
  }

  private fun parseMove(input: String?) =
      MoveParser(board, mark)
          .parse(input)
          .fold(::retry, ::identity)

  private fun retry(invalidInput: InvalidInput) =
      MoveRequester(io, board, mark, invalidInput).run()
}
