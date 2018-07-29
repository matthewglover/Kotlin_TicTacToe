package game

import com.winterbe.expekt.expect
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object BoardSpec : Spek({
  describe("isComplete") {
    it("is complete when all moves are taken") {
      expect(BoardStates.COMPLETE.isComplete).to.be.`true`
    }

    it("is complete when either player has a winning line") {
      Mark.values().forEach { mark ->
        BoardSpecHelper.winningLinesFor(mark).forEach { winningLine ->
          expect(Board(winningLine).isComplete).to.be.`true`
        }
      }
    }

    it("is not complete when there is no winner and there are moves remaining") {
      expect(BoardStates.EMPTY.isComplete).to.be.`false`
    }
  }

  describe("lastMark") {
    it("is null for empty board") {
      expect(BoardStates.EMPTY.lastMark).to.be.`null`
    }

    it("is mark of last move made") {
      expect(BoardStates.X_WINNING_ROW.lastMark).to.equal(Mark.ONE)
      expect(BoardStates.O_WINNING_COL.lastMark).to.equal(Mark.TWO)
    }
  }
})

object BoardSpecHelper {
  private val lines: List<Line>
    get() = listOf(
        listOf(1, 2, 3),
        listOf(4, 5, 6),
        listOf(7, 8, 9),
        listOf(1, 4, 7),
        listOf(2, 5, 8),
        listOf(3, 6, 9),
        listOf(1, 5, 9),
        listOf(3, 5, 7)
    )

  fun winningLinesFor(mark: Mark) =
      lines.map { lineOfMark(it, mark) }

  private fun lineOfMark(line: List<Int>, mark: Mark) =
      line.map { Move(it, mark) }
}
