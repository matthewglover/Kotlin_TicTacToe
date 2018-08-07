package gameOptions

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.identity
import core.InvalidData
import game.IO
import game.InputToIntParser

class PlayerTypeSelector(private val io: IO, private vararg val selections: PlayerTypes) {
  private val requestText by lazy {
    "Game Options:\n" +
        "$selectionListing\n" +
        "Enter selection: "
  }

  private val selectionListing by lazy {
    selections
        .mapIndexed(::toPrintableSelection)
        .joinToString(separator = "\n")
  }

  fun requestSelection(invalidInput: InvalidData? = null): PlayerTypes {
    io.clearScreen()
    io.write(requestSelectionText(invalidInput))
    return parseInput()
  }

  private fun parseInput() =
      InputToIntParser.parse(io.readLine())
          .flatMap(::parseSelection)
          .fold(::requestSelection, ::identity)

  private fun parseSelection(itemNumber: Int): Either<InvalidData, PlayerTypes> =
      try {
        Either.Right(selections[itemNumber - 1])
      } catch (exception: ArrayIndexOutOfBoundsException) {
        Either.Left(SelectionOutOfBounds)
      }

  private fun requestSelectionText(invalidInput: InvalidData?) =
    listOfNotNull(
        invalidInput?.message,
        requestText).joinToString(separator = "\n")

  private fun toPrintableSelection(selectionIndex: Int, selection: PlayerTypes): String {
    val (p1, p2) = selection
    return "<${selectionIndex + 1}> ${p1.toPrintableString()} vs ${p2.toPrintableString()}"
  }
}