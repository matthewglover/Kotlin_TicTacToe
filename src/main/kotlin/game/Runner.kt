package game

object Runner {
  fun run(game: Game) {
    playGame(game)
  }

  private fun onGameOver() {}

  private fun playGame(game: Game) {
    game.next(::playGame, ::onGameOver)
  }
}

