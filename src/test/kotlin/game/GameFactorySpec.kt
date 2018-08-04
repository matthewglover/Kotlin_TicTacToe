package game

import com.winterbe.expekt.expect
import core.Board
import core.Mark
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object GameFactorySpec : Spek({
  describe("from") {
    it("returns a game") {
      val ui = UI(MockIO())
      val game = GameFactory.from(ui)

      expect(game).to.equal(Game(Player(ui, Mark.ONE), Player(ui, Mark.TWO), Board()))
    }
  }
})

