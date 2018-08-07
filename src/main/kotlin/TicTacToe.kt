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
    val ui = UI(ConsoleIO)
    val game = GameFactory.from(ui, selection)

    Runner.run(game)
  }
}

fun main(args: Array<String>) {
  val ticTacToe = TicTacToe(ConsoleIO)

  ticTacToe.run()
}
