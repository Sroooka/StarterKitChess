package com.capgemini.chess.algorithms.implementation;

import com.capgemini.chess.algorithms.data.Coordinate;
import com.capgemini.chess.algorithms.data.Move;
import com.capgemini.chess.algorithms.data.enums.Color;
import com.capgemini.chess.algorithms.data.enums.MoveType;
import com.capgemini.chess.algorithms.data.enums.Piece;
import com.capgemini.chess.algorithms.data.generated.Board;
import com.capgemini.chess.algorithms.implementation.exceptions.InvalidMoveException;
import com.capgemini.chess.algorithms.implementation.exceptions.KingInCheckException;

import static com.capgemini.chess.algorithms.data.PredicateFactory.*;

public class MovementManager {

	private Coordinate from;
	private Coordinate to;
	private Board board;
	private Move movement;

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

	private Move chooseRightValidator() throws InvalidMoveException {
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
	
	public Move validate() throws KingInCheckException, InvalidMoveException {
		pieceIsOnBoard();
		playerIsMovingHisOwnFigure();
		sourceSpotIsNotEmpty();
		coordinatesAreNotTheSame();
		return chooseRightValidator();
	}

	public Move validateWithoutPlayer() throws KingInCheckException, InvalidMoveException {
		pieceIsOnBoard();
		sourceSpotIsNotEmpty();
		coordinatesAreNotTheSame();
		return chooseRightValidator();
	}

	private Move validateKing() throws InvalidMoveException, KingInCheckException {
		if (lenghtOfMovementEquals(2)) {
			isCastling();
			return castlingtDone();
		} else {
			checkRangeOfLength(1);
			return movementDone();
		}
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
			if (destinationSpotIsEmpty()) {
				historyMustBeNotEmpty();
				checkEnPassantPossibility();
				return enPassantDone();
			} else {
				destinationSpotMustContainEnemy();
				return captureDone();
			}
		}
		throw new InvalidMoveException();
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
							MovementManager movementManager = new MovementManager(srcSpot, destSpot, board);
							try {
								movementManager.validateWithoutPlayer();
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
	
	private void historyMustBeNotEmpty() throws InvalidMoveException {
		if (board.getMoveHistory() == null || board.getMoveHistory().isEmpty()) {
			throw new InvalidMoveException();
		}
	}

	private void checkEnPassantPossibility() throws InvalidMoveException {
		Move previousMovement = board.getMoveHistory().get(board.getMoveHistory().size() - 1);
		if (previousMovement == null && !checkEnPassant().test(to, previousMovement)) {
			throw new InvalidMoveException();
		}
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
	
	private void playerIsMovingHisOwnFigure() throws InvalidMoveException {
		if (!movingMyOwnFigure(calculateActualPlayerColor()).test(from, board)) {
			throw new InvalidMoveException();
		}
	}

	private void movementMustBeStraight() throws InvalidMoveException {
		if (!isMovementStraight().test(from, to)) {
			throw new InvalidMoveException();
		}
	}

	private boolean destinationSpotIsEmpty() throws InvalidMoveException {
		return isSpotEmpty().test(to, board);
	}

	private void destinationSpotMustContainEnemy() throws InvalidMoveException {
		if (!isThisEnemyPiece().test(board.getPieceAt(from), board.getPieceAt(to))) {
			throw new InvalidMoveException();
		}
	}
	
	private boolean lenghtOfMovementEquals(int i) {
		return !(Math.abs(from.getX() - to.getX()) != i);
	}

	private void destinationSpotMustBeEmpty() throws InvalidMoveException {
		if (!isSpotEmpty().test(to, board)) {
			throw new InvalidMoveException();
		}
	}

	private void checkObstaclesWhenMovingStraight() throws InvalidMoveException {
		int deltaX = from.getX() - to.getX();
		int deltaY = from.getY() - to.getY();
		Coordinate spot = from;
		int xDirection = 0, yDirection = 0;
		if (deltaX == 0) {
			yDirection = (deltaY < 0) ? 1 : -1;
		} else {
			xDirection = (deltaX < 0) ? 1 : -1;
		}

		for (int i = 0; i < Math.abs(deltaX + deltaY) - 1; i++) {
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
	
	private Move attackDone() throws KingInCheckException {
		movement.setType(MoveType.ATTACK);
		movement.setMovedPiece(board.getPieceAt(from));
		myKingCantBeUndelied();
		return movement;
	}
	
	private Move captureDone() throws InvalidMoveException {
		movement.setType(MoveType.CAPTURE);
		movement.setMovedPiece(board.getPieceAt(from));
		myKingCantBeUndelied();
		return movement;
	}

	private Move castlingtDone() {
		movement.setType(MoveType.CASTLING);
		movement.setMovedPiece(board.getPieceAt(from));
		return movement;
	}

	private Move enPassantDone() throws InvalidMoveException {
		movement.setType(MoveType.EN_PASSANT);
		movement.setMovedPiece(board.getPieceAt(from));
		myKingCantBeUndelied();
		return movement;
	}

	private Move movementDone() throws InvalidMoveException {
		if (isSpotEmpty().test(to, board)) {
			movement.setType(MoveType.ATTACK);
			movement.setMovedPiece(board.getPieceAt(from));
			myKingCantBeUndelied();
			return movement;
		} else if (isThisEnemyPiece().test(board.getPieceAt(from), board.getPieceAt(to))) {
			movement.setType(MoveType.CAPTURE);
			movement.setMovedPiece(board.getPieceAt(from));
			myKingCantBeUndelied();
			return movement;
		} else {
			throw new InvalidMoveException();
		}
	}

	public boolean isKingInCheckValidator(Color kingColor) {
		Board tempBoard = new Board();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Coordinate spot = new Coordinate(i, j);
				tempBoard.setPieceAt(board.getPieceAt(spot), spot);
			}
		}
		Coordinate kingPosition = findMyKingPosition(kingColor, tempBoard);
		if (kingPosition != null) {
			if (kingColor == Color.WHITE) {
				tempBoard.setPieceAt(Piece.WHITE_PAWN, kingPosition);
			} else {
				tempBoard.setPieceAt(Piece.BLACK_PAWN, kingPosition);
			}
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					Coordinate spot = new Coordinate(i, j);
					if (!isSpotEmpty().test(spot, tempBoard) && (tempBoard.getPieceAt(spot).getColor() != kingColor)) {
						if (canPieceReachMyKing(spot, kingPosition, tempBoard)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private void myKingCantBeUndelied() throws KingInCheckException {
		Color myColor = board.getPieceAt(from).getColor();
		Board tempBoard = new Board();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Coordinate spot = new Coordinate(i, j);
				tempBoard.setPieceAt(board.getPieceAt(spot), spot);
			}
		}
		for (Move move : board.getMoveHistory()) {
			tempBoard.getMoveHistory().add(move);
		}

		Piece movedPiece = board.getPieceAt(from);
		tempBoard.setPieceAt(null, from);
		tempBoard.setPieceAt(movedPiece, to);
		Coordinate kingPosition = findMyKingPosition(myColor, tempBoard);

		if (kingPosition != null) {
			if (myColor == Color.WHITE) {
				tempBoard.setPieceAt(Piece.WHITE_PAWN, kingPosition);
			} else {
				tempBoard.setPieceAt(Piece.BLACK_PAWN, kingPosition);
			}
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					Coordinate spot = new Coordinate(i, j);
					if (!isSpotEmpty().test(spot, tempBoard) && (tempBoard.getPieceAt(spot).getColor() != myColor)) {
						if (canPieceReachMyKing(spot, kingPosition, tempBoard)) {
							throw new KingInCheckException();
						}
					}
				}
			}
		}
	}

	private boolean canPieceReachMyKing(Coordinate spot, Coordinate kingPosition, Board tempBoard) {
		boolean kingReached = false;
		try {
			MovementManager movementManager = new MovementManager(spot, kingPosition, tempBoard);
			movementManager.validateWithoutPlayer();
			kingReached = true;
		} catch (InvalidMoveException e2) {
		}
		return kingReached;
	}

	private Coordinate findMyKingPosition(Color myColor, Board tempBoard) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Coordinate spot = new Coordinate(i, j);
				if (isThisMyKing(myColor).test(spot, tempBoard)) {
					return spot;
				}
			}
		}
		return null;
	}

