package game

import com.winterbe.expekt.expect
import core.Board
import core.Mark
import gameOptions.PlayerType
import io.mockk.mockk
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object GameFactorySpec : Spek({
  describe("from") {
    it("returns a game") {
      val ui = UI(mockk())
      val game = GameFactory.from(ui, Pair(PlayerType.HUMAN, PlayerType.HUMAN))

      expect(game).to.equal(Game(HumanPlayer(ui, Mark.ONE), HumanPlayer(ui, Mark.TWO), Board()))
    }
  }
})

