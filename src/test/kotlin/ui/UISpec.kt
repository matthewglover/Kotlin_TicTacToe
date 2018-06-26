package ui

import com.winterbe.expekt.expect
import game.Board
import game.Move
import game.Mark
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object UISpec : Spek({
  describe("requestMove") {
    it("with valid input, parses input to integer") {
      val (ui, io) = Helper.buildUI()
      val board = Board()
      io.toRead.add("1")

      val actual = ui.requestMove(board, Mark.ONE)

      expect(actual).to.equal(Move(1, Mark.ONE))
      expect(io.callsTo("clearScreen")).to.equal(1)
      expect(io.callsTo("write")).to.equal(1)
      expect(io.written.first()).to.equal(
          BoardRenderer.render(board) + "\n" +
              "${Mark.ONE}'s turn! Choose your move: "
      )
      expect(io.callsTo("read")).to.equal(1)
    }

    it("with non-integer input, warns invalid input and parses next input") {
      val (ui, mockIO) = Helper.buildUI()
      val board = Board()
      mockIO.toRead.add("blah")
      mockIO.toRead.add("2")

      val actual = ui.requestMove(board, Mark.ONE)

      expect(actual).to.equal(Move(2, Mark.ONE))
      expect(mockIO.callsTo("clearScreen")).to.equal(2)
      expect(mockIO.written.first()).to.equal(
          BoardRenderer.render(board) + "\n" +
              "${Mark.ONE}'s turn! Choose your move: "
      )
      expect(mockIO.written.last()).to.equal(
          BoardRenderer.render(board) + "\n" +
              InvalidInput.NON_INTEGER.message + "\n" +
              "${Mark.ONE}'s turn! Choose your move: "
      )
      expect(mockIO.callsTo("read")).to.equal(2)
    }

    it("with move-taken input, warns invalid input and parses next input") {
      val (ui, mockIO) = Helper.buildUI()
      val board = Board().make(Move(1, Mark.ONE))
      mockIO.toRead.add("1")
      mockIO.toRead.add("2")

      val actual = ui.requestMove(board, Mark.TWO)

      expect(actual).to.equal(Move(2, Mark.TWO))
      expect(mockIO.callsTo("clearScreen")).to.equal(2)
      expect(mockIO.callsTo("write")).to.equal(2)
      expect(mockIO.written.first()).to.equal(
          BoardRenderer.render(board) + "\n" +
              "${Mark.TWO}'s turn! Choose your move: "
      )
      expect(mockIO.written.last()).to.equal(
          BoardRenderer.render(board) + "\n" +
              InvalidInput.MOVE_TAKEN.message + "\n" +
              "${Mark.TWO}'s turn! Choose your move: "
      )
      expect(mockIO.callsTo("read")).to.equal(2)
    }
  }
})

object Helper {
  fun buildUI() = with(MockIO()) {
    Pair(UI(this), this)
  }
}

