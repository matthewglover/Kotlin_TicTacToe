package ui

import com.winterbe.expekt.expect
import game.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object UISpec : Spek({
  describe("#requestMove") {
    context("when valid input") {
      val board = BoardStates.EMPTY
      val (mockIO, actual) = Helper.requestMoveWith(board, Mark.ONE, "1")
      val expected = BoardStates.runMoves(board, Move(1, Mark.ONE))

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
      val nextBoard = BoardStates.runMoves(board, Move(2, Mark.ONE))
      val invalidInput = "blah"
      val validInput = "2"
      val (mockIO, actualBoard) = Helper.requestMoveWith(board, Mark.ONE, invalidInput, validInput)

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
                InvalidInput.NON_INTEGER.message + "\n" +
                "${Mark.ONE}'s turn! Choose your move: "
        )
      }
    }

    context("with move taken input") {
      val board = BoardStates.runMoves(BoardStates.EMPTY, Move(1, Mark.ONE))
      val invalidInput = "1"
      val validInput = "2"
      val (mockIO, actual) = Helper.requestMoveWith(board, Mark.TWO, invalidInput, validInput)
      val expected = BoardStates.runMoves(board, Move(2, Mark.TWO))

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
                InvalidInput.MOVE_TAKEN.message + "\n" +
                "${Mark.TWO}'s turn! Choose your move: "
        )
      }
    }
  }

  describe("notifyResult") {
    context("when there's a winner") {
      it("displays final board and notifies winner") {
        listOf(BoardStates.X_WINNING_ROW, BoardStates.O_WINNING_COL)
            .forEach { board ->
              val (ui, mockIO) = Helper.buildUI()
              mockIO.toRead.add("a")

              ui.notifyResult(board)

              expect(mockIO.callsTo("clearScreen")).to.equal(1)
              expect(mockIO.callsTo("write")).to.equal(1)
              expect(mockIO.written.first()).to.equal(
                  BoardRenderer(board).rendered + "\n" +
                      "Congratulations! ${board.winner} wins!\n" +
                      "Press return to continue... "
              )
            }
      }
    }

    context("when it's a draw") {
      it("displays final board and notifies draw") {
        val (ui, mockIO) = Helper.buildUI()
        mockIO.toRead.add("a")

        ui.notifyResult(BoardStates.COMPLETE)

        expect(mockIO.callsTo("clearScreen")).to.equal(1)
        expect(mockIO.callsTo("write")).to.equal(1)
        expect(mockIO.written.first()).to.equal(
            BoardRenderer(BoardStates.COMPLETE).rendered + "\n" +
                "It's a draw!\n" +
                "Press return to continue... "
        )
      }
    }

    context("when game is not over") {
      it("displays current board and notifies game is not over") {
        val (ui, mockIO) = Helper.buildUI()
        mockIO.toRead.add("a")

        ui.notifyResult(BoardStates.EMPTY)

        expect(mockIO.callsTo("clearScreen")).to.equal(1)
        expect(mockIO.callsTo("write")).to.equal(1)
        expect(mockIO.written.first()).to.equal(
            BoardRenderer(BoardStates.EMPTY).rendered + "\n" +
                "Game still active!\n" +
                "Press return to continue... "
        )
      }
    }
  }
})

object Helper {
  fun buildUI() = with(MockIO()) {
    Pair(UI(this), this)
  }

  fun requestMoveWith(board: Board, mark: Mark, vararg inputs: String): Pair<MockIO, Board> {
    val (ui, mockIO) = Helper.buildUI()
    inputs.forEach { input ->
      mockIO.toRead.add(input)
    }

    val actual = ui.requestMove(board, mark)

    return Pair(mockIO, actual)
  }
}

