package ui

import arrow.core.identity
import game.Board
import game.InvalidData
import game.Mark

class MoveRequester(
  private val io: IO,
  private val board: Board,
  private val mark: Mark,
  private val invalidData: InvalidData? = null) {

  private val renderedBoard by lazy { BoardRenderer(board).rendered }

  private val renderedInvalidDataMessage by lazy { invalidData?.message }

  private val renderedMoveRequest by lazy { "$mark's turn! Choose your move: " }

  private val renderedMoveState by lazy {
    listOfNotNull(
        renderedBoard,
        renderedInvalidDataMessage,
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

  private fun retry(invalidData: InvalidData) =
      MoveRequester(io, board, mark, invalidData).run()
}
