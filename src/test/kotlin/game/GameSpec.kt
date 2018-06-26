package game

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import ui.UI

object GameSpec : Spek({
  describe("next") {
    it("getsMove from player with current mark and returns next game") {
      val ui = mockk<UI>()
      val board = Board()
      val player = Player(ui)
      val game = Game(player, player, board)
      val slot = slot<Board>()

      every { ui.requestMove(board, Mark.ONE) } returns Move(1, Mark.ONE)
      every { ui.requestMove(capture(slot), Mark.TWO) } returns Move(2, Mark.TWO)

      game.next().next()

      verify(exactly = 1) { ui.requestMove(board, Mark.ONE) }
      verify(exactly = 1) { ui.requestMove(slot.captured, Mark.TWO) }
    }
  }
})