package com.capgemini.chess.algorithms.data;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;

public class PredicateFactory {

	public static BiPredicate<Coordinate, Coordinate> pieceOutOfBoard() {
		return (from, to) -> from.getX() > 7 || from.getY() > 7 || from.getX() < 0 || from.getY() < 0 || to.getX() > 7
				|| to.getY() > 7 || to.getX() < 0 || to.getY() < 0;
	}

	public static BiPredicate<Coordinate, Piece> pawnFirstMove() {
		return (c, p) -> (p.getColor() == Color.BLACK && c.getY() == 6)
				|| (p.getColor() == Color.WHITE && c.getY() == 1);
	}

	public static BiPredicate<Coordinate, Coordinate> isMovementTooLong(int maxRange) {

		return (from, to) -> (Math.abs(from.getX() - to.getX()) > maxRange)
				|| (Math.abs(from.getY() - to.getY()) > maxRange);
	}

	public static BiPredicate<Coordinate, Coordinate> isMovementStraight() {

		return (from, to) -> (from.getX() == to.getX()) && (from.getY() != to.getY()) ||
				(from.getX() != to.getX()) && (from.getY() == to.getY());
	}

	public static BiPredicate<Coordinate, Coordinate> isMovementDiagonal() {

		return (from, to) -> (Math.abs(from.getX() - to.getX())) == (Math.abs(from.getY() - to.getY()));
	}

	public static BiPredicate<Coordinate, Coordinate> isWhiteForwardMovement() {

		return (from, to) -> from.getY() < to.getY();
	}
	
	public static BiPredicate<Coordinate, Coordinate> isBlackForwardMovement() {

		return (from, to) -> from.getY() > to.getY();
	}

	public static BiPredicate<Coordinate, Board> isSpotEmpty() {
		return (spot, board) -> board.getPieceAt(spot) == null;
	}
	
	public static BiPredicate<Piece, Piece> isThisEnemyPiece() {

		return (pieceFrom, pieceTo) -> pieceFrom.getColor() != pieceTo.getColor();
	}
	

	public static BiPredicate<Coordinate, Coordinate> isMovementLShaped() {

		return (from, to) -> (Math.abs(from.getX() - to.getX()) == 2) && (Math.abs(from.getY() - to.getY()) == 1)
				|| (Math.abs(from.getX() - to.getX()) == 1) && (Math.abs(from.getY() - to.getY()) == 2);
	}
	
	public static BiPredicate<Coordinate, Coordinate> theSameCoordinates() {

		return (from, to) -> from.getX() == to.getX() && from.getY() == to.getY();
	}
}
