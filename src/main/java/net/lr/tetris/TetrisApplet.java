
package net.lr.tetris;

import javax.swing.JApplet;

/**
 * Starting point for use of tetris as an applet.
 * This class simply loads the Tetris panel class
 * as its content pane
 */
public class TetrisApplet extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8291800152310127713L;
	private net.lr.tetris.Tetris tetris = null;
	
	/**
	 * This is the default constructor
	 */
	public TetrisApplet() {
		super();		
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	public void init() {
		this.setContentPane(getTetris());
		this.setSize(320, 300);
		this.setLocation(0, 0);
		this.addKeyListener(new java.awt.event.KeyAdapter() { 
			public void keyPressed(java.awt.event.KeyEvent e) {
				tetris.keyPressed(e);
			}
		});
		
		//URL appletURL=this.getCodeBase();
		getTetris().highScores.read();
	}
	
	public void start() {
			super.start();
			//tetris.newGame();
		}
	
	public void stop() {
		super.stop();
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
			tetris.setFocusable(true);
			tetris.setRequestFocusEnabled(true);
		}
		return tetris;
	}
}
