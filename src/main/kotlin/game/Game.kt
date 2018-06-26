package game

class Game(
    private val p1: Player,
    private val p2: Player,
    private val board: Board
) {

  private val currentPlayer: Player
    get() = when (board.currentMark) {
      Mark.ONE -> p1
      Mark.TWO -> p2
    }

  fun next() =
      Game(p1, p2, nextBoard())

  private fun nextBoard() =
      board.make(nextMove())

  private fun nextMove() =
      currentPlayer
          .requestMove(board, board.currentMark)
}
