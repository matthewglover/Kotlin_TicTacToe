package game

import ui.ConsoleIO
import ui.UI

fun playGame(game: Game) {
  if (game.isOver) {
    game.notifyResult()
  }
  else {
    playGame(game.next())
  }
}

fun main(args: Array<String>) {
  val ui = UI(ConsoleIO)
  val p1 = Player(ui, Mark.ONE)
  val p2 = Player(ui, Mark.TWO)
  val board = Board()
  val game = Game(p1, p2, board)

  playGame(game)
}
