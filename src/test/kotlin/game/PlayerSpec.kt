package game

import core.Board
import core.BoardStates
import core.Mark
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
        it("calls the update board callback after specified delay") {
          val (ui, player, _, delayMove) = createComputerPlayer(Mark.TWO)
          val onUpdateBoard = mockk<(Board) -> Unit>(relaxed = true)
          val initialBoard = BoardStates.takeTiles(BoardStates.EMPTY, 1)

          every { ui.reportMoveRequired(any()) } just Runs
          every { delayMove.run() } just Runs

          player.requestMove(initialBoard, onUpdateBoard)

          verifySequence {
            delayMove.run()
            onUpdateBoard(any())
          }
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
})

fun createHumanPlayer(mark: Mark) = createPlayer(mark, PlayerType.HUMAN)

fun createComputerPlayer(mark: Mark) = createPlayer(mark, PlayerType.COMPUTER)

fun createPlayer(mark: Mark, playerType: PlayerType): Quad<PlayerUI, Player, Mark, DelayMove> {
  val ui = mockk<PlayerUI>()
  val delayMove = mockk<DelayMove>()
  val playerFactory = PlayerFactory(ui, delayMove)
  val player = playerFactory.from(playerType, mark)

  return Quad(ui, player, mark, delayMove)
}

data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

