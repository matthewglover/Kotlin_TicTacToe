package game

import core.Board

class ResultNotifier(private val io: IO, private val board: Board) {

  private val renderedBoard by lazy { BoardRenderer(board).rendered }

  private val renderedResult by lazy {
    listOfNotNull(
        renderedBoard,
        renderedStatusMessage,
        "Press return to continue... "
    ).joinToString("\n")
  }

  private val renderedResultMessage by lazy {
    if (board.isWinner) {
      "Congratulations! ${board.winner} wins!"
    } else {
      "It's a draw!"
    }
  }

  private val renderedStatusMessage by lazy {
    if (board.isComplete) {
      renderedResultMessage
    } else {
      "Game still active!"
    }
  }

  fun run() {
    io.clearScreen()
    io.write(renderedResult)
    io.readLine()
  }
}
