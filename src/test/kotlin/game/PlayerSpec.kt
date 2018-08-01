package game

import io.mockk.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import ui.UI

object PlayerSpec : Spek({
  describe("requestMove") {
    it("gets player's move from the UI") {
      val (ui, player, mark) = createPlayer(Mark.ONE)
      val board = BoardStates.EMPTY

      every { ui.requestMove(any(), any()) } returns BoardStates.runMoves(board, Move(1, mark))

      player.requestMove(board)

      verify { ui.requestMove(board, mark) }
    }
  }

  describe("notifyResult") {
    it("notifies UI of result if Player has won") {
      val (ui, player) = createPlayer(Mark.ONE)

      every { ui.notifyResult(any()) } just Runs

      player.notifyResult(BoardStates.X_WINNING_ROW)

      verify { ui.notifyResult(BoardStates.X_WINNING_ROW) }
    }

    it("does not notify UI of result if Player has lost") {
      val (ui, player) = createPlayer(Mark.TWO)

      every { ui.notifyResult(any()) } just Runs

      player.notifyResult(BoardStates.X_WINNING_ROW)

      verify(exactly = 0) { ui.notifyResult(BoardStates.X_WINNING_ROW) }
    }

    it("notifies UI of result if it's a draw AND Player made last move") {
      val (ui, player) = createPlayer(Mark.ONE)

      every { ui.notifyResult(any()) } just Runs

      player.notifyResult(BoardStates.COMPLETE)

      verify(exactly = 1) { ui.notifyResult(BoardStates.COMPLETE) }
    }

    it("does not notify UI of result if it's a draw AND Player did not make last move") {
      val (ui, player) = createPlayer(Mark.TWO)

      every { ui.notifyResult(any()) } just Runs

      player.notifyResult(BoardStates.COMPLETE)

      verify(exactly = 0) { ui.notifyResult(BoardStates.COMPLETE) }
    }
  }
})

fun createPlayer(mark: Mark): PlayerConfig {
  val ui = mockk<UI>()
  val player = Player(ui, mark)

  return PlayerConfig(ui, player, mark)
}

data class PlayerConfig(val ui: UI, val player: Player, val mark: Mark)
