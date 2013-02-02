package com.veganjay.game.gomoku;

import java.util.HashSet;
import java.util.Set;

import com.veganjay.game.gomoku.Logger;
import com.veganjay.game.gomoku.GomokuBoard.Piece;

public class GomokuGameNode {

	Logger logger = new Logger("GomokuGameNode");

	private GomokuBoard board;
	private Piece currentPlayer;
	
	private int   row;
	private int   col;
	
	private Piece computerPiece;
	
	public GomokuGameNode(GomokuBoard other, Piece currentPlayer) {
		this.board         = other.createCopy();
		this.currentPlayer = currentPlayer;
		this.computerPiece = currentPlayer;
	}
	
	public GomokuGameNode(GomokuBoard GomokuBoard, Piece currentPlayer, int row, int col) {
		this(GomokuBoard, currentPlayer);
		this.row = row;
		this.col = col;
	}

	public void debug() {
		this.board.printBoard();
	}
	public Move getMove() {
		return new Move(row, col);
	}
	
	/**
	 * 
	 * @return true if the board has a winner or is a tie
	 */
	public boolean isTerminalNode() {
		boolean terminal = false;
		
		if (this.board.isFull()) {
			terminal = true;
		} else if (this.board.isWinner(Piece.X)) {
			terminal = true;
		} else if (this.board.isWinner(Piece.O)) {
			terminal = true;
		}
		return terminal;
	}
	protected Piece getNextPlayer() {
		if (this.currentPlayer == Piece.X) {
			return Piece.O;
		} else {
			return Piece.X;
		}
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("(");
		sb.append(row);
		sb.append(",");
		sb.append(col);
		sb.append(")");
		
		return sb.toString();
	}

	public Set<GomokuGameNode> getChildren() {
		Set<GomokuGameNode> children = new HashSet<GomokuGameNode>();
		
		Set<Move> adjacentMoves = this.getAdjacentMoves();
		
		for (Move move : adjacentMoves) {
			// Create a new board containing this move possibility
			GomokuBoard childBoard = board.createCopy();
			
			// Attempt to add the piece to the board
			boolean success = childBoard.addPiece(move.getRow(), move.getCol(), currentPlayer);

			// If an invalid move, skip it
			if (!success) {
				continue;
			}
		
			// Store the child in the tree
			GomokuGameNode child = new GomokuGameNode(childBoard, getNextPlayer(), move.getRow(), move.getCol());
			children.add(child);
		}
		return children;
	}
	
	public int add(int n, int val) {
		int result = n + val;
		if (result >= GomokuBoard.SIZE) {
			result = GomokuBoard.SIZE - 1;
		}
		return result;
	}

	public int subtract(int n, int val) {
		int result = n - val;
		if (result < 0) {
			result = 0;
		}
		return result;
	}

	
	public int getMinRow(GomokuBoard b) {
		int row = 0;
		boolean minFound = false;

		while (!minFound && row < GomokuBoard.SIZE) {
			for (int col = 0; col < GomokuBoard.SIZE; col++) {
				if (b.isOccupied(row, col)) {
					minFound = true;
					break;
				}
			}
			row++;
		}
		return row;
	}
	
	public int getMaxRow(GomokuBoard b) {
		int row = GomokuBoard.SIZE - 1;
		boolean maxFound = false;

		while (!maxFound && row >= 0) {
			for (int col = 0; col < GomokuBoard.SIZE; col++) {
				if (b.isOccupied(row, col)) {
					maxFound = true;
					break;
				}
			}
			row--;
		}
		return row;
	}

	public int getMinCol(GomokuBoard b) {
		int col = 0;
		boolean minFound = false;

		while (!minFound && col < GomokuBoard.SIZE) {
			for (int row = 0; row < GomokuBoard.SIZE; row++) {
				if (b.isOccupied(row, col)) {
					minFound = true;
					break;
				}
			}
			col++;
		}
		return col;
	}
	
	public int getMaxCol(GomokuBoard b) {
		int col = GomokuBoard.SIZE - 1;
		boolean maxFound = false;

		while (!maxFound && col >= 0) {
			for (int row = 0; row < GomokuBoard.SIZE; row++) {
				if (b.isOccupied(row, col)) {
					maxFound = true;
					break;
				}
			}
			col--;
		}
		return col;
	}
	
	private Set<Move> getAdjacentMoves() {
		HashSet<Move> moves = new HashSet<Move>();
		
		for (int row = 0; row < GomokuBoard.SIZE; row++) {
			for (int col = 0; col < GomokuBoard.SIZE; col++) {
				if (!board.isOccupied(row, col)) {
					if (hasNeighbor(row, col)) {
						moves.add(new Move(row, col));
					}
				}
			}
		}
		return moves;
	}
	
	private boolean hasNeighbor(int row, int col) {
		boolean neighbor = false;
		
		if (board.isOccupied(row + 1, col - 1)  ||
			board.isOccupied(row + 1, col)  ||
			board.isOccupied(row + 1, col + 1)  ||

			board.isOccupied(row, col - 1)  ||
			board.isOccupied(row, col + 1)  ||

			board.isOccupied(row - 1, col - 1)  ||
			board.isOccupied(row - 1, col)  ||
			board.isOccupied(row - 1, col + 1)
			) { 
			neighbor = true;
		}
	
		return neighbor;
	}
	
	public int getObjectiveValue(Piece computerPiece) {
		int objValue = 0;
		
		Piece humanPiece = Piece.X;
		
		if (computerPiece.equals(Piece.X)) {
			humanPiece = Piece.O;			
		}
		
		if (board.isWinner(computerPiece)) {
			objValue = Integer.MAX_VALUE;
		} else if (board.isWinner(humanPiece)) {
			objValue = Integer.MIN_VALUE;
		}

		if (objValue != Integer.MIN_VALUE & objValue != Integer.MAX_VALUE) {
			// Determine the number of various "threat" positions
			int numHumanThrees    = board.getNumThrees(humanPiece);
			int numComputerThrees = board.getNumThrees(computerPiece);

			int numHumanFours     = board.getNumFours(humanPiece);
			int numComputerFours  = board.getNumFours(computerPiece);

			int numHumanStraightFours     = board.getNumStraightFours(humanPiece);
			int numComputerStraightFours  = board.getNumStraightFours(computerPiece);

			// Create an objective value
			objValue += 30 * numComputerThrees;
			objValue -= 50 * numHumanThrees;
	
			objValue += 300 * numComputerFours;
			objValue -= 500 * numHumanFours;
	
			objValue += 900 * numComputerStraightFours;
			objValue -= 1000 * numHumanStraightFours;
		}

		return objValue;
	}

}
