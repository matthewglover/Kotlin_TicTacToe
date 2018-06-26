package ui

import com.winterbe.expekt.expect
import game.Board
import game.Move
import game.Mark
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object BoardRendererSpec : Spek({
  describe("ui.BoardRenderer::render") {
    it("renders an empty board") {
      val board = Board()

      expect(BoardRenderer.render(board)).to.equal(
          " 1 | 2 | 3\n" +
              "-----------\n" +
              " 4 | 5 | 6\n" +
              "-----------\n" +
              " 7 | 8 | 9"
      )
    }

    it("renders taken squares") {
      val board = Board()
          .make(Move(1, Mark.ONE))
          .make(Move(2, Mark.TWO))
          .make(Move(3, Mark.ONE))
          .make(Move(4, Mark.TWO))
          .make(Move(5, Mark.TWO))
          .make(Move(6, Mark.ONE))
          .make(Move(7, Mark.ONE))
          .make(Move(8, Mark.ONE))
          .make(Move(9, Mark.TWO))

      expect(BoardRenderer.render(board)).to.equal(
          " X | O | X\n" +
              "-----------\n" +
              " O | O | X\n" +
              "-----------\n" +
              " X | X | O"
      )
    }
  }
})
