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

	private Coordinate from;
	private Coordinate to;
	private Board board;
	private Move movement;

	public MovementManager() {
		this.from = null;
		this.to = null;
		this.board = null;
		this.movement = null;
	}

	public MovementManager(Board board) {
		this.from = null;
		this.to = null;
		this.board = board;
		this.movement = new Move();
		this.movement.setFrom(new Coordinate(0, 0));
		this.movement.setTo(new Coordinate(0, 0));
	}

	public MovementManager(Coordinate from, Coordinate to, Board board) {
		this.from = from;
		this.to = to;
		this.board = board;
		this.movement = new Move();
		this.movement.setFrom(from);
		this.movement.setTo(to);
	}

	public Move validate() throws KingInCheckException, InvalidMoveException {
		pieceIsOnBoard();
		sourceSpotIsNotEmpty();
		coordinatesAreNotTheSame();

		switch (board.getPieceAt(from).getType()) {
		case KING:
			return validateKing();
		case QUEEN:
			return validateQueen();
		case BISHOP:
			return validateBishop();
		case KNIGHT:
			return validateKnight();
		case ROOK:
			return validateRook();
		case PAWN:
			return validatePawn();
		default:
			throw new InvalidMoveException();
		}
	}

	public boolean areAnyPossibleMoves(Color nextMoveColor) {
		Coordinate srcSpot, destSpot;
		boolean foundPossibleMove = false;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				srcSpot = new Coordinate(i, j);
				if (!isSpotEmpty().test(srcSpot, board) && (board.getPieceAt(srcSpot).getColor() == nextMoveColor)) {
					for (int m = 0; m < 8; m++) {
						for (int n = 0; n < 8; n++) {
							destSpot = new Coordinate(m, n);
							this.from = srcSpot;
							this.to = destSpot;
							try {
								validate();
								foundPossibleMove = true;
							} catch (InvalidMoveException e2) {
								
							}
						}
					}
				}
			}
		}
		return foundPossibleMove;
	}

	private Move validateKing() throws InvalidMoveException, KingInCheckException {

		// TODO sprawdzanie czy wykonywana jest roszada, wtedy max range krola

		checkRangeOfLength(1);

		return movementDone();
	}

	private Move validateQueen() throws InvalidMoveException {

		if (isMovementStraight().test(from, to)) {
			checkObstaclesWhenMovingStraight();

		} else if (isMovementDiagonal().test(from, to)) {
			checkObstaclesWhenMovingDiagonal();

		} else {
			throw new InvalidMoveException();
		}

		return movementDone();
	}

	private Move validateBishop() throws InvalidMoveException {

		movementMustBeDiagonal();

		checkObstaclesWhenMovingDiagonal();

		return movementDone();

	}

	private Move validateKnight() throws InvalidMoveException {

		movementMustBeLShaped();

		return movementDone();

	}

	private Move validateRook() throws InvalidMoveException {

		movementMustBeStraight();

		checkObstaclesWhenMovingStraight();

		return movementDone();

	}

	private Move validatePawn() throws InvalidMoveException {

		pawnMustMoveForward();
		if (isMovementStraight().test(from, to)) {
			int maxRange = pawnFirstMove().test(from, board.getPieceAt(from)) ? 2 : 1;
			checkRangeOfLength(maxRange);
			pawnSpotsOnWayMustBeEmpty(maxRange);
			return attackDone();

		} else if (isMovementDiagonal().test(from, to)) {
			checkRangeOfLength(1);
			destinationSpotCannotBeEmpty();
			destinationSpotMustContainEnemy();
			return captureDone();
		}
		throw new InvalidMoveException();
	}

	public void pieceIsOnBoard() throws InvalidMoveException {
		if (pieceOutOfBoard().test(from, to)) {
			throw new InvalidMoveException();
		}
	}

	public void sourceSpotIsNotEmpty() throws InvalidMoveException {
		if (isSpotEmpty().test(from, board)) {
			throw new InvalidMoveException();
		}
	}

	public void coordinatesAreNotTheSame() throws InvalidMoveException {
		if (theSameCoordinates().test(from, to)) {
			throw new InvalidMoveException();
		}
	}

	private void checkRangeOfLength(int maxRange) throws InvalidMoveException {
		if (isMovementTooLong(maxRange).test(from, to)) {
			throw new InvalidMoveException();
		}
	}

	private void pawnSpotsOnWayMustBeEmpty(int maxRange) throws InvalidMoveException {
		int movementDirection = (board.getPieceAt(from).getColor() == Color.BLACK) ? -1 : 1;
		Coordinate spot = from;
		for (int i = 0; i < maxRange; i++) {
			spot = new Coordinate(spot.getX(), spot.getY() + movementDirection);
			if (!isSpotEmpty().test(spot, board)) {
				throw new InvalidMoveException();
			}
		}
	}

	private void pawnMustMoveForward() throws InvalidMoveException {
		boolean isBlack = (board.getPieceAt(from).getColor() == Color.BLACK) ? true : false;
		if ((!isBlack && !isWhiteForwardMovement().test(from, to))
				|| (isBlack && !isBlackForwardMovement().test(from, to))) {
			throw new InvalidMoveException();
		}
	}

	private void checkObstaclesWhenMovingStraight() throws InvalidMoveException {
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
				throw new InvalidMoveException();
			}
		}
	}

	private void checkObstaclesWhenMovingDiagonal() throws InvalidMoveException {
		int deltaX = from.getX() - to.getX();
		int deltaY = from.getY() - to.getY();
		Coordinate spot = from;
		int xDirection = (deltaX < 0) ? 1 : -1;
		int yDirection = (deltaY < 0) ? 1 : -1;
		for (int i = 0; i < Math.abs(deltaX) - 1; i++) {
			spot = new Coordinate(spot.getX() + xDirection, spot.getY() + yDirection);
			if (!isSpotEmpty().test(spot, board)) {
				throw new InvalidMoveException();
			}
		}

	}

	private void movementMustBeDiagonal() throws InvalidMoveException {
		if (!isMovementDiagonal().test(from, to)) {
			throw new InvalidMoveException();
		}
	}

	private void movementMustBeLShaped() throws InvalidMoveException {
		if (!isMovementLShaped().test(from, to)) {
			throw new InvalidMoveException();
		}
	}

	private void movementMustBeStraight() throws InvalidMoveException {
		if (!isMovementStraight().test(from, to)) {
			throw new InvalidMoveException();
		}
	}

	private void destinationSpotCannotBeEmpty() throws InvalidMoveException {
		if (isSpotEmpty().test(to, board)) {
			throw new InvalidMoveException();
		}
	}

	private void destinationSpotMustContainEnemy() throws InvalidMoveException {
		if (!isThisEnemyPiece().test(board.getPieceAt(from), board.getPieceAt(to))) {
			throw new InvalidMoveException();
		}
	}

	private Move captureDone() throws InvalidMoveException {
		cantCaptureKing();
		movement.setType(MoveType.CAPTURE);
		movement.setMovedPiece(board.getPieceAt(from));
		return movement;
	}

	private Move attackDone() {
		movement.setType(MoveType.ATTACK);
		movement.setMovedPiece(board.getPieceAt(from));
		return movement;
	}

	private Move movementDone() throws InvalidMoveException {
		if (isSpotEmpty().test(to, board)) {
			movement.setType(MoveType.ATTACK);
			movement.setMovedPiece(board.getPieceAt(from));
			return movement;
		} else if (isThisEnemyPiece().test(board.getPieceAt(from), board.getPieceAt(to))) {
			cantCaptureKing();
			movement.setType(MoveType.CAPTURE);
			movement.setMovedPiece(board.getPieceAt(from));
			return movement;
		} else {
			throw new InvalidMoveException();
		}
	}

	private void cantCaptureKing() throws InvalidMoveException {
		if (pieceIsKing().test(board.getPieceAt(to))) {
			throw new InvalidMoveException();
		}
	}
}
