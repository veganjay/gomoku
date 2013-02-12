package test.com.veganjay.game.gomoku;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import com.veganjay.game.gomoku.GomokuBoard;
import com.veganjay.game.gomoku.GomokuBoard.Piece;
import com.veganjay.game.gomoku.GomokuGameNode;
import com.veganjay.game.gomoku.Move;

public class TestGomokuGameNode {

	@Test
	public void testGetAdjacentMoves() {
		GomokuBoard board = new GomokuBoard();
		
		board.addPiece(7, 6, Piece.X);
		board.addPiece(7, 7, Piece.X);
		board.addPiece(6, 6, Piece.O);

		GomokuGameNode node = new GomokuGameNode(board, Piece.X);

		Set<Move> moves = node.getAdjacentMoves();

		System.out.println("Adjacent Moves:" + moves.size());
		System.out.print("{");		
		for (Move move : moves) {
			System.out.print("(");
			System.out.print(move);
			System.out.print(")");
			System.out.print(",");
		}
		System.out.println("}");		
		
	}

}
