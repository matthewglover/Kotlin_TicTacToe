package game

import com.winterbe.expekt.expect
import core.*
import io.mockk.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object UISpec : Spek({
  describe("PlayerUI") {
    describe("#reportMoveRequired") {
      val board = BoardStates.EMPTY
      val (_, io, playerUI) = setupGame(board)

      it("renders current board and requests move from next player") {
        playerUI.reportMoveRequired(board)

        verify {
          io.write(BoardRenderer(board).rendered + "\n" +
              "${Mark.ONE}'s turn!")
        }
      }
    }

    describe("#requestMove") {
      context("when valid input") {
        val board = BoardStates.EMPTY
        val (_, io, playerUI) = setupGame(board)
        val nextBoard = BoardStates.takeTiles(board, 1)

        every { io.readLine() } returns "1"

        it("reports move required and reads input from io") {
          expect(playerUI.requestMove(board)).to.equal(nextBoard)

          verifySequence {
            io.clearScreen()
            io.write(
                BoardRenderer(board).rendered + "\n" +
                    "${Mark.ONE}'s turn! Choose your move: "
            )
            io.readLine()
          }
        }
      }

      context("with non-integer input") {
        val board = BoardStates.EMPTY
        val (_, io, playerUI) = setupGame(board)
        val nextBoard = BoardStates.takeTiles(board, 2)
        val invalidInput = "blah"
        val validInput = "2"

        every { io.readLine() } returnsMany listOf(invalidInput, validInput)

        it("requests input until valid") {
          expect(playerUI.requestMove(board)).to.equal(nextBoard)

          verifySequence {
            io.clearScreen()
            io.write(
                BoardRenderer(board).rendered + "\n" +
                    "${Mark.ONE}'s turn! Choose your move: "
            )
            io.readLine()
            io.clearScreen()
            io.write(
                BoardRenderer(board).rendered + "\n" +
                    NonInteger.message + "\n" +
                    "${Mark.ONE}'s turn! Choose your move: "
            )
            io.readLine()
          }
        }
      }

      context("with move taken input") {
        val board = BoardStates.takeTiles(BoardStates.EMPTY, 1)
        val (_, io, playerUI) = setupGame(board)
        val nextBoard = BoardStates.takeTiles(board, 2)
        val invalidInput = "1"
        val validInput = "2"

        every { io.readLine() } returnsMany listOf(invalidInput, validInput)

        it("requests input until valid") {
          expect(playerUI.requestMove(board)).to.equal(nextBoard)

          verifySequence {
            io.clearScreen()
            io.write(
                BoardRenderer(board).rendered + "\n" +
                    "${Mark.TWO}'s turn! Choose your move: "
            )
            io.readLine()
            io.clearScreen()
            io.write(
                BoardRenderer(board).rendered + "\n" +
                    MoveTaken.message + "\n" +
                    "${Mark.TWO}'s turn! Choose your move: "
            )
            io.readLine()
          }
        }
      }
    }
  }

  describe("GameUI") {
    describe("notifyResult") {
      context("when there's a winner") {
        it("displays final board and notifies winner") {
          listOf(BoardStates.MARK_ONE_WINNING_ROW, BoardStates.MARK_TWO_WINNING_COL)
              .forEach { board ->
                val (game, io, _, gameUI) = setupGame(board)

                every { io.clearScreen() } just Runs
                every { io.write(any()) } just Runs
                every { io.readLine() } returns "anything"

                gameUI.notifyResult(game)

                verifySequence {
                  io.clearScreen()
                  io.write(
                      BoardRenderer(board).rendered + "\n" +
                          "Congratulations! ${board.winner} wins!\n" +
                          "Press return to continue... "
                  )
                  io.readLine()
                }
              }
        }
      }

      context("when it's a draw") {
        it("displays final board and notifies draw") {
          val board = BoardStates.COMPLETE
          val (game, io, _, gameUI) = setupGame(board)

          every { io.readLine() } returns "anything"

          gameUI.notifyResult(game)

          verifySequence {
            io.clearScreen()
            io.write(
                BoardRenderer(board).rendered + "\n" +
                    "It's a draw!\n" +
                    "Press return to continue... "
            )
            io.readLine()
          }
        }
      }

      context("when game is not over") {
        it("displays current board and notifies game is not over") {
          val board = BoardStates.EMPTY
          val (game, io, _, gameUI) = setupGame(board)

          every { io.readLine() } returns "anything"

          gameUI.notifyResult(game)

          verifySequence {
            io.clearScreen()
            io.write(
                BoardRenderer(board).rendered + "\n" +
                    "Game still active!\n" +
                    "Press return to continue... "
            )
            io.readLine()
          }
        }
      }
    }
  }
})

fun setupGame(board: Board): Quad<Game, IO, PlayerUI, GameUI> {
  val game = mockk<Game>()
  val io = mockk<IO>()
  val playerUI = PlayerUI(io)
  val gameUI = GameUI(io)

  every { game.board } returns board

  every { io.clearScreen() } just Runs
  every { io.write(any()) } just Runs

  return Quad(game, io, playerUI, gameUI)
}
