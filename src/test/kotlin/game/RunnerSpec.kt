package game

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object RunnerSpec : Spek({
  describe("run") {
    context("game complete") {
      val game = mockk<Game>()

      every { game.next(any(), any()) } answers { secondArg<() -> Unit>()() }

      it("calls onGameOver callback immediately") {
        Runner.run(game)

        verify(exactly = 1) { game.next(any(), any()) }
      }
    }

    context("game with one move remaining") {
      val game = mockk<Game>()
      var callCount = 0;

      every { game.next(any(), any()) } answers {
        callCount++
        if (callCount == 2) {
          secondArg<() -> Unit>()()
        } else {
          firstArg<(Game) -> Unit>()(game)
        }
      }

      it("calls onGameOver callback immediately") {
        Runner.run(game)

        verify(exactly = 2) { game.next(any(), any()) }
      }
    }
  }
})
