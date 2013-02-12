package com.veganjay.game.gomoku;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.veganjay.game.gomoku.GomokuBoard.Piece;
import com.veganjay.game.gomoku.GomokuGame.Player;

public class GomokuGameGUI implements ActionListener {

	private GomokuBoard board;
	private GomokuAI	   ai;
	
	public enum Player { HUMAN, COMPUTER };
	
	private Player currentPlayer;
	private Player startPlayer;
	
	private Piece  humanPiece;
	private Piece  computerPiece;	
	
	// TODO: Create my own graphic images
	private JFrame frame;
	private GomokuButton buttons[][];
	
	public GomokuGameGUI() {
		frame = new JFrame("Gomoku");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(createBoard());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		this.initGame();
	}
	
	private void initGame() {
		board = new GomokuBoard();
		
		// TODO: Reset graphics
		currentPlayer = Player.HUMAN;
		startPlayer   = Player.HUMAN;
		
		humanPiece    = Piece.X;
		computerPiece = Piece.O;
		
		ai = new GomokuAI(computerPiece);
	}
	
	private JPanel createBoard() {
		JPanel panel = new JPanel(new GridLayout(GomokuBoard.SIZE, GomokuBoard.SIZE, 0, 0));
		buttons = new GomokuButton[GomokuBoard.SIZE][GomokuBoard.SIZE];
		
		int pos = 0;
				
		for (int i = GomokuBoard.SIZE - 1; i >= 0; i--) {
			for (int j = 0; j < GomokuBoard.SIZE; j++) {
				buttons[i][j] = new GomokuButton(i,j);
				buttons[i][j].addActionListener(this);
				panel.add(buttons[i][j]);
				pos++;
			}
		}		
		
		return panel;
	}	
	
	public static void main(String [] args) {
		GomokuGameGUI gui = new GomokuGameGUI();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (currentPlayer == Player.HUMAN) {
			GomokuButton b = (GomokuButton) e.getSource();
			System.out.println(b);			

			int row = b.getRow();
			int col = b.getCol();
			
			// Check for valid move
			if (!board.isOccupied(row, col)) {
				board.addPiece(row, col, humanPiece);
				b.setXIcon();
				
				if (this.isFinished()) {
					System.out.println("You win!");
				} else {
					Move move = ai.getMove(board);
					System.out.println("Computer moves to " + move);
					row = move.getRow();
					col = move.getCol();
					
					board.addPiece(row, col, computerPiece);

					GomokuButton b2 = buttons[row][col];
					if (b2 != null) {
						b2.setOIcon();
					} else {
						System.out.println("b2 was null");
					}
				}
			}
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
}
