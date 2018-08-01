package game

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
  val COMPLETE = Board()
      .make(Move(1, Mark.ONE))
      .make(Move(2, Mark.TWO))
      .make(Move(3, Mark.ONE))
      .make(Move(4, Mark.TWO))
      .make(Move(8, Mark.ONE))
      .make(Move(5, Mark.TWO))
      .make(Move(7, Mark.ONE))
      .make(Move(9, Mark.TWO))
      .make(Move(6, Mark.ONE))

  //   X | X | X
  //  -----------
  //   O | 5 | X
  //  -----------
  //   7 | 8 | 9
  val X_WINNING_ROW = Board()
      .make(Move(1, Mark.ONE))
      .make(Move(4, Mark.TWO))
      .make(Move(2, Mark.ONE))
      .make(Move(6, Mark.TWO))
      .make(Move(3, Mark.ONE))

  //   X | X | 3
  //  -----------
  //   O | O | O
  //  -----------
  //   X | 8 | 9
  val O_WINNING_ROW = Board()
      .make(Move(1, Mark.ONE))
      .make(Move(4, Mark.TWO))
      .make(Move(2, Mark.ONE))
      .make(Move(5, Mark.TWO))
      .make(Move(7, Mark.ONE))
      .make(Move(6, Mark.TWO))

  //   O | 2 | X
  //  -----------
  //   O | 5 | X
  //  -----------
  //   7 | 8 | X
  val X_WINNING_COL = Board()
      .make(Move(3, Mark.ONE))
      .make(Move(1, Mark.TWO))
      .make(Move(6, Mark.ONE))
      .make(Move(4, Mark.TWO))
      .make(Move(9, Mark.ONE))

  //   O | 2 | 3
  //  -----------
  //   O | X | X
  //  -----------
  //   O | 8 | X
  val O_WINNING_COL = Board()
      .make(Move(5, Mark.ONE))
      .make(Move(1, Mark.TWO))
      .make(Move(6, Mark.ONE))
      .make(Move(4, Mark.TWO))
      .make(Move(9, Mark.ONE))
      .make(Move(7, Mark.TWO))
}