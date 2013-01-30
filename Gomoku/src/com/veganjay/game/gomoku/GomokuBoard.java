package com.veganjay.game.gomoku;


public class GomokuBoard {
	
	public static enum Piece { EMPTY, X, O };

	// The Game board itself
	protected Piece [][] board;

	// Size of the Board
	public static final int SIZE = 15;	
	
	/**
	 * Create the Gomoku Board and set all the pieces to empty
	 */
	public GomokuBoard() {
		board = new Piece[SIZE][SIZE];
		this.reset();
	}
	
	/**
	 * Create a copy of an existing board
	 * @return a copy of the Gomoku board
	 */
	public GomokuBoard createCopy() {
		GomokuBoard other = new GomokuBoard();
		
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				other.board[i][j] = this.board[i][j];
			}
		}	
		return other;
	}

	/**
	 * Set all positions to Empty
	 */
	public void reset() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				this.board[i][j] = Piece.EMPTY;
			}
		}
	}
	
	/**
	 * @param row
	 * @param col
	 * @return true if non empty
	 */
	public boolean isOccupied(int row, int col) {
		boolean occupied = false;
		
		if (row < 0 || col < 0 || row >= SIZE || col >= SIZE) {
			occupied = false;
		} else {
			occupied = (!(board[row][col] == Piece.EMPTY));
		}
		return occupied;
	}
	
	/**
	 * Add a piece to the board
	 * @param row
	 * @param col
	 * @param p
	 * @return false if the space is occupied, true otherwise
	 */
	public boolean addPiece(int row, int col, Piece p) {
		boolean success = true;
		
		if (isOccupied(row, col)) {
			success = false;
		} else {
			board[row][col] = p;
		}
		return success;
	}

	public void printBoard() {
		System.out.println("                       1 1 1 1 1");
		System.out.println("   0 1 2 3 4 5 6 7 8 9 0 1 2 3 4");
		for (int i = SIZE - 1; i >= 0; i--) {
			printRowHeadings(i);

			for (int j = 0; j < SIZE; j++) {
				this.printPiece(board[i][j]);
			}
			System.out.println();
		}

	}
	/**
	 * Display the board
	 */
	public void printBoardOld() {
		printRowSeparator();
		
		for (int i = SIZE - 1; i >= 0; i--) {
			printRowHeadings(i);

			for (int j = 0; j < SIZE; j++) {
				this.printPiece(board[i][j]);
			}
			System.out.println("|");
			printRowSeparator();
		}
		
		printColumnHeadings();
	}

	private void printRowSeparator() {
		System.out.println("	-------------------------------------------------------------");
	}

	private void printColumnHeadings() {
		// Print column headings
		System.out.print("	");
		System.out.print("  0   1   2   3   4   5   6   7   8   9  10  11  12  13  14");
		System.out.println();
	}

	private void printRowHeadings(int i) {
		// Print row headings
		if (i < 10) {
			System.out.print(0);
		}
		System.out.print(i);
		System.out.print(" ");
	}

	private void printPiece(Piece p) {
		String pieceStr = ".";
		if (p.equals(Piece.X)) {
			pieceStr = "X";
		} else if (p.equals(Piece.O)) {
			pieceStr = "O";
		}
		System.out.print("");
		System.out.print(pieceStr);
		System.out.print(" ");
	}

	private void printPieceOld(Piece p) {
		String pieceStr = " ";
		if (p.equals(Piece.X)) {
			pieceStr = "X";
		} else if (p.equals(Piece.O)) {
			pieceStr = "O";
		}
		System.out.print("| ");
		System.out.print(pieceStr);
		System.out.print(" ");
	}

	/**
	 * @return true if the board is full
	 */
	public boolean isFull() {
		boolean full = true;
		
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (this.board[i][j] == Piece.EMPTY) {
					full = false;
					break;
				}
			}
		}
		return full;
	}

	/**
	 * @param p a piece
	 * @return true if the piece is a winner
	 */
	public boolean isWinner(Piece p) {
		boolean won = false;
		
		if (hasHorizontalWin(p)) {
			won = true;
		} else if (hasVerticalWin(p)) {
			won = true;
		} else if (hasDiagonalWin(p)) {
			won = true;
		}
		return won;
	}
	
	private boolean hasHorizontalWin(Piece p) {
		boolean won = false;

		for (int i = 0; i <= SIZE - 5; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (board[i][j]   == p &&
					board[i+1][j] == p &&
					board[i+2][j] == p &&
					board[i+3][j] == p &&
					board[i+4][j] == p) {
					won = true;
					break;
				}
			}
		}
		
		return won;
	}
	
	private boolean hasVerticalWin(Piece p) {
		boolean won = false;

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j <= SIZE - 5; j++) {
				if (board[i][j]   == p &&
					board[i][j+1] == p &&
					board[i][j+2] == p &&
					board[i][j+3] == p &&
					board[i][j+4] == p) {
					won = true;
					break;
				}
			}
		}
		
		return won;
	}
	
	protected boolean hasDiagonalWin(Piece p) {
		boolean won = false;
		
		// Check the first diagonal
		for (int i = 0; i <= SIZE - 5; i++) {
			for (int j = 0; j <= SIZE - 5; j++) {
				if (board[i][j]   == p &&
					board[i+1][j+1] == p &&
					board[i+2][j+2] == p &&
					board[i+3][j+3] == p &&
					board[i+4][j+4] == p) {
					won = true;
					break;
				}
			}
		}
		
		// Check the second diagonal
		for (int i = 0; i <= SIZE - 5 ; i++) {  // Rows 0..10
			for (int j = 4; j < SIZE; j++) {    // Cols 4..14
				if (board[i][j]   == p &&
					board[i+1][j-1] == p &&
					board[i+2][j-2] == p &&
					board[i+3][j-3] == p &&
					board[i+4][j-4] == p) {
					won = true;
					break;
				}
			}
		}
		return won;
	}

	public int getRows() {
		return SIZE;
	}

	public int getCols() {
		return SIZE;
	}

	/**
	 * Get the number of "fours" defined as:
	 * A line of five squares, consisting in any order:
	 * (4) ATTACKER, and (1) EMPTY
	 * @param p the Piece to check
	 * @return number of fours
	 */
	public int getNumFours(Piece p) {
		return getNumFoursVertical(p) +
			   getNumFoursHorizontal(p) +
			   getNumFoursDiagonal(p);
	}
	
	/**
	 * Get the number of "straight fours" defined as:
	 * A line of six squares, consisting in order:
	 * EMPTY, ATTACKER, ATTACKER, ATTACKER, ATTACKER, EMPTY
	 * @param p the Piece to check
	 * @return number of straight fours
	 */
	public int getNumStraightFours(Piece p) {
		return getNumStraightFoursVertical(p) +
			   getNumStraightFoursHorizontal(p) +
			   getNumStraightFoursDiagonal(p);
	}
	
	/**
	 * Get the number of "threes" defined as:
	 * A line of seven squares, consisting in order:
	 * EMPTY, EMPTY, ATTACKER, ATTACKER, ATTACKER, EMPTY, EMPTY
	 * or
	 * TODO:
	 * A line of six squares, consisting of 
	 * EMPTY, ATTACKER, ATTACKER, ATTACKER, EMPTY, EMPTY or
	 * EMPTY, ATTACKER, ATTACKER, EMPTY, ATTACKER, EMPTY or
	 * EMPTY, ATTACKER, EMPTY, ATTACKER, ATTACKER, EMPTY or
	 * EMPTY, EMPTY, ATTACKER, ATTACKER, ATTACKER, EMPTY
	 * @param p the Piece to check
	 * @return number of threes
	 */	
	public int getNumThrees(Piece p) {
		return getNumThreesVertical(p) +
			   getNumThreesHorizontal(p) +
			   getNumThreesDiagonal(p);
	}
	
	/**
	 * TODO
	 * Get the number of "broken threes" defined as:
	 * A line of six squares which the attacker has occupied three non-consecutive squares
	 * of the four center squares, while the other three squares are empty:
	 * EMPTY, ATTACKER, ATTACKER, EMPTY, ATTACKER, EMPTY or
	 * EMPTY, ATTACKER, EMPTY, ATTACKER, ATTACKER, EMPTY
	 * @param p
	 * @return number of broken threes
	 */
	public int getBrokenThrees(Piece p) {
		return 0;
	}
	
	// -------------
	// Fours Methods
	// -------------
	private int getNumFoursVertical(Piece p) {
		int num = 0;

		for (int i = 0; i <= this.getRows() - 5; i++) {
			for (int j = 0; j < this.getCols(); j++) {

				// Get five pieces in a line
				Piece p1 = board[i][j];
				Piece p2 = board[i+1][j];
				Piece p3 = board[i+2][j];
				Piece p4 = board[i+3][j];
				Piece p5 = board[i+4][j];

				// Check for a "four".
				if (isFour(p, p1, p2, p3, p4, p5)) {
					num++;
				}
			}
		}
		
		return num;
	}

	private int getNumFoursHorizontal(Piece p) {
		int num = 0;

		for (int i = 0; i < this.getRows(); i++) {
			for (int j = 0; j <= this.getCols() - 5; j++) {
				
				// Get five pieces in a line
				Piece p1 = board[i][j];
				Piece p2 = board[i][j+1];
				Piece p3 = board[i][j+2];
				Piece p4 = board[i][j+3];
				Piece p5 = board[i][j+4];
				
				// Check for a "four".
				if (isFour(p, p1, p2, p3, p4, p5)) {
					num++;
				}
			}
		}
		
		return num;
	}

	private int getNumFoursDiagonal(Piece p) {
		int num = 0;
			
		// Check the first diagonal
		for (int i = 0; i <= SIZE - 5; i++) {
			for (int j = 0; j <= SIZE - 5; j++) {

				// Get five pieces in a line
				Piece p1 = board[i][j];
				Piece p2 = board[i+1][j+1];
				Piece p3 = board[i+2][j+2];
				Piece p4 = board[i+3][j+3];
				Piece p5 = board[i+4][j+4];

				// Check for a "four".
				if (isFour(p, p1, p2, p3, p4, p5)) {
					num++;
				}
			}
		}
		
		// Check the second diagonal
		for (int i = 0; i <= SIZE - 5 ; i++) {  // Rows 0..10
			for (int j = 4; j < SIZE; j++) {    // Cols 4..14
				
				// Get five pieces in a line
				Piece p1 = board[i][j];
				Piece p2 = board[i+1][j-1];
				Piece p3 = board[i+2][j-2];
				Piece p4 = board[i+3][j-3];
				Piece p5 = board[i+4][j-4];

				// Check for a "four".
				if (isFour(p, p1, p2, p3, p4, p5)) {
					num++;
				}
			}
		}

		return num;
	}

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
	
	// ----------------------
	// Straight Fours Methods
	// ----------------------
	
	private int getNumStraightFoursVertical(Piece p) {
		int numStraightFours = 0;

		for (int i = 0; i <= this.getRows() - 6; i++) {
			for (int j = 0; j < this.getCols(); j++) {

				Piece p1 = board[i][j];
				Piece p2 = board[i+1][j];
				Piece p3 = board[i+2][j];
				Piece p4 = board[i+3][j];
				Piece p5 = board[i+4][j];
				Piece p6 = board[i+5][j];

				if (isStraightFour(p, p1, p2, p3, p4, p5, p6)) {
					numStraightFours++;
				}
			}
		}		
		return numStraightFours;
	}
	
	private int getNumStraightFoursHorizontal(Piece p) {
		int numStraightFours = 0;

		for (int i = 0; i < this.getRows(); i++) {
			for (int j = 0; j <= this.getCols() - 6; j++) {
				
				Piece p1 = board[i][j];
				Piece p2 = board[i][j+1];
				Piece p3 = board[i][j+2];
				Piece p4 = board[i][j+3];
				Piece p5 = board[i][j+4];
				Piece p6 = board[i][j+5];
				
				if (isStraightFour(p, p1, p2, p3, p4, p5, p6)) {
					numStraightFours++;
				}			
			}
		}
		
		return numStraightFours;
	}
	
	private int getNumStraightFoursDiagonal(Piece p) {
		int numStraightFours = 0;

		// Check the first diagonal
		for (int i = 0; i <= SIZE - 6; i++) {
			for (int j = 0; j <= SIZE - 6; j++) {
				if (board[i][j]     == Piece.EMPTY &&
					board[i+1][j+1] == p &&
					board[i+2][j+2] == p &&
					board[i+3][j+3] == p &&
					board[i+4][j+4] == p &&
					board[i+5][j+5] == Piece.EMPTY) {
					numStraightFours++;
					break;
				}
			}
		}
		
		// Check the second diagonal
		for (int i = 0; i <= SIZE - 6 ; i++) {  // Rows 0..9
			for (int j = 5; j < SIZE; j++) {    // Cols 5..14
				if (board[i][j]    == Piece.EMPTY &&
					board[i+1][j-1] == p &&
					board[i+2][j-2] == p &&
					board[i+3][j-3] == p &&
					board[i+4][j-4] == p &&
					board[i+5][j-5] == Piece.EMPTY) {
					numStraightFours++;
					break;
				}
			}
		}
		
		return numStraightFours;
	}

	private boolean isStraightFour(Piece p, Piece p1, Piece p2, Piece p3, Piece p4, Piece p5, Piece p6) {
		return (p1 == Piece.EMPTY &&
			p2 == p && p3 == p && p4 == p && p5 == p &&
			p6 == Piece.EMPTY);
	}

	// --------------
	// Threes methods
	// --------------
	private int getNumThreesVertical(Piece p) {
		int num = 0;

		for (int i = 0; i <= this.getRows() - 7; i++) {
			for (int j = 0; j < this.getCols(); j++) {

				// Get four pieces in a line
				Piece p1 = board[i][j];
				Piece p2 = board[i+1][j];
				Piece p3 = board[i+2][j];
				Piece p4 = board[i+3][j];
				Piece p5 = board[i+4][j];
				Piece p6 = board[i+5][j];
				Piece p7 = board[i+6][j];

				// Check for a "three"
				if (isThree(p, p1, p2, p3, p4, p5, p6, p7)) {
					num++;
				}
			}
		}
		
		return num;
	}
	
	private int getNumThreesHorizontal(Piece p) {
		int num = 0;

		for (int i = 0; i < this.getRows(); i++) {
			for (int j = 0; j <= this.getCols() - 7; j++) {
				
				// Get four pieces in a line
				Piece p1 = board[i][j];
				Piece p2 = board[i][j+1];
				Piece p3 = board[i][j+2];
				Piece p4 = board[i][j+3];
				Piece p5 = board[i][j+4];
				Piece p6 = board[i][j+5];
				Piece p7 = board[i][j+6];

				// Check for a "three"
				if (isThree(p, p1, p2, p3, p4, p5, p6, p7)) {
					num++;
				}
			}
		}
		
		return num;
	}
	
	private int getNumThreesDiagonal(Piece p) {
		int num = 0;
		
		// Check the first diagonal
		for (int i = 0; i <= SIZE - 7; i++) {
			for (int j = 0; j <= SIZE - 7; j++) {

				// Get five pieces in a line
				Piece p1 = board[i][j];
				Piece p2 = board[i+1][j+1];
				Piece p3 = board[i+2][j+2];
				Piece p4 = board[i+3][j+3];
				Piece p5 = board[i+4][j+4];
				Piece p6 = board[i+5][j+5];
				Piece p7 = board[i+6][j+6];

				// Check for a "four".
				if (isThree(p, p1, p2, p3, p4, p5, p6, p7)) {
					num++;
				}
			}
		}
		
		// Check the second diagonal
		for (int i = 0; i <= SIZE - 7 ; i++) {  // Rows 0..10
			for (int j = 6; j < SIZE; j++) {    // Cols 3..13
				
				// Get five pieces in a line
				Piece p1 = board[i][j];
				Piece p2 = board[i+1][j-1];
				Piece p3 = board[i+2][j-2];
				Piece p4 = board[i+3][j-3];
				Piece p5 = board[i+4][j-4];
				Piece p6 = board[i+5][j-5];
				Piece p7 = board[i+6][j-6];

				// Check for a "four".
				if (isThree(p, p1, p2, p3, p4, p5, p6, p7)) {
					num++;
				}
			}
		}
		return num;
	}
	
	private boolean isThree(Piece p, Piece p1, Piece p2, Piece p3, Piece p4,
							Piece p5, Piece p6, Piece p7) {
		boolean three = false;
		
		if (p1 == Piece.EMPTY && p2 == Piece.EMPTY &&
			p3 == p && p4 == p && p5 == p &&
			p6 == Piece.EMPTY && p7 == Piece.EMPTY) {
			three = true;
		}

		return three;
		/*
		int numEmpty = 0;
		int numPiece = 0;
		if (p1  == p) {
			numPiece++;
		} else if (p1 == Piece.EMPTY) {
			numEmpty++;
		}
		
		if (p2   == p) {
			numPiece++;
		} else if (p2 == Piece.EMPTY) {
			numEmpty++;
		}

		if (p3   == p) {
			numPiece++;
		} else if (p3 == Piece.EMPTY) {
			numEmpty++;
		}

		if (p4   == p) {
			numPiece++;
		} else if (p4 == Piece.EMPTY) {
			numEmpty++;
		}

		if (numPiece == 3 && numEmpty == 1) {
			three = true;
		}
		return three;
		*/
	}

} // End class GomokuBoard
