package game

import core.Board

object Runner {
  fun run(game: Game) {
    playGame(game)
  }

  private fun onGameOver(game: Game) {}

  private fun playGame(game: Game) {
    game.next(::playGame, ::onGameOver)
  }
}

