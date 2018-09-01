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

    it("does a thing") {
      every { io.clearScreen() } just Runs
      every { io.write(any()) } just Runs

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
      val (mockIO, actual) = Helper.requestMoveWith(board, "1")
      val expected = BoardStates.takeTiles(board, 1)

      it("clears screen") {
        expect(mockIO.callsTo("clearScreen")).to.equal(1)
      }

      it("writes move request message to output") {
        expect(mockIO.callsTo("write")).to.equal(1)
        expect(mockIO.written.first()).to.equal(
            BoardRenderer(board).rendered + "\n" +
                "${Mark.ONE}'s turn! Choose your move: "
        )
      }

      it("reads input and parses it to an integer") {
        expect(mockIO.callsTo("readLine")).to.equal(1)
        expect(actual).to.equal(expected)
      }
    }

    context("with non-integer input") {
      val board = Board()
      val nextBoard = BoardStates.takeTiles(board, 2)
      val invalidInput = "blah"
      val validInput = "2"
      val (mockIO, actualBoard) = Helper.requestMoveWith(board, invalidInput, validInput)

      it("parses valid input") {
        expect(actualBoard).to.equal(nextBoard)
      }

      it("reads two lines of input (once for each move request)") {
        expect(mockIO.callsTo("readLine")).to.equal(2)
      }

      it("clears screen twice (once for each move request)") {
        expect(mockIO.callsTo("clearScreen")).to.equal(2)
      }

      it("writes two messages to output (one for each move request)") {
        expect(mockIO.callsTo("write")).to.equal(2)
      }

      it("firstly, writes a request message for Mark TWO's move") {
        expect(mockIO.written.first()).to.equal(
            BoardRenderer(board).rendered + "\n" +
                "${Mark.ONE}'s turn! Choose your move: "
        )
      }

      it("secondly, warns of invalid (non-integer) input and writes another request message for Mark TWO's move") {
        expect(mockIO.written.last()).to.equal(
            BoardRenderer(board).rendered + "\n" +
                NonInteger.message + "\n" +
                "${Mark.ONE}'s turn! Choose your move: "
        )
      }
    }

    context("with move taken input") {
      val board = BoardStates.takeTiles(BoardStates.EMPTY, 1)
      val invalidInput = "1"
      val validInput = "2"
      val (mockIO, actual) = Helper.requestMoveWith(board, invalidInput, validInput)
      val expected = BoardStates.takeTiles(board, 2)

      it("parses valid input and returns updated board") {
        expect(actual).to.equal(expected)
      }

      it("reads two lines of input (one for each move request)") {
        expect(mockIO.callsTo("readLine")).to.equal(2)
      }

      it("clears screen twice (once for each move request)") {
        expect(mockIO.callsTo("clearScreen")).to.equal(2)
      }

      it("writes two messages to output (one for each move request)") {
        expect(mockIO.callsTo("write")).to.equal(2)
      }

      it("firstly, writes a request message for Mark TWO's move") {
        expect(mockIO.written.first()).to.equal(
            BoardRenderer(board).rendered + "\n" +
                "${Mark.TWO}'s turn! Choose your move: "
        )
      }

      it("secondly, warns of invalid input and writes another request message for Mark TWO's move") {
        expect(mockIO.written.last()).to.equal(
            BoardRenderer(board).rendered + "\n" +
                MoveTaken.message + "\n" +
                "${Mark.TWO}'s turn! Choose your move: "
        )
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

object Helper {
  fun buildUI() = with(MockIO()) {
    Pair(UI(this), this)
  }

  fun requestMoveWith(board: Board, vararg inputs: String): Pair<MockIO, Board> {
    val (ui, mockIO) = Helper.buildUI()
    inputs.forEach { input ->
      mockIO.toRead.add(input)
    }

    val actual = ui.requestMove(board)

    return Pair(mockIO, actual)
  }
}
