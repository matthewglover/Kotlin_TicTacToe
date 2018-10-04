package game

import core.Board
import core.Mark
import gameOptions.PlayerTypes

object GameFactory {
  fun from(ui: UI, playerTypes: PlayerTypes, board: Board = Board(), delayMove: DelayMove = DelayMove(1000L)): Game {
    val playerFactory = PlayerFactory(ui, delayMove)
    val playerOne = playerFactory.from(playerTypes.first, Mark.ONE)
    val playerTwo = playerFactory.from(playerTypes.second, Mark.TWO)

    return Game(playerOne, playerTwo, board)
  }
}
