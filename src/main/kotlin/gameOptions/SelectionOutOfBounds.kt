package gameOptions

import core.InvalidData

object SelectionOutOfBounds : InvalidData {
  override val message = "Oops! That's not a valid selection"
}