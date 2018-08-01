package ui

import game.Board
import arrow.core.Either
import arrow.core.flatMap
import game.InvalidInput
import game.Mark
import game.Move

class MoveParser(private val board: Board, private val mark: Mark) {
  fun parse(input: String?): Either<InvalidInput, Board> =
      parseToString(input)
          .flatMap(::parseToInt)
          .flatMap(::makeMove)

  private fun parseToInt(input: String) =
      try {
        Either.Right(input.toInt())
      } catch (e: NumberFormatException) {
        Either.Left(InvalidInput.NON_INTEGER)
      }

  private fun parseToString(input: String?) =
      if (input == null)
        Either.Left(InvalidInput.NON_INTEGER)
      else
        Either.Right(input)

  private fun makeMove(tileNumber: Int) =
      board.make(Move(tileNumber, mark))
}