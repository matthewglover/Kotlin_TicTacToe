package game

import core.Board
import core.Mark
import gameOptions.PlayerTypes

object GameFactory {
  fun from(ui: UI, playerTypes: PlayerTypes, board: Board = Board(), delayMove: DelayMove = DelayMove(1000L)): Game {
    val playerOne = PlayerFactory.from(ui, playerTypes.first, Mark.ONE, delayMove)
    val playerTwo = PlayerFactory.from(ui, playerTypes.second, Mark.TWO, delayMove)

    return Game(playerOne, playerTwo, board)
  }
}
