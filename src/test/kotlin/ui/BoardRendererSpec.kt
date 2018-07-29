package ui

import com.winterbe.expekt.expect
import game.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object BoardRendererSpec : Spek({
  describe("ui.BoardRenderer::rendered") {
    it("renders an empty board") {
      expect(BoardRenderer(BoardStates.EMPTY).rendered).to.equal(
          " 1 | 2 | 3\n" +
          "-----------\n" +
          " 4 | 5 | 6\n" +
          "-----------\n" +
          " 7 | 8 | 9"
      )
    }

    it("renders taken squares") {
      expect(BoardRenderer(BoardStates.COMPLETE).rendered).to.equal(
          " X | O | X\n" +
          "-----------\n" +
          " O | O | X\n" +
          "-----------\n" +
          " X | X | O"
      )
    }
  }
})
