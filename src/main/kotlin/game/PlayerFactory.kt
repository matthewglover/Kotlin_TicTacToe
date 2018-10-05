package game

import core.Mark
import gameOptions.PlayerType

class PlayerFactory(private val ui: PlayerUI, private val delayMove: DelayMove = DelayMove(1000L)) {
  fun from(playerType: PlayerType, mark: Mark) =
      when(playerType) {
        PlayerType.HUMAN -> HumanPlayer(ui, mark)
        PlayerType.COMPUTER -> ComputerPlayer(ui, mark, delayMove)
      }
}