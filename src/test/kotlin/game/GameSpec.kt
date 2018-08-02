package game

import com.winterbe.expekt.expect
import io.mockk.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import ui.UI

object GameSpec : Spek({
  describe("next") {
    context("player 1's next move") {
      it("calls callback with Game updated with p1's next move") {
        val mockUI = mockk<UI>()
        val game = GameFactory.from(mockUI)
        val nextBoard = BoardStates.runMoves(BoardStates.EMPTY, Move(1, Mark.ONE))
        val nextGame = GameFactory.from(mockUI, nextBoard)
        val cb = mockk<(Game) -> Unit>()

        every { cb(any()) } just Runs
        every { mockUI.requestMove(any(), any()) } returns nextBoard

        game.next(cb)

        verify { cb(nextGame) }
      }
    }

    context("player 2's next move") {
      it("calls callback with Game updated with p2's next move") {
        val mockUI = mockk<UI>()
        val initialBoard = BoardStates.runMoves(BoardStates.EMPTY, Move(1, Mark.ONE))
        val game = GameFactory.from(mockUI, initialBoard)
        val nextBoard = BoardStates.runMoves(initialBoard, Move(2, Mark.TWO))
        val nextGame = GameFactory.from(mockUI, nextBoard)
        val cb = mockk<(Game) -> Unit>()

        every { cb(any()) } just Runs
        every { mockUI.requestMove(any(), any()) } returns nextBoard

        game.next(cb)

        verify { cb(nextGame) }
      }
    }
  }

  describe("isOver") {
    it("is over when board drawn") {
      val (game) = createGame(BoardStates.COMPLETE)

      expect(game.isOver).to.be.`true`
    }

    it("is over when either player wins with row") {
      arrayOf(BoardStates.X_WINNING_ROW, BoardStates.O_WINNING_ROW).forEach { boardState ->
        val (game) = createGame(boardState)

        expect(game.isOver).to.be.`true`
      }
    }

    it("is over when either player wins with column") {
      arrayOf(BoardStates.X_WINNING_COL, BoardStates.O_WINNING_COL).forEach { boardState ->
        val (game) = createGame(boardState)

        expect(game.isOver).to.be.`true`
      }
    }

    it("is not over when game is not finished") {
      val (game) = createGame(BoardStates.EMPTY)

      expect(game.isOver).to.be.`false`
    }
  }

  describe("notifyResult") {
    it("notifies players when game is over") {
      val (game, p1, p2) = createGame(BoardStates.COMPLETE)

      every { p1.notifyResult(any()) } just Runs
      every { p2.notifyResult(any()) } just Runs

      game.notifyResult()

      verifyAll {
        p1.notifyResult(BoardStates.COMPLETE)
        p2.notifyResult(BoardStates.COMPLETE)
      }
    }

    it("does not notify players when game is not over") {
      val (game, p1, p2) = createGame(BoardStates.EMPTY)

      game.notifyResult()

      verify(exactly = 0) { p1.notifyResult(any()) }
      verify(exactly = 0) { p2.notifyResult(any()) }
    }
  }
})

fun createGame(board: Board): GameConfig {
  val p1 = mockk<Player>()
  val p2 = mockk<Player>()

  return GameConfig(Game(p1, p2, board), p1, p2, board)
}

data class GameConfig(val game: Game, val p1: Player, val p2: Player, val board: Board)
