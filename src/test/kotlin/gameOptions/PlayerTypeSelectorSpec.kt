package gameOptions

import com.winterbe.expekt.expect
import game.IO
import game.NonInteger
import io.mockk.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object PlayerTypeSelectorSpec : Spek({
  describe("requestSelection") {
    val humanVsHuman = Pair(PlayerType.HUMAN, PlayerType.HUMAN)
    val humanVsComputer = Pair(PlayerType.HUMAN, PlayerType.COMPUTER)
    val selections = listOf(humanVsHuman, humanVsComputer)
    val requestSelectionText =
        "Game Options:\n" +
            "<1> Human vs Human\n" +
            "<2> Human vs Computer\n" +
            "Enter selection: "

    context("Human vs Human selected from the UI") {
      val (io, selector) = SpecHelper.buildPlayerTypeSelector(selections)

      every { io.readLine() } returns "1"

      val result = selector.requestSelection()

      it("writes options to output") {
        verify { io.write(eq(requestSelectionText)) }
      }

      it("returns a Pair of (Human, Human)") {
        expect(result).to.equal(humanVsHuman)
      }
    }

    context("Human vs Computer selected from the UI") {
      val (io, selector) = SpecHelper.buildPlayerTypeSelector(selections)

      every { io.readLine() } returns "2"

      val result = selector.requestSelection()

      it("writes options to output") {
        verify { io.write(eq(requestSelectionText)) }
      }

      it("returns a Pair of (Human, Computer)") {
        expect(result).to.equal(humanVsComputer)
      }
    }

    context("Non integer selection from the UI") {
      val (io, selector) = SpecHelper.buildPlayerTypeSelector(selections)

      every { io.readLine() } answers {
        "blah"
      } andThen {
        "1"
      }

      val result = selector.requestSelection()

      it("returns a Pair of (Human, Human)") {
        expect(result).to.equal(humanVsHuman)
      }

      it("requests input twice, reporting incorrect input") {
        verifySequence {
          io.clearScreen()
          io.write(requestSelectionText)
          io.readLine()
          io.clearScreen()
          io.write(NonInteger.message + "\n" + requestSelectionText)
          io.readLine()
        }
      }
    }

    context("Out of bounds selection from the UI") {
      val (io, selector) = SpecHelper.buildPlayerTypeSelector(selections)

      every { io.readLine() } answers {
        "3"
      } andThen {
        "2"
      }

      val result = selector.requestSelection()

      it("returns a Pair of (Human, Computer)") {
        expect(result).to.equal(humanVsComputer)
      }

      it("requests input twice, reporting incorrect input") {
        verifySequence {
          io.clearScreen()
          io.write(requestSelectionText)
          io.readLine()
          io.clearScreen()
          io.write(SelectionOutOfBounds.message + "\n" + requestSelectionText)
          io.readLine()
        }
      }
    }
  }
})

object SpecHelper {
  fun buildPlayerTypeSelector(selections: List<PlayerTypes>): Pair<IO, PlayerTypeSelector> {
    val io = mockk<IO>()
    val selector = PlayerTypeSelector(io, *selections.toTypedArray())

    every { io.clearScreen() } just Runs
    every { io.write(any()) } just Runs

    return Pair(io, selector)
  }
}
