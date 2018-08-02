package game

import ui.ConsoleIO
import ui.UI

fun playGame(game: Game) {
  if (game.isOver) {
    game.notifyResult()
  }
  else {
    game.next(::playGame)
  }
}

fun main(args: Array<String>) {
  val ui = UI(ConsoleIO)
  val game = GameFactory.from(ui)

  playGame(game)
}
