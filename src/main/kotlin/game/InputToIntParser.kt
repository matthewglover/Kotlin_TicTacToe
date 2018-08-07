package game

import arrow.core.Either
import arrow.core.flatMap
import core.InvalidData

object InputToIntParser {
  fun parse(input: String?): Either<InvalidData, Int> =
      parseString(input)
          .flatMap(::parseInt)

  private fun parseInt(input: String) =
      try {
        Either.Right(input.toInt())
      } catch (e: NumberFormatException) {
        Either.Left(NonInteger)
      }

  private fun parseString(input: String?) =
      if (input == null)
        Either.Left(NonInteger)
      else
        Either.Right(input)
}