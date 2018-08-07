package game

import arrow.core.Either
import arrow.core.flatMap
import core.*

class MoveParser(private val board: Board) {
  fun parse(input: String?): Either<InvalidData, Board> =
      InputToIntParser.parse(input)
          .flatMap(::makeMove)

  private fun makeMove(tileNumber: Int) =
      board.make(Move(tileNumber, board.currentMark))
}
