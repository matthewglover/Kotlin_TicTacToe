package game

import core.Board

class MoveReporter(
    private val io: IO,
    private val board: Board
) {

  private val renderedBoard by lazy { BoardRenderer(board).rendered }

  private val renderedMoveRequest by lazy { "${board.currentMark}'s turn!" }

  private val renderedMoveState by lazy {
    listOfNotNull(
        renderedBoard,
        renderedMoveRequest
    ).joinToString("\n")
  }

  fun run() {
    io.clearScreen()
    io.write(renderedMoveState)
  }
}