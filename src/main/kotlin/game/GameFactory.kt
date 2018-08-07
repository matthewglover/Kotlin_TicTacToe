package game

import core.Board
import core.Mark
import gameOptions.PlayerTypes

object GameFactory {
  fun from(ui: UI, playerTypes: PlayerTypes, board: Board = Board()): Game {
    val playerOne = PlayerFactory.from(ui, playerTypes.first, Mark.ONE)
    val playerTwo = PlayerFactory.from(ui, playerTypes.second, Mark.TWO)

    return Game(playerOne, playerTwo, board)
  }
}
