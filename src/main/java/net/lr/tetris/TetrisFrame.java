package net.lr.tetris;

import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * Starting point for use of Tetris as a standalone
 * Application
 * 
 * Simply loads the Tetris class as it´s content Pane
 * 
 * @author chris
 *
 */
public class TetrisFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2432811889021456787L;
	private net.lr.tetris.Tetris tetris = null;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(
				UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		TetrisFrame tetrisFrame=new TetrisFrame();
		tetrisFrame.tetris.newGame();		
	}
	
	/**
	 * This is the default constructor
	 */
	public TetrisFrame() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setContentPane(getTetris());
		this.setSize(330, 327);
		this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		this.setTitle("Tetris by Christian Schneider");
		this.setVisible(true);
		this.getTetris().setFocusable(true);
		this.getTetris().setRequestFocusEnabled(true);
		getTetris().highScores.read();
	}
	
	public void dispose() {
		super.dispose();
		tetris.setMoverThread(null);
	}
	
	/**
	 * This method initializes tetris
	 * 
	 * @return tetris.Tetris
	 */
	private net.lr.tetris.Tetris getTetris() {
		if(tetris == null) {
			tetris = new net.lr.tetris.Tetris();
		}
		return tetris;
	}
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
