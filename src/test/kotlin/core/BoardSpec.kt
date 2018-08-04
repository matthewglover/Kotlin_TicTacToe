package core

import arrow.core.Either
import com.winterbe.expekt.expect
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object BoardSpec : Spek({
  describe("Empty board") {
    val board = BoardStates.EMPTY

    it("is not complete") {
      expect(board.isComplete).to.be.`false`
    }

    it("does not have a winner") {
      expect(board.isWinner).to.be.`false`
      expect(board.winner).to.be.`null`
    }

    it("Mark.ONE is the current mark") {
      expect(board.isCurrentMark(Mark.ONE)).to.be.`true`
      expect(board.isCurrentMark(Mark.TWO)).to.be.`false`
    }

    it("has all empty tiles") {
      (1..9).forEach { expect(board.tile(it)).to.equal(FreeTile(it)) }
    }

    it("has no last mark") {
      expect(board.wasLastMark(Mark.ONE)).to.be.`false`
      expect(board.wasLastMark(Mark.TWO)).to.be.`false`
    }
  }

  describe("Board after first move") {
    val board = BoardStates.runMoves(BoardStates.EMPTY, Move(1, Mark.ONE))

    it("is not complete") {
      expect(board.isComplete).to.be.`false`
    }

    it("does not have a winner") {
      expect(board.isWinner).to.be.`false`
      expect(board.winner).to.be.`null`
    }

    it("Mark.TWO is the current mark") {
      expect(board.isCurrentMark(Mark.TWO)).to.be.`true`
      expect(board.isCurrentMark(Mark.ONE)).to.be.`false`
    }

    it("has tile marked for Mark.ONE") {
      expect(board.tile(1)).to.equal(Move(1, Mark.ONE))
    }

    it("has last mark of Mark.ONE") {
      expect(board.wasLastMark(Mark.ONE)).to.be.`true`
      expect(board.wasLastMark(Mark.TWO)).to.be.`false`
    }
  }

  describe("Drawn board") {
    val board = BoardStates.COMPLETE


    it("is complete") {
      expect(board.isComplete).to.be.`true`
    }

    it("does not have a winner") {
      expect(board.isWinner).to.be.`false`
      expect(board.winner).to.be.`null`
    }

    it("does not have a current mark") {
      expect(board.isCurrentMark(Mark.TWO)).to.be.`false`
      expect(board.isCurrentMark(Mark.ONE)).to.be.`false`
    }

    it("has last mark of Mark.ONE") {
      expect(board.wasLastMark(Mark.ONE)).to.be.`true`
      expect(board.wasLastMark(Mark.TWO)).to.be.`false`
    }
  }

  describe("Mark.ONE Winning board") {
    listOf(BoardStates.MARK_ONE_WINNING_ROW, BoardStates.MARK_ONE_WINNING_COL)
        .forEach { board ->
          it("is complete") {
            expect(board.isComplete).to.be.`true`
          }

          it("has a winner of Mark.ONE") {
            expect(board.isWinner).to.be.`true`
            expect(board.winner).to.equal(Mark.ONE)
          }

          it("does not have a current mark") {
            expect(board.isCurrentMark(Mark.TWO)).to.be.`false`
            expect(board.isCurrentMark(Mark.ONE)).to.be.`false`
          }

          it("has last mark of Mark.ONE") {
            expect(board.wasLastMark(Mark.ONE)).to.be.`true`
            expect(board.wasLastMark(Mark.TWO)).to.be.`false`
          }
        }
  }

  describe("Mark.TWO Winning board") {
    listOf(BoardStates.MARK_TWO_WINNING_ROW, BoardStates.MARK_TWO_WINNING_COL)
        .forEach { board ->
          it("is complete") {
            expect(board.isComplete).to.be.`true`
          }

          it("has a winner of Mark.TWO") {
            expect(board.isWinner).to.be.`true`
            expect(board.winner).to.equal(Mark.TWO)
          }

          it("does not have a current mark") {
            expect(board.isCurrentMark(Mark.TWO)).to.be.`false`
            expect(board.isCurrentMark(Mark.ONE)).to.be.`false`
          }

          it("has last mark of Mark.TWO") {
            expect(board.wasLastMark(Mark.TWO)).to.be.`true`
            expect(board.wasLastMark(Mark.ONE)).to.be.`false`
          }
        }
  }

  describe("Moving") {
    val board = BoardStates.runMoves(BoardStates.EMPTY, Move(1, Mark.ONE))

    context("when move is out of turn") {
      it("returns a Left of MoveOutOfTurn") {
        val result = board.make(Move(2, Mark.ONE))
        expect(result).to.equal(Either.Left(MoveOutOfTurn))
      }
    }

    context("when move is taken") {
      it("returns a Left of MoveTaken") {
        val result = board.make(Move(1, Mark.TWO))
        expect(result).to.equal(Either.Left(MoveTaken))
      }
    }

    context("when move is not on the board") {
      it("returns a Left of MoveOutOfBounds") {
        val result = board.make(Move(10, Mark.TWO))
        expect(result).to.equal(Either.Left(MoveOutOfBounds))
      }
    }

    context("when move is valid") {
      it("returns a Right of updated Board") {
        val result = board.make(Move(2, Mark.TWO))
        val updatedBoard = BoardStates.runMoves(board, Move(2, Mark.TWO))

        expect(result).to.equal(Either.Right(updatedBoard))
      }
    }
  }
})
