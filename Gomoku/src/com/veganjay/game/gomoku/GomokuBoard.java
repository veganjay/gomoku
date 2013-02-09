package com.veganjay.game.gomoku;


public class GomokuBoard {
	
	public static enum Piece { INVALID, EMPTY, X, O };

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

	public Piece getPiece(int row, int col) {
		Piece p = Piece.INVALID;
		
		if (isValidRow(row) && isValidColumn(col)) {
			p = board[row][col];
		}
			
		return p;
	}
	public boolean isValidRow(int row) {
		return (row >= 0 && row < SIZE);
	}

	public boolean isValidColumn(int column) {
		return (column >= 0 && column < SIZE);
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

} // End class GomokuBoard
