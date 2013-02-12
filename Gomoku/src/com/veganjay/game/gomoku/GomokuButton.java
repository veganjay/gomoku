package com.veganjay.game.gomoku;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.*;

public class GomokuButton extends JButton {
	private static final ImageIcon noIcon
	  				= new ImageIcon(GomokuButton.class.getResource("images/NoPiece.png"));
	
	private static final ImageIcon xIcon
		= new ImageIcon(GomokuButton.class.getResource("images/PlayerPiece.png"));

	private static final ImageIcon oIcon
	= new ImageIcon(GomokuButton.class.getResource("images/ComputerPiece.png"));

  	private static final Insets noMargin = new Insets(0, 0, 0, 0);

  	private int row, col;
  	
	public GomokuButton(int row, int col) {
		this.row = row;
		this.col = col;
		
		setIcon(noIcon);
	    setIconTextGap(0);
	    setMargin(noMargin);
	    setAlignmentX(Component.CENTER_ALIGNMENT);
	    setAlignmentY(Component.CENTER_ALIGNMENT);		
	}
	
	public Dimension getPreferredSize() {
	    return new Dimension(getIcon().getIconWidth() + 8, getIcon().getIconHeight() + 8);
	}
	
	public void setXIcon() {
		setIcon(xIcon);
	}

	public void setOIcon() {
		setIcon(oIcon);
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public String toString() {
		return row + "," + col;
	}
}