	private Color calculateActualPlayerColor() {
		if (this.board.getMoveHistory().size() % 2 == 0) {
			return Color.WHITE;
		} else {
			return Color.BLACK;
		}
	}

	private void isCastling() throws InvalidMoveException {
		movementMustBeStraight();
		Coordinate tempTo = this.to;
		this.to = findValidRook();
		checkObstaclesWhenMovingStraight();
		this.to = tempTo;
		destinationSpotMustBeEmpty();
		mustBeFirstMovementOfKing();
		mustBeFirstMovementOfRook();
		checkSpotsThatKingCanReachAreNotUnderAttack();
	}

	private void checkSpotsThatKingCanReachAreNotUnderAttack() throws KingInCheckException {
		int moveDirection = ((from.getX() - to.getX()) > 0) ? -1 : 1;
		Coordinate tempTo = this.to;
		Coordinate spotBetweenKingAndDestination = new Coordinate(from.getX() + moveDirection, from.getY());
		myKingCantBeUndelied();
		this.to = spotBetweenKingAndDestination;
		myKingCantBeUndelied();
		this.to = tempTo;
	}

	private Coordinate findValidRook() {
		Coordinate cord = ((from.getX() - to.getX()) > 0) ? new Coordinate(0, from.getY())
				: new Coordinate(7, from.getY());
		return cord;
	}

	private void mustBeFirstMovementOfKing() throws InvalidMoveException {
		Color myColor = board.getPieceAt(from).getColor();
		Piece king = (myColor == Color.WHITE) ? Piece.WHITE_KING : Piece.BLACK_KING;
		for (Move move : board.getMoveHistory()) {
			if (doesMovementContainPiece().test(move, king)) {
				throw new InvalidMoveException();
			}
		}
	}

	private void mustBeFirstMovementOfRook() throws InvalidMoveException {
		Color myColor = board.getPieceAt(from).getColor();
		Piece rook = (myColor == Color.WHITE) ? Piece.WHITE_ROOK : Piece.BLACK_ROOK;
		for (Move move : board.getMoveHistory()) {
			if (doesMovementContainPiece().test(move, rook)) {
				throw new InvalidMoveException();
			}
		}
	}
}
