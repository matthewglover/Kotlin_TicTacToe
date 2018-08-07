package core

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
  val COMPLETE = takeTiles(EMPTY, 1, 2, 3, 4, 8, 5, 7, 9, 6)

  //   X | X | X
  //  -----------
  //   O | 5 | X
  //  -----------
  //   7 | 8 | 9
  val MARK_ONE_WINNING_ROW = takeTiles(EMPTY, 1, 4, 2, 6, 3)

  //   X | X | 3
  //  -----------
  //   O | O | O
  //  -----------
  //   X | 8 | 9
  val MARK_TWO_WINNING_ROW =
      takeTiles(EMPTY, 1, 4, 2, 5, 7, 6)

  //   O | 2 | X
  //  -----------
  //   O | 5 | X
  //  -----------
  //   7 | 8 | X
  val MARK_ONE_WINNING_COL = takeTiles(EMPTY, 3, 1, 6, 4, 9)

  //   O | 2 | 3
  //  -----------
  //   O | X | X
  //  -----------
  //   O | 8 | X
  val MARK_TWO_WINNING_COL = takeTiles(EMPTY, 5, 1, 6, 4, 9, 7)

  fun takeTiles(board: Board, vararg tileNumbers: Int): Board =
      if (tileNumbers.isEmpty())
        board
      else
        board
            .takeTile(tileNumbers.first())
            .map { nextBoard -> takeTiles(nextBoard, *tileNumbers.drop(1).toIntArray()) }
            .getOrElse { board }
}
