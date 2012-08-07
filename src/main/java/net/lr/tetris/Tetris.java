package net.lr.tetris;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/* 
    Created on 02.01.2004

    Copyright (C) 2004 Christian Schneider

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/**
 * Java Tetris main class
 * 
 * Implements the Controller part of the Tetris game.
 * Reacts on keyboard input and starts a thread for
 * moving the stone down after a certain amount of time.
 *
 * @author chris
 * @since 2004
 */
public class Tetris extends JPanel implements Runnable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -3267887732569843668L;
	private javax.swing.JLabel scoreLabel = null;
	private GamePanel gamePanel = null;
	private GamePanel previewPanel = null;
	private javax.swing.JMenuBar menuBar = null;
	private javax.swing.JMenu jMenu = null;
	private javax.swing.JMenuItem mINew = null;
	private javax.swing.JMenu jMenu1 = null;
	private javax.swing.JLabel levelLabel = null;
	private javax.swing.JLabel statusLine = null;
	ButtonGroup group;
	private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem = null;
	private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1 = null;
	private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem2 = null;

	private javax.swing.JDialog jDialog = null;  //  @jve:visual-info  decl-index=0 visual-constraint="451,24"
	private javax.swing.JMenu jMenu2 = null;
	private javax.swing.JMenuItem jMenuItem = null;
	private net.lr.tetris.ScorePanel scorePanel = null;	

	private boolean isApplet;
	private Thread moverThread;
	Stone currentStone;
	Stone nextStone;
	private int stones;
	private int score;
	private int level;
	Board board;
	boolean gameOver;
	ScoreList highScores;
	
	/**
	 * 
	 */
	public Tetris() {
		super();
		initialize();
	}
	
	/**
	 * moverThread to move the stone down after a certain amount of time
	 */
	public void run() {
		while (moverThread==Thread.currentThread()) {
			try {
				Thread.sleep(70+700/level);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if (!gameOver) {
				moveDown();
			}
		}
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		group = new ButtonGroup();
		setLayout(null);
		add(getScoreLabel(), java.awt.BorderLayout.SOUTH);
		add(getGamePanel(), null);
		add(getPreviewPanel(), null);			
		add(getMenuBar(), null);
		add(getLevelLabel(), null);
		add(getStatusLine(), null);
		this.setSize(302, 353);
		this.setVisible(true);

		this.addKeyListener(new java.awt.event.KeyAdapter() { 
			public void keyPressed(java.awt.event.KeyEvent e) {
				Tetris.this.keyPressed(e);
			}
		});
		
		this.setName("TetrisPane");
		board=new Board(12,22);
		gamePanel.setBoard(board);
		stones=0;
		gameOver=true;
		setScore(0);
		setLevel(1);
		nextStone=new Stone();
		Properties prop = new Properties();
        highScores=new ScoreList();
		setFocusable(true);
		try {
            InputStream inS = this.getClass().getClassLoader().getResourceAsStream("tetris.properties");
            if (inS!=null) {
			prop.load(inS);
            }
            highScores.setBaseURL(new URL(prop.getProperty("tetris.ServerScriptUrl","")));
            getScorePanel().setScores(highScores);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * Moves the stone left, right and down.
	 * Rotates the stone.
	 * 
	 * @param e
	 */
	public void keyPressed(KeyEvent e) {
		if (gameOver) return;
		
		if (e.getKeyCode()==KeyEvent.VK_LEFT) {
			moveLeft();
			redrawGame();    
		}
		if (e.getKeyCode()==KeyEvent.VK_RIGHT) {
			moveRight();
			redrawGame();    
		}
		if (e.getKeyCode()==KeyEvent.VK_UP) {
			rotate();
			redrawGame();
		}				
		if (e.getKeyCode()==KeyEvent.VK_DOWN) {
			setScore(getScore()+5+getLevel());
			moveDown();
		}
	} 

	public void redrawGame() {
		gamePanel.repaint();
	}

	/**
	 * Starts a new game
	 * 
	 * Resets the score, clears the board and creates a new stone.
	 * Starts the moverThread.
	 *
	 */
	public void newGame() {	
		stones=0;	
		setScore(0);
		setLevel(1);
		statusLine.setText("");
		gamePanel.getBoard().clearField();
		createStone();
		redrawGame();
		gameOver=false;
		moverThread = new Thread ( this ) ;
		moverThread.start();
	}
	
	/**
	 * Creates a new randoem stone and places it at the top of the board.
	 * Returns true if the stone could be placed and false if there was no free space.
	 * 
	 * @return
	 */
	public boolean createStone() {
		stones++;
		if (stones>=10+level*2) {
			setLevel(getLevel()+1);
			stones=0;
		}
		currentStone=nextStone;
		currentStone.setBoard(board);
		currentStone.posX=3;
		currentStone.posY=-1;

		nextStone=new Stone();
		previewPanel.setBoard(nextStone);
		previewPanel.repaint();

		if (currentStone.mayPlace(currentStone.posX,currentStone.posY)) {
			currentStone.place();
			return true;
		} else {
			return false;
		}
	}

	public void moveLeft() {
		currentStone.take();
		if (currentStone.mayPlace(currentStone.posX-1,currentStone.posY)) {			
			currentStone.posX-=1;
		}
		currentStone.place();
	}
	
	public void moveRight() {
		currentStone.take();
		if (currentStone.mayPlace(currentStone.posX+1,currentStone.posY)) {			
			currentStone.posX+=1;
		}
		currentStone.place();
	}
	
	/**
	 * Try to move down the stone
	 * 
	 * Places the stone one line below it´s current position if possible.
	 * If the stone hits a filled block the board is cleared of full lines and the score
	 * is increased.
	 * 
	 * Returns if the stone was placed succesfully.
	 * 
	 * @return
	 */
	public synchronized boolean moveDown() {
		currentStone.take();
		boolean mayPlace=currentStone.mayPlace(currentStone.posX,currentStone.posY+1);
		if (mayPlace) {
			currentStone.posY+=1;
		}
		currentStone.place();
		if (!mayPlace) {
			int linesRemoved=board.removeFullLines();
			setScore(getScore() + 1000*level*linesRemoved);
			if (!createStone()) gameOver();			
		}
		redrawGame();
		return mayPlace;
	}
	
	/**
	 * Rotates the stone to the left if possible and places it there.
	 * 
	 * @return
	 */
	public boolean rotate() {
		currentStone.take();
		currentStone.rotate();
		if (!currentStone.mayPlace(currentStone.posX,currentStone.posY)) {
			currentStone.rotate();
			currentStone.rotate();
			currentStone.rotate();
		}
		currentStone.place();
		return true;
		
	}
	
	/**
	 * Called when the game is over
	 * 
	 * Checks if the player gets into the highscore list.
	 * If yes the name is queried and the score added.
	 * Stops the moverThread.
	 */
	public void gameOver() {
		moverThread=null;
		gameOver=true;
		statusLine.setText("Game Over");
		if (score>highScores.getMinScore()) {
			String playerName=JOptionPane.showInputDialog("Dein Name","");
			if (playerName!=null) {
				highScores.upload(new Score(playerName,score));
			}
		}
		getJDialog().setVisible(true);		 
	}


	
	
	/*********************************************************/
	/* Only VE created code and getters/setters below         */
	/*********************************************************/
	
	/**
	 * This method initializes scoreLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	javax.swing.JLabel getScoreLabel() {
		if(scoreLabel == null) {
			scoreLabel = new javax.swing.JLabel();
			scoreLabel.setSize(106, 19);
			scoreLabel.setText("");
			scoreLabel.setLocation(179, 178);
		}
		return scoreLabel;
	}
	/**
	 * This method initializes gamePanel
	 * 
	 * @return GamePanel
	 */
	private GamePanel getGamePanel() {
		if(gamePanel == null) {
			gamePanel = new GamePanel();
			gamePanel.setLayout(null);
			gamePanel.setSize(144, 264);
			gamePanel.setLocation(13, 26);
		}
		return gamePanel;
	}
	/**
	 * This method initializes previewPanel
	 * 
	 * @return GamePanel
	 */
	private GamePanel getPreviewPanel() {
		if(previewPanel == null) {
			previewPanel = new GamePanel();
			previewPanel.setBounds(178, 26, 60, 60);
		}
		return previewPanel;
	}

	
	/**
	 * This method initializes menuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
	private javax.swing.JMenuBar getMenuBar() {
		if(menuBar == null) {
			menuBar = new javax.swing.JMenuBar();
			menuBar.add(getJMenu());
			menuBar.add(getJMenu1());
			menuBar.add(getJMenu2());
			menuBar.setBounds(0, 1, 314, 18);
		}
		return menuBar;
	}
	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private javax.swing.JMenu getJMenu() {
		if(jMenu == null) {
			jMenu = new javax.swing.JMenu();
			jMenu.add(getMINew());
			jMenu.setText("Game");
		}
		return jMenu;
	}
	/**
	 * This method initializes mINew
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getMINew() {
		if(mINew == null) {
			mINew = new javax.swing.JMenuItem();
			mINew.setActionCommand("new");
			mINew.setName("new game");
			mINew.setText("new game");
			mINew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.Event.ALT_MASK, false));
			mINew.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					newGame();
				}
			});
		}
		return mINew;
	}
	
	/**
	 * This method initializes jMenu1
	 * 
	 * @return javax.swing.JMenu
	 */
	private javax.swing.JMenu getJMenu1() {
		if(jMenu1 == null) {
			jMenu1 = new javax.swing.JMenu();
			jMenu1.add(getJRadioButtonMenuItem());
			jMenu1.add(getJRadioButtonMenuItem1());
			jMenu1.add(getJRadioButtonMenuItem2());
			jMenu1.setText("Level");
			jMenu1.setVisible(false);
		}
		return jMenu1;
	}
	/**
	 * This method initializes levelLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLevelLabel() {
		if(levelLabel == null) {
			levelLabel = new javax.swing.JLabel();
			levelLabel.setBounds(179, 141, 106, 19);
			levelLabel.setText("");
		}
		return levelLabel;
	}
	/**
	 * @return
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param i
	 */
	public void setLevel(int i) {
		level = i;
		levelLabel.setText("Level: "+level);
	}

	/**
	 * This method initializes statusLine
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getStatusLine() {
		if(statusLine == null) {
			statusLine = new javax.swing.JLabel();
			statusLine.setBounds(179, 211, 106, 19);
			statusLine.setText("");
		}
		return statusLine;
	}
	/**
	 * This method initializes jRadioButtonMenuItem
	 * 
	 * @return javax.swing.JRadioButtonMenuItem
	 */
	private javax.swing.JRadioButtonMenuItem getJRadioButtonMenuItem() {
		if(jRadioButtonMenuItem == null) {
			jRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
			jRadioButtonMenuItem.setText("1");
			jRadioButtonMenuItem.setSelected(true);
			group.add(jRadioButtonMenuItem);
		}
		return jRadioButtonMenuItem;
	}
	/**
	 * This method initializes jRadioButtonMenuItem1
	 * 
	 * @return javax.swing.JRadioButtonMenuItem
	 */
	private javax.swing.JRadioButtonMenuItem getJRadioButtonMenuItem1() {
		if(jRadioButtonMenuItem1 == null) {
			jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
			jRadioButtonMenuItem1.setText("2");
			group.add(jRadioButtonMenuItem1);
		}
		return jRadioButtonMenuItem1;
	}
	/**
	 * This method initializes jRadioButtonMenuItem2
	 * 
	 * @return javax.swing.JRadioButtonMenuItem
	 */
	private javax.swing.JRadioButtonMenuItem getJRadioButtonMenuItem2() {
		if(jRadioButtonMenuItem2 == null) {
			jRadioButtonMenuItem2 = new javax.swing.JRadioButtonMenuItem();
			jRadioButtonMenuItem2.setText("3");
			group.add(jRadioButtonMenuItem2);
		}
		return jRadioButtonMenuItem2;
	}
	/**
	 * This method initializes jDialog
	 * 
	 * @return javax.swing.JDialog
	 */
	private javax.swing.JDialog getJDialog() {
		if(jDialog == null) {
			jDialog = new javax.swing.JDialog();
			jDialog.setContentPane(getScorePanel());
			jDialog.setSize(192, 267);
			jDialog.setTitle("Highscores");
			jDialog.setModal(true);
		}
		return jDialog;
	}
	/**
	 * This method initializes jMenu2
	 * 
	 * @return javax.swing.JMenu
	 */
	private javax.swing.JMenu getJMenu2() {
		if(jMenu2 == null) {
			jMenu2 = new javax.swing.JMenu();
			jMenu2.add(getJMenuItem());
			jMenu2.setText("Info");
		}
		return jMenu2;
	}
	/**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private javax.swing.JMenuItem getJMenuItem() {
		if(jMenuItem == null) {
			jMenuItem = new javax.swing.JMenuItem();
			jMenuItem.setText("Highscores");
			jMenuItem.addActionListener(new java.awt.event.ActionListener() { 
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					getJDialog().setVisible(true);
				}
			});
		}
		return jMenuItem;
	}
	/**
	 * This method initializes scorePanel
	 * 
	 * @return tetris.ScorePanel
	 */
	private net.lr.tetris.ScorePanel getScorePanel() {
		if(scorePanel == null) {
			scorePanel = new net.lr.tetris.ScorePanel();
		}
		return scorePanel;
	}
	
	/**
	 * @return
	 */
	public Thread getMoverThread() {
		return moverThread;
	}

	/**
	 * @param thread
	 */
	public void setMoverThread(Thread thread) {
		moverThread = thread;
	}

	/**
	 * @return
	 */
	public boolean isApplet() {
		return isApplet;
	}

	/**
	 * @param b
	 */
	public void setApplet(boolean b) {
		isApplet = b;
	}
	
	public void setScore(int score) {
		this.score = score;
		scoreLabel.setText("Score: "+score);
	}

	public int getScore() {
		return score;
	}

}  //  @jve:visual-info  decl-index=0 visual-constraint="78,35"
