package game

import arrow.core.Either
import arrow.core.flatMap
import core.*

class MoveParser(private val board: Board) {
  fun parse(input: String?): Either<InvalidData, Board> =
      InputToIntParser.parse(input)
          .flatMap { tileNumber -> board.takeTile(tileNumber) }
}
