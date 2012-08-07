package net.lr.tetris;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;


/**
 * Implements a Panel to display a tetris gameboard
 */
public class GamePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9073308186742942554L;
	private Board board;

	/**
	 * This is the default constructor
	 */
	public GamePanel() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(120, 240);
		this.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
		this.setPreferredSize(new java.awt.Dimension(120,240));
		this.setBackground(new java.awt.Color(237,237,236));
	}
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		g.clearRect(0,0,getWidth(),getHeight());
		if (board!=null){
			int width=board.getWidth();
			int height=board.getHeight();
			for (int y=0;y<height;y++) {
				for (int x=0;x<width;x++) {
					if (board.field[y][x]!=0) {
					g.setColor(Color.red);
					g.fillRect(x*12,y*12,10,10);
					}
				}
			}
		}
	}


	/**
	 * @return
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * @param game
	 */
	public void setBoard(Board game) {
		this.board = game;
	}
	


}
