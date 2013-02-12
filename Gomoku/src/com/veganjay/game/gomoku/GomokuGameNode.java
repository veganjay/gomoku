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
	
	public boolean isComputerWin() {
		return this.board.isWinner(computerPiece);
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
	
	public Set<Move> getAdjacentMoves() {
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
		
		// TODO - move isWinner inside loop
		
		Piece humanPiece = Piece.X;
		
		if (computerPiece.equals(Piece.X)) {
			humanPiece = Piece.O;			
		}
		
		if (board.isWinner(computerPiece)) {
			objValue = 100000;
		} else if (board.isWinner(humanPiece)) {
			objValue = -100000;
		} else {
			int numHumanFours = 0;
			int numComputerFours = 0;

			int numHumanThrees = 0;
			int numComputerThrees = 0;

			int numHumanTwos = 0;
			int numComputerTwos = 0;

			Piece p1, p2, p3, p4, p5, p6, p7, p8;
			
			// Look through the entire board
			for (int i = 0; i < GomokuBoard.SIZE; i++) {
				for (int j = 0; j < GomokuBoard.SIZE; j++) {

					// Vertical Line
					p1 = board.getPiece(i, j);
					p2 = board.getPiece(i+1, j);
					p3 = board.getPiece(i+2, j);
					p4 = board.getPiece(i+3, j);
					p5 = board.getPiece(i+4, j);
					p6 = board.getPiece(i+5, j);
					p7 = board.getPiece(i+6, j);

					// Check for four threats
					if (hasFourThreat(humanPiece, p1, p2, p3, p4, p5, p6)) {
						numHumanFours++;
					} else if (hasFourThreat(computerPiece, p1, p2, p3, p4, p5, p6)) {
						numComputerFours++;
					}
					
					// Check for three threats
					if (hasThreeThreat(humanPiece, p1, p2, p3, p4, p5, p6, p7)) {
						numHumanThrees++;
					} else if (hasThreeThreat(computerPiece, p1, p2, p3, p4, p5, p6, p7)) {
						numComputerThrees++;
					}
					
					// Check for two threats
					if (hasTwoThreat(humanPiece, p1, p2, p3, p4, p5)) {
						numHumanTwos++;
					} else if (hasTwoThreat(computerPiece, p1, p2, p3, p4, p5)) {
						numComputerTwos++;
					}

					// Horizontal Line
					p1 = board.getPiece(i, j);
					p2 = board.getPiece(i, j+1);
					p3 = board.getPiece(i, j+2);
					p4 = board.getPiece(i, j+3);
					p5 = board.getPiece(i, j+4);
					p6 = board.getPiece(i, j+5);
					p7 = board.getPiece(i, j+6);
					
					// Check for four threats
					if (hasFourThreat(humanPiece, p1, p2, p3, p4, p5, p6)) {
						numHumanFours++;
					} else if (hasFourThreat(computerPiece, p1, p2, p3, p4, p5, p6)) {
						numComputerFours++;
					}
					
					// Check for three threats
					if (hasThreeThreat(humanPiece, p1, p2, p3, p4, p5, p6, p7)) {
						numHumanThrees++;
					} else if (hasThreeThreat(computerPiece, p1, p2, p3, p4, p5, p6, p7)) {
						numComputerThrees++;
					}
					
					// Check for two threats
					if (hasTwoThreat(humanPiece, p1, p2, p3, p4, p5)) {
						numHumanTwos++;
					} else if (hasTwoThreat(computerPiece, p1, p2, p3, p4, p5)) {
						numComputerTwos++;
					}
					
					// Diagonal Line A
					p1 = board.getPiece(i, j);
					p2 = board.getPiece(i+1, j+1);
					p3 = board.getPiece(i+2, j+2);
					p4 = board.getPiece(i+3, j+3);
					p5 = board.getPiece(i+4, j+4);
					p6 = board.getPiece(i+5, j+5);
					p7 = board.getPiece(i+6, j+6);

					// Check for four threats
					if (hasFourThreat(humanPiece, p1, p2, p3, p4, p5, p6)) {
						numHumanFours++;
					} else if (hasFourThreat(computerPiece, p1, p2, p3, p4, p5, p6)) {
						numComputerFours++;
					}
					
					// Check for three threats
					if (hasThreeThreat(humanPiece, p1, p2, p3, p4, p5, p6, p7)) {
						numHumanThrees++;
					} else if (hasThreeThreat(computerPiece, p1, p2, p3, p4, p5, p6, p7)) {
						numComputerThrees++;
					}
					
					// Check for two threats
					if (hasTwoThreat(humanPiece, p1, p2, p3, p4, p5)) {
						numHumanTwos++;
					} else if (hasTwoThreat(computerPiece, p1, p2, p3, p4, p5)) {
						numComputerTwos++;
					}

					
					// Diagonal Line B
					p1 = board.getPiece(i, j);
					p2 = board.getPiece(i+1, j-1);
					p3 = board.getPiece(i+2, j-2);
					p4 = board.getPiece(i+3, j-3);
					p5 = board.getPiece(i+4, j-4);
					p6 = board.getPiece(i+5, j-5);
					p7 = board.getPiece(i+6, j-6);
					

					// Check for four threats
					if (hasFourThreat(humanPiece, p1, p2, p3, p4, p5, p6)) {
						numHumanFours++;
					} else if (hasFourThreat(computerPiece, p1, p2, p3, p4, p5, p6)) {
						numComputerFours++;
					}
					
					// Check for three threats
					if (hasThreeThreat(humanPiece, p1, p2, p3, p4, p5, p6, p7)) {
						numHumanThrees++;
					} else if (hasThreeThreat(computerPiece, p1, p2, p3, p4, p5, p6, p7)) {
						numComputerThrees++;
					}
					
					// Check for two threats
					if (hasTwoThreat(humanPiece, p1, p2, p3, p4, p5)) {
						numHumanTwos++;
					} else if (hasTwoThreat(computerPiece, p1, p2, p3, p4, p5)) {
						numComputerTwos++;
					}

				}
			}

			if (numHumanFours > 1) {
				objValue = -90000;
			} else if (numComputerFours > 1) {
				objValue = 90000;
			} else {
				objValue += numHumanFours  * -1000;
				objValue += numHumanThrees * -500;
				objValue += numHumanTwos   * -20;
				objValue += numComputerFours  * 1000;
				objValue += numComputerThrees * 500;				
				objValue += numComputerTwos   * 20;
			}
		}

		return objValue;
	}

	private boolean hasFourThreat(Piece humanPiece, Piece p1, Piece p2,
			Piece p3, Piece p4, Piece p5, Piece p6) {
		return isFour(humanPiece, p1, p2, p3, p4, p5) ||
			isStraightFour(humanPiece, p1, p2, p3, p4, p5, p6);
	}

	private boolean hasThreeThreat(Piece humanPiece, Piece p1, Piece p2,
			Piece p3, Piece p4, Piece p5, Piece p6, Piece p7) {
		return isThree(humanPiece, p1, p2, p3, p4, p5, p6) ||
			isThree(humanPiece, p1, p2, p3, p4, p5, p6, p7) || 
			isBrokenThree(humanPiece, p1, p2, p3, p4, p5, p6);
	}
	
	private boolean hasTwoThreat(Piece humanPiece, Piece p1, Piece p2,
			Piece p3, Piece p4, Piece p5) {
		return isTwo(humanPiece, p1, p2, p3, p4, p5);
	}
		
	/**
	 * Determine if a sequence is a "four" defined as:
	 * A line of five squares, consisting in any order:
	 * (4) ATTACKER, and (1) EMPTY
	 */	
	private boolean isFour(Piece p, Piece p1, Piece p2, Piece p3, Piece p4, Piece p5) {
		boolean four = false;
		
		int numPiece = 0;
		int numEmpty = 0;
		
		if (p1 == p) {
			numPiece++;
		} else if (p1 == Piece.EMPTY) {
			numEmpty++;
		}
		
		if (p2 == p) {
			numPiece++;
		} else if (p2 == Piece.EMPTY) {
			numEmpty++;
		}

		if (p3 == p) {
			numPiece++;
		} else if (p3 == Piece.EMPTY) {
			numEmpty++;
		}

		if (p4 == p) {
			numPiece++;
		} else if (p4 == Piece.EMPTY) {
			numEmpty++;
		}
		if (p5 == p) {
			numPiece++;
		} else if (p5 == Piece.EMPTY) {
			numEmpty++;
		}

		// Check for four attacker pieces and one empty
		// in any order
		if (numPiece == 4 && numEmpty == 1) {
			four = true;
		}
		return four;
	}
	
	/**
	 * Determine if a sequence is a "Straight Four" defined as:
	 * A line of six squares, consisting in order:
	 * EMPTY, ATTACKER, ATTACKER, ATTACKER, ATTACKER, EMPTY
	 */
	private boolean isStraightFour(Piece p, Piece p1, Piece p2, Piece p3, Piece p4, Piece p5, Piece p6) {
		return (p1 == Piece.EMPTY &&
			p2 == p && p3 == p && p4 == p && p5 == p &&
			p6 == Piece.EMPTY);
	}
	/**
	 * Determine if a sequence is a "Three" defined as:
	 * A line of seven squares, consisting in order:
	 * EMPTY, EMPTY, ATTACKER, ATTACKER, ATTACKER, EMPTY, EMPTY
	 */
	private boolean isThree(Piece p, Piece p1, Piece p2, Piece p3, Piece p4, Piece p5, Piece p6, Piece p7) {
		boolean three = false;
		
		if (p1 == Piece.EMPTY && p2 == Piece.EMPTY &&
			p3 == p && p4 == p && p5 == p &&
			p6 == Piece.EMPTY && p7 == Piece.EMPTY) {
			three = true;
		}
		
		return three;
	}
	
	/**
	 * Determine if a sequence is a "Three" defined as:
	 * A line of six squares, consisting of 
	 * EMPTY, ATTACKER, ATTACKER, ATTACKER, EMPTY, EMPTY or
	 * EMPTY, EMPTY, ATTACKER, ATTACKER, ATTACKER, EMPTY
	 */
	private boolean isThree(Piece p, Piece p1, Piece p2, Piece p3, Piece p4, Piece p5, Piece p6) {
		boolean three = false;
		
		if (p1 == Piece.EMPTY && p6 == Piece.EMPTY) {
			if ( 	(p2 == p && p3 == p && p4 == p && p5 == Piece.EMPTY) ||
					(p2 == Piece.EMPTY && p3 == p && p4 == p && p5 == p) ) {
				three = true;
			}
		}
		
		return three;
	}
	
	/**
	 * Determine if a sequence is a "Broken Three" defined as:
	 * A line of six squares which the attacker has occupied three non-consecutive squares
	 * of the four center squares, while the other three squares are empty:
	 * EMPTY, ATTACKER, ATTACKER, EMPTY, ATTACKER, EMPTY or
	 * EMPTY, ATTACKER, EMPTY, ATTACKER, ATTACKER, EMPTY
	 * @param p
	 * @return number of broken threes
	 */
	private boolean isBrokenThree(Piece p, Piece p1, Piece p2, Piece p3, Piece p4, Piece p5, Piece p6) {
		boolean three = false;
		
		if (p1 == Piece.EMPTY && p6 == Piece.EMPTY) {
			if ( (p2 == p && p3 == p && p4 == Piece.EMPTY && p5 == p) ||
				 (p2 == p && p3 == Piece.EMPTY && p4 == p && p5 == p) ) {
				three = true;
			}
		}
		
		return three;
	}
	
	/**
	 * Determine if a sequence is a "two" defined as:
	 * A line of five squares, consisting in any order:
	 * (2) ATTACKER, and (3) EMPTY	 
	 */
	private boolean isTwo(Piece p, Piece p1, Piece p2, Piece p3, Piece p4, Piece p5) {
		boolean two = false;
		
		int numPiece = 0;
		int numEmpty = 0;
		
		if (p1 == p) {
			numPiece++;
		} else if (p1 == Piece.EMPTY) {
			numEmpty++;
		}
		
		if (p2 == p) {
			numPiece++;
		} else if (p2 == Piece.EMPTY) {
			numEmpty++;
		}

		if (p3 == p) {
			numPiece++;
		} else if (p3 == Piece.EMPTY) {
			numEmpty++;
		}

		if (p4 == p) {
			numPiece++;
		} else if (p4 == Piece.EMPTY) {
			numEmpty++;
		}
		if (p5 == p) {
			numPiece++;
		} else if (p5 == Piece.EMPTY) {
			numEmpty++;
		}

		// Check for two attacker pieces and three empty
		// in any order
		if (numPiece == 2 && numEmpty == 3) {
			two = true;
		}
		return two;
	}
}
