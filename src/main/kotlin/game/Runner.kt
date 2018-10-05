package game

object Runner {
  fun run(game: Game, onGameOver: GameUpdateHandler) {
    fun onGameUpdate(game: Game) {
      game.next(::onGameUpdate, onGameOver)
    }

    onGameUpdate(game)
  }
}

