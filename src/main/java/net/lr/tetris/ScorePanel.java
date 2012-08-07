/*
 * Created on 05.01.2004
 *
 */
package net.lr.tetris;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Displays the Highscores
 * 
 * The scores themself are modelled in the ScoreList class
 *
 *  @author chris
 */
public class ScorePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6758718786766456726L;
	private ScoreList scores;

	/**
	 * 
	 */
	public ScorePanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 300);
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
		if (getScores()==null) return;
		Score s;
		for (int c=0;c<scores.size();c++) {
			s=(Score)scores.get(c);
			g.setColor(Color.black);
			g.drawString(s.getName(),10,20+c*20);
			g.drawString(""+s.getScore(),100,20+c*20);
		}
	}

	void setScores(ScoreList scores) {
		this.scores = scores;
	}

	ScoreList getScores() {
		return scores;
	}

}
