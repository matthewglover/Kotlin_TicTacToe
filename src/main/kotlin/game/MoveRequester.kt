package game

import arrow.core.identity
import core.Board
import core.InvalidData

class MoveRequester(
    private val io: IO,
    private val board: Board,
    private val invalidData: InvalidData? = null) {

  private val renderedBoard by lazy { BoardRenderer(board).rendered }

  private val renderedInvalidDataMessage by lazy { invalidData?.message }

  private val renderedMoveRequest by lazy { "${board.currentMark}'s turn! Choose your move: " }

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
      MoveParser(board)
          .parse(input)
          .fold(::retry, ::identity)

  private fun retry(invalidData: InvalidData) =
      MoveRequester(io, board, invalidData).run()
}

