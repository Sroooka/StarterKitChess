package com.capgemini.chess.algorithms.data;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.enums.PieceType;
import com.capgemini.chess.algorithms.data.generated.Board;

public class PredicateFactory {

	public static BiPredicate<Coordinate, Coordinate> pieceOutOfBoard() {
		return (from, to) -> from.getX() > 7 || from.getY() > 7 || from.getX() < 0 || from.getY() < 0 || to.getX() > 7
				|| to.getY() > 7 || to.getX() < 0 || to.getY() < 0;
	}

	public static Predicate<Coordinate> singlePieceOutOfBoard() {
		return (spot) -> spot.getX() > 7 || spot.getY() > 7 || spot.getX() < 0 || spot.getY() < 0;
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

		return (from, to) -> (from.getX() == to.getX()) && (from.getY() != to.getY())
				|| (from.getX() != to.getX()) && (from.getY() == to.getY());
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

	public static Predicate<Piece> pieceIsKing() {
		return (p) -> p.getType() == PieceType.KING;
	}

	public static BiPredicate<Coordinate, Board> isThisMyKing(Color myColor) {
		return (spot, board) -> board.getPieceAt(spot) != null && board.getPieceAt(spot).getType() == PieceType.KING
				&& board.getPieceAt(spot).getColor() == myColor;
	}

	public static BiPredicate<Coordinate, Board> movingMyOwnFigure(Color playerColor) {
		return (from, board) -> board.getPieceAt(from) != null && board.getPieceAt(from).getColor() == playerColor;
	}

	public static BiPredicate<Coordinate, Move> checkEnPassant() {
		return (spot,
				move) -> (move.getMovedPiece().getColor() == Color.WHITE && move.getFrom().getX() == spot.getX()
						&& move.getTo().getX() == spot.getX() && move.getFrom().getY() == (spot.getY() - 1)
						&& move.getTo().getY() == (spot.getY() + 1))
						|| (move.getMovedPiece().getColor() == Color.BLACK && move.getFrom().getX() == spot.getX()
								&& move.getTo().getX() == spot.getX() && move.getFrom().getY() == (spot.getY() + 1)
								&& move.getTo().getY() == (spot.getY() - 1));
	}

	public static BiPredicate<Move, Piece> doesMovementContainPiece() {
		return (move, piece) -> move.getMovedPiece().getType() == piece.getType()
				&& move.getMovedPiece().getColor() == piece.getColor();
	}
}
