package ui

import arrow.core.Either
import com.winterbe.expekt.expect
import game.Board
import game.Move
import game.Mark
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object MoveParserSpec : Spek({
  describe("MoveInputValidator") {
    context("with free square") {
      it("returns Right of move") {
        val parser = MoveParser(Board(), Mark.ONE)

        expect(parser.parse("1")).to.equal(Either.Right(Move(1, Mark.ONE)))
      }
    }

    context("with non-integer input") {
      it("returns Left of non-integer invalid input") {
        val parser = MoveParser(Board(), Mark.ONE)
        
        expect(parser.parse("blah")).to.equal(Either.Left(InvalidInput.NON_INTEGER))
      }
    }

    context("with null input") {
      it("returns Left of non-integer invalid input") {
        val parser = MoveParser(Board(), Mark.ONE)

        expect(parser.parse(null)).to.equal(Either.Left(InvalidInput.NON_INTEGER))
      }
    }

    context("with taken square") {
      it("returns Left of move-taken invalid input") {
        val board = Board().make(Move(1, Mark.ONE))
        val parser = MoveParser(board, Mark.TWO)

        expect(parser.parse("1")).to.equal(Either.Left(InvalidInput.MOVE_TAKEN))
      }
    }
  }
})