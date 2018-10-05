package game

import core.Board
import core.BoardStates
import gameOptions.PlayerType
import io.mockk.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object GameSpec : Spek({
  describe("next") {
    val playerTypes = Pair(PlayerType.HUMAN, PlayerType.HUMAN)

    context("player 1's move") {
      it("calls onGameUpdate callback with Game updated with p1's next move") {
        val board = BoardStates.EMPTY
        val nextBoard = BoardStates.takeTiles(board, 1)
        val (playerUI, onGameUpdate, onGameOver) = buildMocks(board, nextBoard)
        val game = GameFactory.from(playerUI, playerTypes, board)
        val nextGame = GameFactory.from(playerUI, playerTypes, nextBoard)

        game.next(onGameUpdate, onGameOver)

        verify(exactly = 1) { onGameUpdate(nextGame) }
        verify(exactly = 0) { onGameOver(any()) }
      }
    }

    context("player 2's move") {
      it("calls onGameUpdate callback with Game updated with p2's next move") {
        val board = BoardStates.takeTiles(BoardStates.EMPTY, 1)
        val nextBoard = BoardStates.takeTiles(board, 2)
        val (playerUI, onGameUpdate, onGameOver) = buildMocks(board, nextBoard)
        val game = GameFactory.from(playerUI, playerTypes, board)
        val nextGame = GameFactory.from(playerUI, playerTypes, nextBoard)

        game.next(onGameUpdate, onGameOver)

        verify(exactly = 1) { onGameUpdate(nextGame) }
        verify(exactly = 0) { onGameOver(any()) }
      }
    }

    context("game is drawn") {
      it("calls onGameOver callback with current Game") {
        val board = BoardStates.COMPLETE
        val (playerUI, onGameUpdate, onGameOver) = buildMocks(board)
        val game = GameFactory.from(playerUI, playerTypes, board)

        game.next(onGameUpdate, onGameOver)

        verify(exactly = 0) { onGameUpdate(any()) }
        verify(exactly = 1) { onGameOver(any()) }
      }
    }

    context("player wins") {
      with(BoardStates) {
        listOf(MARK_ONE_WINNING_ROW, MARK_ONE_WINNING_COL, MARK_TWO_WINNING_ROW, MARK_TWO_WINNING_COL).forEach { board ->
          val (playerUI, onGameUpdate, onGameOver) = buildMocks(board)
          val game = GameFactory.from(playerUI, playerTypes, board)

          it("calls onGameOver callback with current Game") {
            game.next(onGameUpdate, onGameOver)

            verify(exactly = 0) { onGameUpdate(any()) }
            verify(exactly = 1) { onGameOver(any()) }
          }
        }
      }
    }
  }
})

fun buildMocks(currentBoard: Board, nextBoard: Board? = null): Triple<PlayerUI, (Game) -> Unit, (Game) -> Unit> {
  val playerUI = mockk<PlayerUI>()
  val onGameUpdate = mockk<(Game) -> Unit>()
  val onGameOver = mockk<(Game) -> Unit>()

  every { onGameUpdate(any()) } just Runs
  every { onGameOver(any()) } just Runs

  if (nextBoard != null) {
    every { playerUI.requestMove(currentBoard) } returns nextBoard
  }

  return Triple(playerUI, onGameUpdate, onGameOver)
}
