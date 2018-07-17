package com.capgemini.chess.algorithms.implementation;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.KingInCheckException;

import static com.capgemini.chess.algorithms.data.PredicateFactory.*;

public class MovementManager {

	public static Move validateKing(Coordinate from, Coordinate to, Board board)
			throws InvalidMoveException, KingInCheckException {
		Move movement = new Move();
		movement.setFrom(from);
		movement.setTo(to);

		// TODO sprawdzanie czy wykonywana jest roszada, wtedy max range krola
		// ma 2 pola
		// int maxRange = isKingCastling().test(from, board.getPieceAt(from)) ?
		// 2 : 1;
		if (isMovementTooLong(1).test(from, to)) {
			throw new InvalidMoveException();
		}

		if (isSpotEmpty().test(to, board)) {
			movement.setType(MoveType.ATTACK);
			movement.setMovedPiece(board.getPieceAt(from));
			return movement;
		} else if (isThisEnemyPiece().test(board.getPieceAt(from), board.getPieceAt(to))) {
			movement.setType(MoveType.CAPTURE);
			movement.setMovedPiece(board.getPieceAt(from));
			return movement;
		} else {
			System.out.println("Error");
			throw new InvalidMoveException();
		}
	}

	public static Move validateQueen(Coordinate from, Coordinate to, Board board) throws InvalidMoveException {
		Move movement = new Move();
		movement.setFrom(from);
		movement.setTo(to);

		if (isMovementStraight().test(from, to)) {
			// straight movement
			int deltaX = from.getX() - to.getX();
			int deltaY = from.getY() - to.getY();
			Coordinate spot = from;
			int xDirection = 0, yDirection = 0;
			if (deltaX == 0) {
				// vertical movement
				yDirection = (deltaY < 0) ? 1 : -1;
			} else {
				// horizontal movement
				xDirection = (deltaX < 0) ? 1 : -1;
			}

			for (int i = 0; i < Math.abs(deltaX + deltaY) - 1; i++)
			// sum because 1 of this is 0
			{
				spot = new Coordinate(spot.getX() + xDirection, spot.getY() + yDirection);
				if (!isSpotEmpty().test(spot, board)) {
					System.out.println("Something is stanting on the way!");
					throw new InvalidMoveException();
				}
			}
		} else if (isMovementDiagonal().test(from, to)) {
			// diagonal movement
			// is something blocking way to this spot?
			int deltaX = from.getX() - to.getX();
			int deltaY = from.getY() - to.getY();
			Coordinate spot = from;
			int xDirection = (deltaX < 0) ? 1 : -1;
			int yDirection = (deltaY < 0) ? 1 : -1;
			for (int i = 0; i < Math.abs(deltaX) - 1; i++) {
				spot = new Coordinate(spot.getX() + xDirection, spot.getY() + yDirection);
				if (!isSpotEmpty().test(spot, board)) {
					System.out.println("Something is stanting on the way!");
					throw new InvalidMoveException();
				}
			}
		} else {
			System.out.println("Error");
			throw new InvalidMoveException();
		}

		if (isSpotEmpty().test(to, board)) {
			movement.setType(MoveType.ATTACK);
			movement.setMovedPiece(board.getPieceAt(from));
			return movement;
		} else if (isThisEnemyPiece().test(board.getPieceAt(from), board.getPieceAt(to))) {
			movement.setType(MoveType.CAPTURE);
			movement.setMovedPiece(board.getPieceAt(from));
			return movement;
		}

		System.out.println("Error");
		throw new InvalidMoveException();
	}

	public static Move validateBishop(Coordinate from, Coordinate to, Board board) throws InvalidMoveException {
		Move movement = new Move();
		movement.setFrom(from);
		movement.setTo(to);
		if (!isMovementDiagonal().test(from, to)) {
			System.out.println("Movement is not diagonal!!");
			throw new InvalidMoveException();
		}

		// is something blocking way to this spot?
		int deltaX = from.getX() - to.getX();
		int deltaY = from.getY() - to.getY();
		Coordinate spot = from;
		int xDirection = (deltaX < 0) ? 1 : -1;
		int yDirection = (deltaY < 0) ? 1 : -1;
		for (int i = 0; i < Math.abs(deltaX) - 1; i++) {
			spot = new Coordinate(spot.getX() + xDirection, spot.getY() + yDirection);
			if (!isSpotEmpty().test(spot, board)) {
				System.out.println("Something is stanting on the way!");
				throw new InvalidMoveException();
			}
		}

		if (isSpotEmpty().test(to, board)) {
			movement.setType(MoveType.ATTACK);
			movement.setMovedPiece(board.getPieceAt(from));
			System.out.println("Im here");
			return movement;
		} else if (isThisEnemyPiece().test(board.getPieceAt(from), board.getPieceAt(to))) {

			movement.setType(MoveType.CAPTURE);
			movement.setMovedPiece(board.getPieceAt(from));
			return movement;
		}
		System.out.println("Error");
		throw new InvalidMoveException();
	}

