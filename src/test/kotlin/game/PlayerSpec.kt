package game

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import ui.UI

object PlayerSpec : Spek({
  describe("requestMove") {
    it("gets mark's move from the UI") {
      val ui = mockk<UI>()
      val player = Player(ui)
      val board = Board()
      every { ui.requestMove(any(), any()) } returns Move(1, Mark.ONE)
      player.requestMove(board, Mark.ONE)
      verify(exactly = 1) { ui.requestMove(board, Mark.ONE) }
    }
  }
})