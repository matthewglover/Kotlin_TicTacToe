package game

import core.Mark
import gameOptions.PlayerType

object PlayerFactory {
  fun from(ui: UI, playerType: PlayerType, mark: Mark) =
      when(playerType) {
        PlayerType.HUMAN -> HumanPlayer(ui, mark)
        PlayerType.COMPUTER -> ComputerPlayer(ui, mark)
      }
}