package com.capgemini.chess.algorithms.implementation;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.KingInCheckException;

import static com.capgemini.chess.algorithms.data.PredicateFactory.*;

public class MovementManager {
	public static Move validateWhiteKing(Coordinate from, Coordinate to, Board board)
			throws InvalidMoveException, KingInCheckException {

		return null;
	}

	public static Move validateWhiteQueen(Coordinate from, Coordinate to, Board board) {

		return null;
	}

	public static Move validateWhiteBishop(Coordinate from, Coordinate to, Board board) {

		return null;
	}

	public static Move validateWhiteKnight(Coordinate from, Coordinate to, Board board) {

		return null;
	}

	public static Move validateWhiteRook(Coordinate from, Coordinate to, Board board) {

		return null;
	}

	public static Move validateWhitePawn(Coordinate from, Coordinate to, Board board) throws InvalidMoveException {


		Move movement = new Move();
		movement.setFrom(from);
		movement.setTo(to);

		int maxRange = pawnFirstMove().test(from, board.getPieceAt(from)) ? 2 : 1;
		if (isMovementToLong(maxRange).test(from, to)) {
			throw new InvalidMoveException();
		}

		if (!isWhiteForwardMovement().test(from, to)) {
			throw new InvalidMoveException();
		}

		if (isMovementStraight().test(from, to)) {
			movement.setType(MoveType.ATTACK);
			
			for(int i = from.getY(); i<to.getY(); ++i){
				Coordinate spot = new Coordinate(from.getX(), i);
				if(!isSpotEmpty().test(spot, board)){
					throw new InvalidMoveException();
				}
			}
		} else if (isMovementDiagonal().test(from, to)) {

		}

		return null;
	}

	public static Move validateBlackKing(Coordinate from, Coordinate to, Board board) {

		return null;
	}

	public static Move validateBlackQueen(Coordinate from, Coordinate to, Board board) {

		return null;
	}

	public static Move validateBlackBishop(Coordinate from, Coordinate to, Board board) {

		return null;
	}

	public static Move validateBlackKnight(Coordinate from, Coordinate to, Board board) {

		return null;
	}

	public static Move validateBlackRook(Coordinate from, Coordinate to, Board board) {

		return null;
	}

	public static Move validateBlackPawn(Coordinate from, Coordinate to, Board board) {

		return null;
	}
}
