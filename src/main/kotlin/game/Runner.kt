package game

import ui.ConsoleIO
import ui.UI

fun onGameOver() {}

fun playGame(game: Game) {
  game.next(::playGame, ::onGameOver)
}

fun main(args: Array<String>) {
  val ui = UI(ConsoleIO)
  val game = GameFactory.from(ui)

  playGame(game)
}
