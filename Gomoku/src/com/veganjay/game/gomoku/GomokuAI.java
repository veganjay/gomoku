package com.veganjay.game.gomoku;

import java.util.Set;

import com.veganjay.game.gomoku.GomokuBoard.Piece;

public class GomokuAI {

	// Constants
	public static final int MINMAX_DEPTH = 2;
	
	// Member variables
	private Piece computerPiece;
	private Piece playerPiece;
	
	// Create a simple logger
	private Logger logger = new Logger("AI");
	
	/**
	 * Initialize the AI
	 * @param computerPiece the piece the computer is playing (X or O)
	 */
	public GomokuAI(Piece computerPiece) {
		this.computerPiece = computerPiece;
		
		if (computerPiece == Piece.X) {
			this.playerPiece = Piece.O;
		} else {
			this.playerPiece = Piece.X;
		}
	}
	
	private int minimax(GomokuGameNode node, int depth, int alpha, int beta, boolean needMax) {
		if (depth <= 0 || node.isTerminalNode()) {
			return node.getObjectiveValue(computerPiece);
		}

		for (GomokuGameNode child : node.getChildren()) {
			int score = minimax(child, depth-1, alpha, beta, !needMax);

			if (needMax) {  // Do Max
				if (score > alpha) {
					alpha = score;
				}
				if (beta <= alpha) {
					break;
				}
			} else { // Do Min
				if (score < beta) {
					beta = score;
				}
				if (beta <= alpha) {
					break;
				}
			}
		}
		
		return needMax ? alpha : beta;
	}
	
	/**
	 * Given a game situation, determine the best move
	 * @param board game board
	 * @return a move (row, col)
	 */
	public Move getMove(GomokuBoard board) {
		Move move = null;
		
		int score = 0;
		int bestScore = Integer.MIN_VALUE;
		GomokuGameNode bestMove = null;
		GomokuGameNode node = new GomokuGameNode(board, computerPiece);
		
		Set<GomokuGameNode> children = node.getChildren();

		logger.debug("getMove() entered.");
		for (GomokuGameNode child : children) {
			// Use Minimax to get computer move
			score = this.minimax(child, MINMAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
			
			// Display the move and score, for debugging
			logger.debug("Move:" + child + ", score=" + score);
			
			// Chose the best move
			if (score > bestScore) {
				bestScore = score;
				bestMove  = child;
			}
		}

		if (bestMove != null) {
			logger.debug("bestMove " + bestMove.getMove() + ", bestScore = " + bestScore);
			move = bestMove.getMove();
		} else {
			System.err.println("bestMove was null!");
		}
		return move;
	}
} // End class GomokuAI
