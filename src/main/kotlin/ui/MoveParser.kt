package ui

import arrow.core.Either
import arrow.core.flatMap
import game.*

class MoveParser(private val board: Board, private val mark: Mark) {
  fun parse(input: String?): Either<InvalidData, Board> =
      parseToString(input)
          .flatMap(::parseToInt)
          .flatMap(::makeMove)

  private fun parseToInt(input: String) =
      try {
        Either.Right(input.toInt())
      } catch (e: NumberFormatException) {
        Either.Left(NonInteger)
      }

  private fun parseToString(input: String?) =
      if (input == null)
        Either.Left(NonInteger)
      else
        Either.Right(input)

  private fun makeMove(tileNumber: Int) =
      board.make(Move(tileNumber, mark))
}
