package com.capgemini.chess.algorithms.implementation;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.enums.PieceType;
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

	public static Move validateWhiteKnight(Coordinate from, Coordinate to, Board board) throws InvalidMoveException {
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

	public static Move validateWhiteRook(Coordinate from, Coordinate to, Board board) {

		return null;
	}

	public static Move validateWhitePawn(Coordinate from, Coordinate to, Board board) throws InvalidMoveException {

		Move movement = new Move();
		movement.setFrom(from);
		movement.setTo(to);

		System.out.println("Im gonna check !isWhiteForwardMovement");

		if (!isWhiteForwardMovement().test(from, to)) {
			System.out.println("White is not moving forward");
			throw new InvalidMoveException();
		}

		System.out.println("Im gonna check isMovementStraight and isMovementDiagonal");

		if (isMovementStraight().test(from, to)) {
			System.out.println("Movement Straight");

			// first move - range 2, next moves - range 1
			int maxRange = pawnFirstMove().test(from, board.getPieceAt(from)) ? 2 : 1;
			if (isMovementTooLong(maxRange).test(from, to)) {
				System.out.println("Too Long Movement!");
				throw new InvalidMoveException();
			}

			for (int i = from.getY(); i < to.getY(); i++) {
				Coordinate spot = new Coordinate(from.getX(), i + 1);
				if (!isSpotEmpty().test(spot, board)) {
					System.out.println("Spot is not empty!");
					throw new InvalidMoveException();
				}
			}
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

	public static Move validateBlackKing(Coordinate from, Coordinate to, Board board) {

		return null;
	}

	public static Move validateBlackQueen(Coordinate from, Coordinate to, Board board) {

		return null;
	}

	public static Move validateBlackBishop(Coordinate from, Coordinate to, Board board) {

		return null;
	}

	public static Move validateBlackKnight(Coordinate from, Coordinate to, Board board) throws InvalidMoveException {

		Move movement = new Move();
		movement.setFrom(from);
		movement.setTo(to);
		if(!isMovementLShaped().test(from,  to)){
			System.out.println("Movement is not L shaped!");
			throw new InvalidMoveException();
		}
		if(isSpotEmpty().test(to, board)){
			movement.setType(MoveType.ATTACK);
			movement.setMovedPiece(board.getPieceAt(from));
			return movement;
		} else if (isThisEnemyPiece().test(board.getPieceAt(from), board.getPieceAt(to))){
			movement.setType(MoveType.CAPTURE);
			movement.setMovedPiece(board.getPieceAt(from));
			return movement;
		} 
			System.out.println("My own piece at this spot!");
			throw new InvalidMoveException();
	}

	public static Move validateBlackRook(Coordinate from, Coordinate to, Board board) {

		return null;
	}

	public static Move validateBlackPawn(Coordinate from, Coordinate to, Board board) throws InvalidMoveException {

		Move movement = new Move();
		movement.setFrom(from);
		movement.setTo(to);

		System.out.println("Im gonna check !isBlackForwardMovement");

		if (!isBlackForwardMovement().test(from, to)) {
			System.out.println("Black is not moving forward");
			throw new InvalidMoveException();
		}

		System.out.println("Im gonna check isMovementStraight and isMovementDiagonal");
		System.out.println("From x: " + from.getX() + " Y: " + from.getY());
		System.out.println("To x: " + to.getX() + " Y: " + to.getY());
		if (isMovementStraight().test(from, to)) {
			System.out.println("Movement Straight");

			// first move - range 2, next moves - range 1
			int maxRange = pawnFirstMove().test(from, board.getPieceAt(from)) ? 2 : 1;
			if (isMovementTooLong(maxRange).test(from, to)) {
				System.out.println("Too Long Movement!");
				throw new InvalidMoveException();
			}

			for (int i = from.getY(); i > to.getY(); i--) {
				Coordinate spot = new Coordinate(from.getX(), i - 1);
				if (!isSpotEmpty().test(spot, board)) {
					System.out.println("Spot is not empty!");
					throw new InvalidMoveException();
				}
			}
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
		System.out.println("Im at the end");
		throw new InvalidMoveException();
	}
}
