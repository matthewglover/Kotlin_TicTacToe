package game

import com.winterbe.expekt.expect
import core.*
import io.mockk.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object UISpec : Spek({
  describe("#reportMoveRequired") {
    val board = BoardStates.EMPTY
    val io = mockk<IO>()
    val ui = UI(io)

    every { io.clearScreen() } just Runs
    every { io.write(any()) } just Runs

    it("renders current board and requests move from next player") {
      ui.reportMoveRequired(board)

      verify {
        io.write(BoardRenderer(board).rendered + "\n" +
            "${Mark.ONE}'s turn!")
      }
    }
  }

  describe("#requestMove") {
    context("when valid input") {
      val board = BoardStates.EMPTY
      val nextBoard = BoardStates.takeTiles(board, 1)
      val io = mockk<IO>()
      val ui = UI(io)

      every { io.clearScreen() } just Runs
      every { io.write(any()) } just Runs
      every { io.readLine() } returns "1"

      it("reports move required and reads input from io") {
        expect(ui.requestMove(board)).to.equal(nextBoard)

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
      val nextBoard = BoardStates.takeTiles(board, 2)
      val invalidInput = "blah"
      val validInput = "2"

      val io = mockk<IO>()
      val ui = UI(io)

      every { io.clearScreen() } just Runs
      every { io.write(any()) } just Runs
      every { io.readLine() } returnsMany listOf(invalidInput, validInput)

      it("requests input until valid") {
        expect(ui.requestMove(board)).to.equal(nextBoard)

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
      val nextBoard = BoardStates.takeTiles(board, 2)
      val invalidInput = "1"
      val validInput = "2"

      val io = mockk<IO>()
      val ui = UI(io)

      every { io.clearScreen() } just Runs
      every { io.write(any()) } just Runs
      every { io.readLine() } returnsMany listOf(invalidInput, validInput)

      it("requests input until valid") {
        expect(ui.requestMove(board)).to.equal(nextBoard)

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

  describe("notifyResult") {
    context("when there's a winner") {
      it("displays final board and notifies winner") {
        listOf(BoardStates.MARK_ONE_WINNING_ROW, BoardStates.MARK_TWO_WINNING_COL)
            .forEach { board ->
              val io = mockk<IO>()
              val ui = UI(io)

              every { io.clearScreen() } just Runs
              every { io.write(any()) } just Runs
              every { io.readLine() } returns "anything"

              ui.notifyResult(board)

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
        val io = mockk<IO>()
        val ui = UI(io)

        every { io.clearScreen() } just Runs
        every { io.write(any()) } just Runs
        every { io.readLine() } returns "anything"

        ui.notifyResult(BoardStates.COMPLETE)

        verifySequence {
          io.clearScreen()
          io.write(
              BoardRenderer(BoardStates.COMPLETE).rendered + "\n" +
                  "It's a draw!\n" +
                  "Press return to continue... "
          )
          io.readLine()
        }
      }
    }

    context("when game is not over") {
      it("displays current board and notifies game is not over") {
        val io = mockk<IO>()
        val ui = UI(io)

        every { io.clearScreen() } just Runs
        every { io.write(any()) } just Runs
        every { io.readLine() } returns "anything"

        ui.notifyResult(BoardStates.EMPTY)

        verifySequence {
          io.clearScreen()
          io.write(
              BoardRenderer(BoardStates.EMPTY).rendered + "\n" +
                  "Game still active!\n" +
                  "Press return to continue... "
          )
          io.readLine()
        }
      }
    }
  }
})
