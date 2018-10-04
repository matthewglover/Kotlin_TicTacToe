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
      (1..9).forEach { expect(board.tileAt(it)).to.equal(Tile.Free(it)) }
      expect(board.freeTileNumbers).to.equal((1..9).toList())
    }

    it("has no last mark") {
      expect(board.wasLastMark(Mark.ONE)).to.be.`false`
      expect(board.wasLastMark(Mark.TWO)).to.be.`false`
    }
  }

  describe("Board after first move") {
    val board = BoardStates.takeTiles(BoardStates.EMPTY, 1)

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

    it("has tileAt 1 marked for Mark.ONE") {
      expect(board.tileAt(1)).to.equal(Tile.Taken(1, Mark.ONE))
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

    it("has no empty tiles") {
      (1..9).forEach { expect(board.tileAt(it)).not.to.equal(Tile.Free(it)) }
      expect(board.freeTileNumbers).to.equal(emptyList())
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
    val board = BoardStates.takeTiles(BoardStates.EMPTY, 1)

    context("when move is taken") {
      it("returns a Left of MoveTaken") {
        val result = board.takeTile(1)
        expect(result).to.equal(Either.Left(MoveTaken))
      }
    }

    context("when move is not on the board") {
      listOf(0, 10).forEach { tileNumber ->
        it("returns a Left of MoveOutOfBounds") {
          val result = board.takeTile(tileNumber)
          expect(result).to.equal(Either.Left(MoveOutOfBounds))
        }
      }
    }

    context("when move is valid") {
      it("returns a Right of updated Board") {
        val result = board.takeTile(2)
        val updatedBoard = BoardStates.takeTiles(board, 2)

        expect(result).to.equal(Either.Right(updatedBoard))
      }
    }
  }
})
