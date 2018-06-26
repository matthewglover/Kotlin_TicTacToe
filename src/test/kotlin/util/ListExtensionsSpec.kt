package util

import com.winterbe.expekt.expect
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object ListExtensionsSpec : Spek({
  describe("intersperse") {
    it("intersperses the received argument into the receiver list") {
      val receiver = listOf(1, 3, 5)

      expect(receiver.intersperse(10)).to.equal(
          listOf(1, 10, 3, 10, 5)
      )
    }
  }
})