package game

import arrow.core.getOrElse

object BoardStates {

  //   1 | 2 | 3
  //  -----------
  //   4 | 5 | 6
  //  -----------
  //   7 | 8 | 9
  val EMPTY = Board()

  //   X | O | X
  //  -----------
  //   O | O | X
  //  -----------
  //   X | X | O
  val COMPLETE =
      runMoves(
          EMPTY,
          Move(1, Mark.ONE),
          Move(2, Mark.TWO),
          Move(3, Mark.ONE),
          Move(4, Mark.TWO),
          Move(8, Mark.ONE),
          Move(5, Mark.TWO),
          Move(7, Mark.ONE),
          Move(9, Mark.TWO),
          Move(6, Mark.ONE)
      )

  //   X | X | X
  //  -----------
  //   O | 5 | X
  //  -----------
  //   7 | 8 | 9
  val X_WINNING_ROW =
      runMoves(
          EMPTY,
          Move(1, Mark.ONE),
          Move(4, Mark.TWO),
          Move(2, Mark.ONE),
          Move(6, Mark.TWO),
          Move(3, Mark.ONE)
      )

  //   X | X | 3
  //  -----------
  //   O | O | O
  //  -----------
  //   X | 8 | 9
  val O_WINNING_ROW =
      runMoves(
          EMPTY,
          Move(1, Mark.ONE),
          Move(4, Mark.TWO),
          Move(2, Mark.ONE),
          Move(5, Mark.TWO),
          Move(7, Mark.ONE),
          Move(6, Mark.TWO)
      )

  //   O | 2 | X
  //  -----------
  //   O | 5 | X
  //  -----------
  //   7 | 8 | X
  val X_WINNING_COL =
      runMoves(
          EMPTY,
          Move(3, Mark.ONE),
          Move(1, Mark.TWO),
          Move(6, Mark.ONE),
          Move(4, Mark.TWO),
          Move(9, Mark.ONE)
      )

  //   O | 2 | 3
  //  -----------
  //   O | X | X
  //  -----------
  //   O | 8 | X
  val O_WINNING_COL =
      runMoves(
          EMPTY,
          Move(5, Mark.ONE),
          Move(1, Mark.TWO),
          Move(6, Mark.ONE),
          Move(4, Mark.TWO),
          Move(9, Mark.ONE),
          Move(7, Mark.TWO)
      )

  fun runMoves(board: Board, vararg moves: Move): Board =
      if (moves.isEmpty())
        board
      else
        board
            .make(moves.first())
            .map { nextBoard -> runMoves(nextBoard, *moves.drop(1).toTypedArray()) }
            .getOrElse { board }
}