	public static Move validateKnight(Coordinate from, Coordinate to, Board board) throws InvalidMoveException {
		Move movement = new Move();
		movement.setFrom(from);
		movement.setTo(to);
		if (!isMovementLShaped().test(from, to)) {
			System.out.println("Movement is not L shaped!");
			throw new InvalidMoveException();
		}
		if (isSpotEmpty().test(to, board)) {
			movement.setType(MoveType.ATTACK);
			movement.setMovedPiece(board.getPieceAt(from));
			return movement;
		} else if (isThisEnemyPiece().test(board.getPieceAt(from), board.getPieceAt(to))) {
			movement.setType(MoveType.CAPTURE);
			movement.setMovedPiece(board.getPieceAt(from));
			return movement;
		}
		System.out.println("My own piece at this spot!");
		throw new InvalidMoveException();
	}

	public static Move validateRook(Coordinate from, Coordinate to, Board board) throws InvalidMoveException {

		Move movement = new Move();
		movement.setFrom(from);
		movement.setTo(to);
		if (!isMovementStraight().test(from, to)) {
			System.out.println("Movement is not straight!");
			throw new InvalidMoveException();
		}

		// is something blocking way to this spot?
		int deltaX = from.getX() - to.getX();
		int deltaY = from.getY() - to.getY();
		Coordinate spot = from;
		int xDirection = 0, yDirection = 0;
		if (deltaX == 0) {
			// vertical movement
			yDirection = (deltaY < 0) ? 1 : -1;
		} else {
			// horizontal movement
			xDirection = (deltaX < 0) ? 1 : -1;
		}

		for (int i = 0; i < Math.abs(deltaX + deltaY) - 1; i++)
		// sum because 1 of this is 0
		{
			spot = new Coordinate(spot.getX() + xDirection, spot.getY() + yDirection);
			if (!isSpotEmpty().test(spot, board)) {
				System.out.println("Something is stanting on the way!");
				throw new InvalidMoveException();
			}
		}

		if (isSpotEmpty().test(to, board)) {
			movement.setType(MoveType.ATTACK);
			movement.setMovedPiece(board.getPieceAt(from));
			System.out.println("Im here");
			return movement;
		} else if (isThisEnemyPiece().test(board.getPieceAt(from), board.getPieceAt(to))) {
			movement.setType(MoveType.CAPTURE);
			movement.setMovedPiece(board.getPieceAt(from));
			return movement;
		}

		System.out.println("My own piece at this spot!");
		throw new InvalidMoveException();
	}

	public static Move validatePawn(Coordinate from, Coordinate to, Board board) throws InvalidMoveException {

		Move movement = new Move();
		movement.setFrom(from);
		movement.setTo(to);

		isPawnMovingForward(from, to, board);

		if (isMovementStraight().test(from, to)) {

			// first move - range 2, next moves - range 1
			int maxRange = pawnFirstMove().test(from, board.getPieceAt(from)) ? 2 : 1;
			checkRange(maxRange, from, to);

			checkPawnSpots(from, to, board);

			movement.setType(MoveType.ATTACK);
			movement.setMovedPiece(board.getPieceAt(from));
			return movement;

		} else if (isMovementDiagonal().test(from, to)) {
			System.out.println("Movement Diagonal");

			if (isMovementTooLong(1).test(from, to)) {
				System.out.println("Too Long Movement!");
				throw new InvalidMoveException();
			}
			if (isSpotEmpty().test(to, board)) {
				System.out.println("Spot is empty");
				throw new InvalidMoveException();
			}

			if (!isThisEnemyPiece().test(board.getPieceAt(from), board.getPieceAt(to))) {
				System.out.println("This is not enemy piece!");
				throw new InvalidMoveException();
			}

			movement.setType(MoveType.CAPTURE);
			movement.setMovedPiece(board.getPieceAt(from));
			return movement;
		}

		throw new InvalidMoveException();
	}

	private static void checkRange(int maxRange, Coordinate from, Coordinate to) throws InvalidMoveException {
		if (isMovementTooLong(maxRange).test(from, to)) {
			throw new InvalidMoveException();
		}
	}

	private static void checkPawnSpots(Coordinate from, Coordinate to, Board board) throws InvalidMoveException {
		int movementDirection = (board.getPieceAt(from).getColor() == Color.BLACK) ? -1 : 1;
		for (int i = from.getY();; i += movementDirection) {
			if (i == to.getY())
				break;
			Coordinate spot = new Coordinate(from.getX(), i + movementDirection);
			if (!isSpotEmpty().test(spot, board)) {
				throw new InvalidMoveException();
			}
		}
	}

	private static void isPawnMovingForward(Coordinate from, Coordinate to, Board board) throws InvalidMoveException {
		boolean isBlack = (board.getPieceAt(from).getColor() == Color.BLACK) ? true : false;
		if ((!isBlack && !isWhiteForwardMovement().test(from, to))
				|| (isBlack && !isBlackForwardMovement().test(from, to))) {
			System.out.println("White is not moving forward");
			throw new InvalidMoveException();
		}
	}
}
