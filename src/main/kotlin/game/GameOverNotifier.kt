package game

class GameOverNotifier(private val ui: GameUI) {
  fun build() = { game: Game -> ui.notifyResult(game) }
}