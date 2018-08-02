package game

import ui.UI

object GameFactory {
  fun from(ui: UI) = Game(Player(ui, Mark.ONE), Player(ui, Mark.TWO), Board())
}