package game

import io.mockk.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object RunnerSpec : Spek({
  describe("run") {
    context("game complete") {
      val game = mockk<Game>()
      val gameOverHandler = mockk<(Game) -> Unit>()

      every { game.next(any(), any()) } answers { secondArg<(Game) -> Unit>()(game) }
      every { gameOverHandler(any()) } just Runs

      it("calls onGameOver callback immediately") {
        Runner.run(game, gameOverHandler)

        verify(exactly = 1) { game.next(any(), any()) }
      }
    }

    context("game with one move remaining") {
      val game = mockk<Game>()
      val gameOverHandler = mockk<(Game) -> Unit>()
      var callCount = 0;

      every { game.next(any(), any()) } answers {
        callCount++
        if (callCount == 2) {
          secondArg<(Game) -> Unit>()(game)
        } else {
          firstArg<(Game) -> Unit>()(game)
        }
      }
      every { gameOverHandler(any()) } just Runs

      it("calls onGameOver callback immediately") {
        Runner.run(game, gameOverHandler)

        verify(exactly = 2) { game.next(any(), any()) }
      }
    }
  }
})
