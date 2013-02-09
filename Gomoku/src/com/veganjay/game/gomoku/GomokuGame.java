package com.veganjay.game.gomoku;

import com.veganjay.game.gomoku.GomokuBoard.Piece;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GomokuGame {

	private GomokuBoard board;
	
	public enum Player { HUMAN, COMPUTER };
		
	// Member variables
	private BufferedReader input;
	
	private Player currentPlayer;
	private Player startPlayer;
	
	private Piece  humanPiece;
	private Piece  computerPiece;	
	
	private GomokuAI	   ai;
	
	/*
	 * Initialize the input reader and create the board.
	 */
	public GomokuGame() {
		this.input = new BufferedReader(new InputStreamReader(System.in));
		this.board = new GomokuBoard();
	}		

	private int getChoice(String question) {
		String choice;
		int    col;
		
		// Display the choice
		System.out.print(question);
		
		// Read from input
		choice = getInput();
		System.out.println();
		
		try {
			col = Integer.parseInt(choice);
		} catch (NumberFormatException e) {
			System.out.println("Enter a valid value (0-14)");
			return getChoice(question);
		}
		return col;
	}
	
	
	private void playHumanTurn() {
		// Get the choice
		int row = getChoice("Which row? ");
		int col = getChoice("Which column? ");
		
		if (board.addPiece(row, col, getHumanPiece()) == false) {
			System.out.println("Enter a valid move");
			playHumanTurn();
		}
		
		System.out.println("You moved to (" + row + "," + col + ")" );

	}

	// For two player mode
	/*
	
	private void playComputerTurn() {
		// Get the choice
		int row = getChoice("Which row? ");
		int col = getChoice("Which column? ");
		
		if (board.addPiece(row, col, super.getComputerPiece()) == false) {
			playComputerTurn();
		}
		
		// Display the board
		board.printBoard();
	}
	*/



	/**
	 * @return the human piece
	 */
	public Piece getHumanPiece() {
		return humanPiece;
	}

	/**
	 * @return the computer piece
	 */
	public Piece getComputerPiece() {
		return computerPiece;
	}



	private void initialize() {
		board.reset();
		if (getStartPlayer() == Player.HUMAN) {
			humanPiece    = Piece.X;
			computerPiece = Piece.O;
		} else {
			humanPiece    = Piece.O;
			computerPiece = Piece.X;

			board.addPiece(7, 7, computerPiece);
			this.switchPlayers();
		}
		ai = new GomokuAI(computerPiece);
		
		board.printBoard();

	}
	
	/**
	 * Clear the screen
	 */
	private void clearScreen() {
		for (int i = 0; i < 30; i++) {
			System.out.println();
		}
	}
	
	/**
	 * @return a line of input from the user
	 */
	private String getInput() {
		String line = "";
		try {
			return this.input.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line;		
	}

	/**
	 * Ask the user if she wants to go first
	 */
	private void askStartPlayer() {
		System.out.print("Do you want to go first? [y] ");
		
		String choice = this.getInput();

		if (choice.equalsIgnoreCase("n") || choice.equalsIgnoreCase("no")) {
			startPlayer = Player.COMPUTER;
		} else {
			startPlayer = Player.HUMAN;
		}
		
		currentPlayer = startPlayer;
	}

	/**
	 * @return the start player
	 */
	private Player getStartPlayer() {
		return startPlayer;
	}
	private void switchPlayers() {
		if (currentPlayer == Player.COMPUTER) {
			currentPlayer = Player.HUMAN;
		} else {
			currentPlayer = Player.COMPUTER;
		}
	}
	
	/**
	 * Ask the user if she wants to play again
	 * @return true if playing again, false if quitting
	 */
	private boolean askPlayAgain() {
		boolean playAgain = true;
		System.out.print("Do you want to play again? [y] ");
		String choice = this.getInput();
		if (choice.equalsIgnoreCase("n") || choice.equalsIgnoreCase("no")) {
			playAgain = false;
		}
		return playAgain;
	}		

	/**
	 * Display the winner, or tie game.
	 */
	private void displayWinner() {
		if (isComputerWinner()) {
			System.out.println("Computer wins!");
		} else if (isHumanWinner()) {
			System.out.println("You win!");
		} else {
			System.out.println("Tie Game!");
		}
	}
	
	private void playComputerTurn() {
		// Get the computer move from the AI
		Move move = ai.getMove(board);
		
		if (move != null) {
			System.out.println("Computer moves to " + move);
			board.addPiece(move.getRow(), move.getCol(), computerPiece);
		} else {
			System.out.println("move was null!");
		}
	}

	
	/**
	 * @return true if there is a winner or it is a tie
	 */
	private boolean isFinished() {
		boolean done = false;
		
		if (isComputerWinner() || isHumanWinner()) {
			done = true;
		} else if (board.isFull()) {
			done = true;
		}
		return done;
	}

	/**
	 * @return true if the computer won
	 */
	private boolean isComputerWinner() {
		return board.isWinner(computerPiece);
	}

	/**
	 * @return true if the human won
	 */
	private boolean isHumanWinner() {
		return board.isWinner(humanPiece);
	}
	
	/**
	 * Run the main game loop:
	 * - Initialize the board
	 * - Ask for start player
	 * - Run the game
	 * - Ask if the user wants to play again
	 */
	public void mainLoop() {		
		do {
			askStartPlayer();			
			initialize();

			// Keep playing until game is finished
			while (!isFinished()) {				
				if (currentPlayer.equals(Player.HUMAN)) {
					playHumanTurn();
				} else {
					playComputerTurn();
				}				
				
				// Display the board
				board.printBoard();
				
				// Switch players
				switchPlayers();
			}
			// Display the winner
			displayWinner();
		} while (askPlayAgain());
	}
	
	/**
	 * Main method - great the Gomoku game and run it
	 * @param args unused
	 */
	public static void main(String [] args) {
		GomokuGame game = new GomokuGame();
		game.mainLoop();
	}
} // End class GomokuGame
