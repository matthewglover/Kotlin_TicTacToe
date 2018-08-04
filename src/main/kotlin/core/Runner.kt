package core

import game.ConsoleIO
import game.Game
import game.GameFactory
import game.UI

object Runner {
  fun run(game: Game) {
    playGame(game)
  }

  private fun onGameOver() {}

  private fun playGame(game: Game) {
    game.next(::playGame, ::onGameOver)
  }
}

fun main(args: Array<String>) {
  val ui = UI(ConsoleIO)
  val game = GameFactory.from(ui)

  Runner.run(game)
}
