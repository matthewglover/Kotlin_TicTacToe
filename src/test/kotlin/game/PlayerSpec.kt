package game

import core.Board
import core.BoardStates
import core.Mark
import core.Move
import gameOptions.PlayerType
import io.mockk.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object PlayerSpec : Spek({
  describe("HumanPlayer") {
    describe("#requestMove") {
      context("when it is player's move") {
        it("calls the callback with the updated board") {
          val (ui, player) = createHumanPlayer(Mark.ONE)
          val board = BoardStates.EMPTY
          val nextBoard = BoardStates.takeTiles(board, 1)
          val cb = mockk<(Board) -> Unit>(relaxed = true)

          every { ui.requestMove(any()) } returns nextBoard

          player.requestMove(board, cb)

          verify { cb(nextBoard) }
        }
      }

      context("when it is not the player's move") {
        it("does not call the callback") {
          val (ui, player) = createHumanPlayer(Mark.TWO)
          val board = BoardStates.EMPTY
          val nextBoard = BoardStates.takeTiles(board, 1)
          val cb = mockk<(Board) -> Unit>(relaxed = true)

          every { ui.requestMove(any()) } returns nextBoard

          player.requestMove(board, cb)

          verify(exactly = 0) { cb(any()) }
        }
      }
    }
  }

  describe("ComputerPlayer") {
    describe("#requestMove") {
      context("when it is player's move") {
        it("takes first free square") {
          val (ui, player, _, delayMove) = createComputerPlayer(Mark.TWO)
          val onUpdateBoard = mockk<(Board) -> Unit>(relaxed = true)
          val initialBoard = BoardStates.takeTiles(BoardStates.EMPTY, 1)
          val nextBoard = BoardStates.takeTiles(initialBoard, 2)

          every { ui.reportMoveRequired(any()) } just Runs
          every { delayMove.run() } just Runs

          player.requestMove(initialBoard, onUpdateBoard)

          verify { onUpdateBoard(nextBoard) }
          verify { delayMove.run() }
        }
      }

      context("when it is not the player's move") {
        it("does not call the callback") {
          val (_, player) = createComputerPlayer(Mark.TWO)
          val onUpdateBoard = mockk<(Board) -> Unit>(relaxed = true)
          val initialBoard = BoardStates.EMPTY

          player.requestMove(initialBoard, onUpdateBoard)

          verify(exactly = 0) { onUpdateBoard(any()) }
        }
      }
    }
  }

  describe("#notifyResult") {
    context("when Player has won") {
      it("notifies UI of result") {
        val (ui, player) = createHumanPlayer(Mark.ONE)
        val onGameOver = mockk<() -> Unit>()

        every { ui.notifyResult(any()) } just Runs
        every { onGameOver() } just Runs

        player.notifyResult(BoardStates.MARK_ONE_WINNING_ROW, onGameOver)

        verify { ui.notifyResult(BoardStates.MARK_ONE_WINNING_ROW) }
        verify { onGameOver() }
      }
    }

    context("when Player has lost") {
      it("does not notify UI of result") {
        val (ui, player) = createHumanPlayer(Mark.TWO)
        val onGameOver = mockk<() -> Unit>()

        every { ui.notifyResult(any()) } just Runs
        every { onGameOver() } just Runs

        player.notifyResult(BoardStates.MARK_ONE_WINNING_ROW, onGameOver)

        verify(exactly = 0) { ui.notifyResult(BoardStates.MARK_ONE_WINNING_ROW) }
        verify(exactly = 0) { onGameOver() }
      }
    }

    context("when Player has played drawing move") {
      it("notifies UI of result") {
        val (ui, player) = createHumanPlayer(Mark.ONE)
        val onGameOver = mockk<() -> Unit>()

        every { ui.notifyResult(any()) } just Runs
        every { onGameOver() } just Runs

        player.notifyResult(BoardStates.COMPLETE, onGameOver)

        verify(exactly = 1) { ui.notifyResult(BoardStates.COMPLETE) }
        verify(exactly = 1) { onGameOver() }
      }
    }

    context("when Player has not played drawing move") {
      it("does not notify UI of result") {
        val (ui, player) = createHumanPlayer(Mark.TWO)
        val onGameOver = mockk<() -> Unit>()

        every { ui.notifyResult(any()) } just Runs
        every { onGameOver() } just Runs

        player.notifyResult(BoardStates.COMPLETE, onGameOver)

        verify(exactly = 0) { ui.notifyResult(BoardStates.COMPLETE) }
        verify(exactly = 0) { onGameOver() }
      }
    }
  }
})

fun createHumanPlayer(mark: Mark) = createPlayer(mark, PlayerType.HUMAN)

fun createComputerPlayer(mark: Mark) = createPlayer(mark, PlayerType.COMPUTER)

fun createPlayer(mark: Mark, playerType: PlayerType): Quad<UI, Player, Mark, DelayMove> {
  val ui = mockk<UI>()
  val delayMove = mockk<DelayMove>()
  val player = PlayerFactory.from(ui, playerType, mark, delayMove)

  return Quad(ui, player, mark, delayMove)
}

data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

