package ui

import game.InvalidData

object NonInteger : InvalidData {
  override val message = "Oops, that's not a valid move!"
}