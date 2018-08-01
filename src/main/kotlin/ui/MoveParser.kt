package ui

import game.Board
import arrow.core.Either
import arrow.core.flatMap
import game.Mark
import game.Move

class MoveParser(private val board: Board, private val mark: Mark) {
  fun parse(input: String?): Either<InvalidInput, Move> =
      parseToString(input)
          .flatMap(::parseToInt)
          .flatMap(::validate)
          .map { Move(it, mark) }

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

  private fun validate(tileNumber: Int) =
      if (board.isTileTaken(tileNumber))
        Either.Left(InvalidInput.MOVE_TAKEN)
      else if (board.isTileOutOfBounds(tileNumber))
        Either.Left(InvalidInput.OUT_OF_BOUNDS)
      else
        Either.Right(tileNumber)
}