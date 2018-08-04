package game

import core.Board
import core.Mark

object GameFactory {
  fun from(ui: UI, board: Board = Board()) = Game(Player(ui, Mark.ONE), Player(ui, Mark.TWO), board)
}
