package game

import io.mockk.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import ui.UI

object GameSpec : Spek({
  describe("next") {
    context("player 1's move") {
      it("calls onGameUpdate callback with Game updated with p1's next move") {
        val board = BoardStates.EMPTY
        val nextBoard = BoardStates.runMoves(board, Move(1, Mark.ONE))
        val (ui, onGameUpdate, onGameOver) = buildMocks(board, nextBoard)
        val game = GameFactory.from(ui, board)
        val nextGame = GameFactory.from(ui, nextBoard)

        game.next(onGameUpdate, onGameOver)

        verify(exactly = 1) { onGameUpdate(nextGame) }
        verify(exactly = 0) { onGameOver() }
      }
    }

    context("player 2's move") {
      it("calls onGameUpdate callback with Game updated with p2's next move") {
        val board = BoardStates.runMoves(BoardStates.EMPTY, Move(1, Mark.ONE))
        val nextBoard = BoardStates.runMoves(board, Move(2, Mark.TWO))
        val (ui, onGameUpdate, onGameOver) = buildMocks(board, nextBoard)
        val game = GameFactory.from(ui, board)
        val nextGame = GameFactory.from(ui, nextBoard)

        game.next(onGameUpdate, onGameOver)

        verify(exactly = 1) { onGameUpdate(nextGame) }
        verify(exactly = 0) { onGameOver() }
      }
    }

    context("game is drawn") {
      it("calls onGameOver callback with current Game") {
        val board = BoardStates.COMPLETE
        val (ui, onGameUpdate, onGameOver) = buildMocks(board)
        val game = GameFactory.from(ui, board)

        game.next(onGameUpdate, onGameOver)

        verify(exactly = 0) { onGameUpdate(any()) }
        verify(exactly = 1) { onGameOver() }
      }
    }

    context("player wins") {
      with(BoardStates) {
        listOf(X_WINNING_ROW, X_WINNING_COL, O_WINNING_ROW, O_WINNING_COL).forEach { board ->
          val (ui, onGameUpdate, onGameOver) = buildMocks(board)
          val game = GameFactory.from(ui, board)

          it("calls onGameOver callback with current Game") {
            game.next(onGameUpdate, onGameOver)

            verify(exactly = 0) { onGameUpdate(any()) }
            verify(exactly = 1) { onGameOver() }
          }
        }
      }
    }
  }
})

fun buildMocks(currentBoard: Board, nextBoard: Board? = null): Triple<UI, (Game) -> Unit, () -> Unit> {
  val mockUI = mockk<UI>()
  val onGameUpdate = mockk<(Game) -> Unit>()
  val onGameOver = mockk<() -> Unit>()

  every { onGameUpdate(any()) } just Runs
  every { onGameOver() } just Runs
  every { mockUI.notifyResult(any()) } just Runs
  if (nextBoard != null) {
    every { mockUI.requestMove(currentBoard, any()) } returns nextBoard
  }

  return Triple(mockUI, onGameUpdate, onGameOver)
}
