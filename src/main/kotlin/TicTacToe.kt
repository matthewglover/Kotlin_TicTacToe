import game.*
import gameOptions.PlayerType
import gameOptions.PlayerTypeSelector

class TicTacToe(val io: IO) {

  private val selections = arrayOf(
      Pair(PlayerType.HUMAN, PlayerType.HUMAN),
      Pair(PlayerType.HUMAN, PlayerType.COMPUTER),
      Pair(PlayerType.COMPUTER, PlayerType.COMPUTER)
  )

  private val playerTypeSelector = PlayerTypeSelector(io, *selections)

  fun run() {
    val selection = playerTypeSelector.requestSelection()
    val playerUI = PlayerUI(io)
    val gameUI = GameUI(io)
    val onGameOver = GameOverNotifier(gameUI).build()
    val game = GameFactory.from(playerUI, selection)

    Runner.run(game, onGameOver)
  }
}

fun main(args: Array<String>) {
  TicTacToe(ConsoleIO).run()
}
