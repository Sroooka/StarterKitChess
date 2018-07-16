package com.capgemini.chess.algorithms.implementation;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.KingInCheckException;

import static junit.framework.Assert.*;

@SuppressWarnings("deprecation")
public class MovementManagerTest {

	////////////////////////////
	///// WHITE PAWN TESTS /////
	////////////////////////////
	@Test
	public void moveWhitePawnFromStartingPositionToFrontTwoSpots() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();

		Coordinate from = new Coordinate(5, 1);
		Coordinate to = new Coordinate(5, 3);

		Move move = MovementManager.validateWhitePawn(from, to, board);
		assertEquals(move.getFrom().getX(), from.getX());
		assertEquals(move.getFrom().getY(), from.getY());
		assertEquals(move.getTo().getX(), to.getX());
		assertEquals(move.getTo().getY(), to.getY());
	}

	@Test
	public void moveWhitePawnFromStartingPositionToFrontOneSpot() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();

		Coordinate from = new Coordinate(5, 1);
		Coordinate to = new Coordinate(5, 2);

		Move move = MovementManager.validateWhitePawn(from, to, board);
		assertEquals(move.getFrom().getX(), from.getX());
		assertEquals(move.getFrom().getY(), from.getY());
		assertEquals(move.getTo().getX(), to.getX());
		assertEquals(move.getTo().getY(), to.getY());
	}

	@Test
	public void moveWhitePawnFromStartingPositionToFrontThreeSpots() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();

		Coordinate from = new Coordinate(5, 1);
		Coordinate to = new Coordinate(5, 4);

		boolean exceptionThrown = false;
		try {
			MovementManager.validateWhitePawn(from, to, board);
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		assertTrue(exceptionThrown);
	}

	@Test
	public void moveWhitePawnFromStartingPositionToFrontFourSpots() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();

		Coordinate from = new Coordinate(5, 1);
		Coordinate to = new Coordinate(5, 5);

		boolean exceptionThrown = false;
		try {
			MovementManager.validateWhitePawn(from, to, board);
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		assertTrue(exceptionThrown);
	}

	@Test
	public void moveWhitePawnFromStartingPositionTwoSpotsNotDiagonalAndNotStraight()
			throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();

		Coordinate from = new Coordinate(5, 1);
		Coordinate to = new Coordinate(6, 3);

		boolean exceptionThrown = false;
		try {
			MovementManager.validateWhitePawn(from, to, board);
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		assertTrue(exceptionThrown);
	}

	@Test
	public void moveWhitePawnFromStartingPositionBackOneSpot() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();

		Coordinate from = new Coordinate(5, 1);
		Coordinate to = new Coordinate(5, 0);

		boolean exceptionThrown = false;
		try {
			MovementManager.validateWhitePawn(from, to, board);
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		assertTrue(exceptionThrown);
	}

	@Test
	public void moveWhitePawnFromStartingPositionTwoSpotsForwardWhenOtherPieceIsBetweenMove()
			throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();
		board.setPieceAt(Piece.BLACK_QUEEN, new Coordinate(5, 2));
		Coordinate from = new Coordinate(5, 1);
		Coordinate to = new Coordinate(5, 3);

		boolean exceptionThrown = false;
		try {
			MovementManager.validateWhitePawn(from, to, board);
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		assertTrue(exceptionThrown);
	}

	@Test
	public void moveWhitePawnFromStartingPositionTwoSpotsForwardWhenOtherPieceIsOnSpot()
			throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(5, 3));
		Coordinate from = new Coordinate(5, 1);
		Coordinate to = new Coordinate(5, 3);

		boolean exceptionThrown = false;
		try {
			MovementManager.validateWhitePawn(from, to, board);
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		assertTrue(exceptionThrown);
	}

	@Test
	public void captureBlackPawnByWhitePawnOnLeft() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(4, 2));
		Coordinate from = new Coordinate(5, 1);
		Coordinate to = new Coordinate(4, 2);

		Move move = MovementManager.validateWhitePawn(from, to, board);
		assertEquals(move.getFrom().getX(), from.getX());
		assertEquals(move.getFrom().getY(), from.getY());
		assertEquals(move.getTo().getX(), to.getX());
		assertEquals(move.getTo().getY(), to.getY());
		assertEquals(move.getType(), MoveType.CAPTURE);
	}

	@Test
	public void captureBlackPawnByWhitePawnOnRight() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(6, 2));
		Coordinate from = new Coordinate(5, 1);
		Coordinate to = new Coordinate(6, 2);

		Move move = MovementManager.validateWhitePawn(from, to, board);
		assertEquals(move.getFrom().getX(), from.getX());
		assertEquals(move.getFrom().getY(), from.getY());
		assertEquals(move.getTo().getX(), to.getX());
		assertEquals(move.getTo().getY(), to.getY());
		assertEquals(move.getType(), MoveType.CAPTURE);
	}

	@Test
	public void captureBlackPawnByWhitePawnOnRightTwoSpotsAway() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();
		board.setPieceAt(Piece.BLACK_PAWN, new Coordinate(7, 3));
		Coordinate from = new Coordinate(5, 1);
		Coordinate to = new Coordinate(7, 3);

		boolean exceptionThrown = false;
		try {
			MovementManager.validateWhitePawn(from, to, board);
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		assertTrue(exceptionThrown);
	}

	////////////////////////////
	///// BLACK PAWN TESTS /////
	////////////////////////////
	@Test
	public void moveBlackPawnFromStartingPositionToFrontTwoSpots() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();

		Coordinate from = new Coordinate(2, 6);
		Coordinate to = new Coordinate(2, 4);

		Move move = MovementManager.validateBlackPawn(from, to, board);
		assertEquals(move.getFrom().getX(), from.getX());
		assertEquals(move.getFrom().getY(), from.getY());
		assertEquals(move.getTo().getX(), to.getX());
		assertEquals(move.getTo().getY(), to.getY());
	}

	@Test
	public void moveBlackPawnFromStartingPositionToFrontOneSpot() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();

		Coordinate from = new Coordinate(2, 6);
		Coordinate to = new Coordinate(2, 5);

		Move move = MovementManager.validateBlackPawn(from, to, board);
		assertEquals(move.getFrom().getX(), from.getX());
		assertEquals(move.getFrom().getY(), from.getY());
		assertEquals(move.getTo().getX(), to.getX());
		assertEquals(move.getTo().getY(), to.getY());
	}

	@Test
	public void moveBlackPawnFromStartingPositionToFrontThreeSpots() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();

		Coordinate from = new Coordinate(2, 6);
		Coordinate to = new Coordinate(2, 3);

		boolean exceptionThrown = false;
		try {
			MovementManager.validateBlackPawn(from, to, board);
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		assertTrue(exceptionThrown);
	}

	@Test
	public void moveBlackPawnFromStartingPositionToFrontFourSpots() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();

		Coordinate from = new Coordinate(4, 6);
		Coordinate to = new Coordinate(4, 2);

		boolean exceptionThrown = false;
		try {
			MovementManager.validateBlackPawn(from, to, board);
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		assertTrue(exceptionThrown);
	}

	@Test
	public void moveBlackPawnFromStartingPositionTwoSpotsNotDiagonalAndNotStraight()
			throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();

		Coordinate from = new Coordinate(2, 6);
		Coordinate to = new Coordinate(1, 4);

		boolean exceptionThrown = false;
		try {
			MovementManager.validateBlackPawn(from, to, board);
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		assertTrue(exceptionThrown);
	}

	@Test
	public void moveBlackPawnFromStartingPositionBackOneSpot() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();

		Coordinate from = new Coordinate(4, 6);
		Coordinate to = new Coordinate(4, 7);

		boolean exceptionThrown = false;
		try {
			MovementManager.validateBlackPawn(from, to, board);
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		assertTrue(exceptionThrown);
	}

	@Test
	public void moveBlackPawnFromStartingPositionTwoSpotsForwardWhenOtherPieceIsBetweenMove()
			throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();
		board.setPieceAt(Piece.WHITE_QUEEN, new Coordinate(2, 5));
		Coordinate from = new Coordinate(2, 6);
		Coordinate to = new Coordinate(2, 4);

		boolean exceptionThrown = false;
		try {
			MovementManager.validateBlackPawn(from, to, board);
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		assertTrue(exceptionThrown);
	}

	@Test
	public void moveBlackPawnFromStartingPositionTwoSpotsForwardWhenOtherPieceIsOnSpot()
			throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();
		board.setPieceAt(Piece.WHITE_QUEEN, new Coordinate(2, 4));
		Coordinate from = new Coordinate(2, 6);
		Coordinate to = new Coordinate(2, 4);

		boolean exceptionThrown = false;
		try {
			MovementManager.validateBlackPawn(from, to, board);
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		assertTrue(exceptionThrown);
	}

	@Test
	public void captureWhitePawnByBlackPawnOnLeft() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(1, 5));
		Coordinate from = new Coordinate(2, 6);
		Coordinate to = new Coordinate(1, 5);

		Move move = MovementManager.validateBlackPawn(from, to, board);
		assertEquals(move.getFrom().getX(), from.getX());
		assertEquals(move.getFrom().getY(), from.getY());
		assertEquals(move.getTo().getX(), to.getX());
		assertEquals(move.getTo().getY(), to.getY());
		assertEquals(move.getType(), MoveType.CAPTURE);
	}

	@Test
	public void captureWhitePawnByBlackPawnOnRight() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();
		board.setPieceAt(Piece.WHITE_QUEEN, new Coordinate(3, 5));
		Coordinate from = new Coordinate(2, 6);
		Coordinate to = new Coordinate(3, 5);

		Move move = MovementManager.validateBlackPawn(from, to, board);
		assertEquals(move.getFrom().getX(), from.getX());
		assertEquals(move.getFrom().getY(), from.getY());
		assertEquals(move.getTo().getX(), to.getX());
		assertEquals(move.getTo().getY(), to.getY());
		assertEquals(move.getType(), MoveType.CAPTURE);
	}

	@Test
	public void captureWhitePawnByBlackPawnOnRightTwoSpotsAway() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();
		board.setPieceAt(Piece.WHITE_PAWN, new Coordinate(4, 4));
		Coordinate from = new Coordinate(2, 6);
		Coordinate to = new Coordinate(4, 4);

		boolean exceptionThrown = false;
		try {
			MovementManager.validateBlackPawn(from, to, board);
		} catch (InvalidMoveException e) {
			exceptionThrown = true;
		}

		assertTrue(exceptionThrown);
	}

	////////////////////////////////////
	///// WHITE/BLACK KNIGHT TESTS /////
	////////////////////////////////////
	@Test
	public void shouldKnightMoveRight1() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();
		board.setPieceAt(Piece.BLACK_KNIGHT, new Coordinate(3, 3));
		Coordinate from = new Coordinate(3, 3);
		Coordinate to = new Coordinate(1, 2);

		Move move = MovementManager.validateBlackKnight(from, to, board);
		assertEquals(move.getFrom().getX(), from.getX());
		assertEquals(move.getFrom().getY(), from.getY());
		assertEquals(move.getTo().getX(), to.getX());
		assertEquals(move.getTo().getY(), to.getY());
		assertEquals(move.getType(), MoveType.ATTACK);
	}
	
	@Test
	public void shouldKnightMoveRight2() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();
		board.setPieceAt(null, new Coordinate(2, 1));
		board.setPieceAt(Piece.BLACK_KNIGHT, new Coordinate(3, 3));
		Coordinate from = new Coordinate(3, 3);
		Coordinate to = new Coordinate(2, 1);

		Move move = MovementManager.validateBlackKnight(from, to, board);
		assertEquals(move.getFrom().getX(), from.getX());
		assertEquals(move.getFrom().getY(), from.getY());
		assertEquals(move.getTo().getX(), to.getX());
		assertEquals(move.getTo().getY(), to.getY());
		assertEquals(move.getType(), MoveType.ATTACK);
	}
	
	@Test
	public void shouldKnightMoveRight3() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();
		board.setPieceAt(null, new Coordinate(4, 1));
		board.setPieceAt(Piece.BLACK_KNIGHT, new Coordinate(3, 3));
		Coordinate from = new Coordinate(3, 3);
		Coordinate to = new Coordinate(4, 1);

		Move move = MovementManager.validateBlackKnight(from, to, board);
		assertEquals(move.getFrom().getX(), from.getX());
		assertEquals(move.getFrom().getY(), from.getY());
		assertEquals(move.getTo().getX(), to.getX());
		assertEquals(move.getTo().getY(), to.getY());
		assertEquals(move.getType(), MoveType.ATTACK);
	}
	
	@Test
	public void shouldKnightMoveRight4() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();
		board.setPieceAt(Piece.BLACK_KNIGHT, new Coordinate(3, 3));
		Coordinate from = new Coordinate(3, 3);
		Coordinate to = new Coordinate(5, 2);

		Move move = MovementManager.validateBlackKnight(from, to, board);
		assertEquals(move.getFrom().getX(), from.getX());
		assertEquals(move.getFrom().getY(), from.getY());
		assertEquals(move.getTo().getX(), to.getX());
		assertEquals(move.getTo().getY(), to.getY());
		assertEquals(move.getType(), MoveType.ATTACK);
	}
	
	@Test
	public void shouldKnightMoveRight5() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();
		board.setPieceAt(Piece.BLACK_KNIGHT, new Coordinate(3, 3));
		Coordinate from = new Coordinate(3, 3);
		Coordinate to = new Coordinate(5, 4);

		Move move = MovementManager.validateBlackKnight(from, to, board);
		assertEquals(move.getFrom().getX(), from.getX());
		assertEquals(move.getFrom().getY(), from.getY());
		assertEquals(move.getTo().getX(), to.getX());
		assertEquals(move.getTo().getY(), to.getY());
		assertEquals(move.getType(), MoveType.ATTACK);
	}
	
	@Test
	public void shouldKnightMoveRight6() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();
		board.setPieceAt(Piece.BLACK_KNIGHT, new Coordinate(3, 3));
		Coordinate from = new Coordinate(3, 3);
		Coordinate to = new Coordinate(4, 5);

		Move move = MovementManager.validateBlackKnight(from, to, board);
		assertEquals(move.getFrom().getX(), from.getX());
		assertEquals(move.getFrom().getY(), from.getY());
		assertEquals(move.getTo().getX(), to.getX());
		assertEquals(move.getTo().getY(), to.getY());
		assertEquals(move.getType(), MoveType.ATTACK);
	}
	
	@Test
	public void shouldKnightMoveRight7() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();
		board.setPieceAt(Piece.BLACK_KNIGHT, new Coordinate(3, 3));
		Coordinate from = new Coordinate(3, 3);
		Coordinate to = new Coordinate(2, 5);

		Move move = MovementManager.validateBlackKnight(from, to, board);
		assertEquals(move.getFrom().getX(), from.getX());
		assertEquals(move.getFrom().getY(), from.getY());
		assertEquals(move.getTo().getX(), to.getX());
		assertEquals(move.getTo().getY(), to.getY());
		assertEquals(move.getType(), MoveType.ATTACK);
	}
	
	@Test
	public void shouldKnightMoveRight8() throws KingInCheckException, InvalidMoveException {
		BoardManager boardManager = new BoardManager();
		Board board = boardManager.getBoard();
		board.setPieceAt(Piece.BLACK_KNIGHT, new Coordinate(3, 3));
		Coordinate from = new Coordinate(3, 3);
		Coordinate to = new Coordinate(1, 4);

		Move move = MovementManager.validateBlackKnight(from, to, board);
		assertEquals(move.getFrom().getX(), from.getX());
		assertEquals(move.getFrom().getY(), from.getY());
		assertEquals(move.getTo().getX(), to.getX());
		assertEquals(move.getTo().getY(), to.getY());
		assertEquals(move.getType(), MoveType.ATTACK);
	}
}
