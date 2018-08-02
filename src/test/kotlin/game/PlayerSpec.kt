package game

import io.mockk.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import ui.UI

object PlayerSpec : Spek({
  describe("requestMove") {
    context("when it is player's move") {
      it("calls the callback with the updated board") {
        val (ui, player, mark) = createPlayer(Mark.ONE)
        val board = BoardStates.EMPTY
        val nextBoard = BoardStates.runMoves(board, Move(1, mark))
        val cb = mockk<(Board) -> Unit>(relaxed = true)

        every { ui.requestMove(any(), any()) } returns nextBoard

        player.requestMove(board, cb)

        verify { cb(nextBoard) }
      }
    }

    context("when it is not the player's move") {
      it("does not call the callback") {
        val (ui, player, mark) = createPlayer(Mark.TWO)
        val board = BoardStates.EMPTY
        val nextBoard = BoardStates.runMoves(board, Move(1, mark))
        val cb = mockk<(Board) -> Unit>(relaxed = true)

        every { ui.requestMove(any(), any()) } returns nextBoard

        player.requestMove(board, cb)

        verify(exactly = 0) { cb(any()) }
      }
    }
  }

  describe("notifyResult") {
    context("when Player has won") {
      it("notifies UI of result") {
        val (ui, player) = createPlayer(Mark.ONE)

        every { ui.notifyResult(any()) } just Runs

        player.notifyResult(BoardStates.X_WINNING_ROW)

        verify { ui.notifyResult(BoardStates.X_WINNING_ROW) }
      }
    }

    context("when Player has lost") {
      it("does not notify UI of result") {
        val (ui, player) = createPlayer(Mark.TWO)

        every { ui.notifyResult(any()) } just Runs

        player.notifyResult(BoardStates.X_WINNING_ROW)

        verify(exactly = 0) { ui.notifyResult(BoardStates.X_WINNING_ROW) }
      }
    }

    context("when Player has played drawing move") {
      it("notifies UI of result") {
        val (ui, player) = createPlayer(Mark.ONE)

        every { ui.notifyResult(any()) } just Runs

        player.notifyResult(BoardStates.COMPLETE)

        verify(exactly = 1) { ui.notifyResult(BoardStates.COMPLETE) }
      }
    }

    context("when Player has not played drawing move") {
      it("does not notify UI of result") {
        val (ui, player) = createPlayer(Mark.TWO)

        every { ui.notifyResult(any()) } just Runs

        player.notifyResult(BoardStates.COMPLETE)

        verify(exactly = 0) { ui.notifyResult(BoardStates.COMPLETE) }
      }
    }
  }
})

fun createPlayer(mark: Mark): PlayerConfig {
  val ui = mockk<UI>()
  val player = Player(ui, mark)

  return PlayerConfig(ui, player, mark)
}

data class PlayerConfig(val ui: UI, val player: Player, val mark: Mark)
