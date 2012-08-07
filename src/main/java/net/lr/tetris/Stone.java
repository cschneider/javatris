package net.lr.tetris;

import java.util.Random;


/**
 * Models a stone in the Tetris game
 * 
 * A stone can be place on the Board and taken from it.
 * It can be rotated.
 * 
 * The Constant stones contains the standard tetris stones.
 * 
 * The moving of a stone is modeled inside the Tetris class as it
 * has some important effects on the game.
 */
public class Stone extends Board {
	int posX;
	int posY;
	int color;
	int newField[][];
	Board board;
	
	static int stones[][][]=
	{
		{
			{0,0,0,0,0},
			{0,0,0,0,0},
			{1,1,1,1,0},
			{0,0,0,0,0},
			{0,0,0,0,0}
		},
		{
			{0,0,0,0,0},
			{0,1,1,0,0},
			{0,1,1,0,0},
			{0,0,0,0,0},
			{0,0,0,0,0}
		},
		{
			{0,0,0,0,0},
			{0,1,1,1,0},
			{0,0,0,1,0},
			{0,0,0,0,0},
			{0,0,0,0,0},
		},
		{
			{0,0,0,0,0},
			{0,0,0,1,0},
			{0,1,1,1,0},
			{0,0,0,0,0},
			{0,0,0,0,0}
		},
		{
			{0,0,0,0,0},
			{0,1,1,0,0},
			{0,0,1,1,0},
			{0,0,0,0,0},
			{0,0,0,0,0}
		},
		{
			{0,0,0,0,0},
			{0,0,1,1,0},
			{0,1,1,0,0},
			{0,0,0,0,0},
			{0,0,0,0,0}
		},
		{
			{0,0,0,0,0},
			{0,0,0,0,0},
			{0,1,1,1,0},
			{0,0,1,0,0},
			{0,0,0,0,0}
		}
	};
	
	/**
	 * 
	 */
	public Stone() {
		super(5,5);
		newField = new int[5][5];
		color=1;
		setRandomType();
		posX=0;
		posY=0;
	}
	
	public void setType(int nr) {
		for (int y=0;y<field.length;y++) {
			for (int x=0;x<field[y].length;x++) {
				field[y][x]=stones[nr][y][x];
			}
		}
	}
	
	public void setRandomType() {
		Random r = new Random();
		setType(r.nextInt(stones.length));
	}
	
	public void place() {
		if (board==null) return;
		for (int y=0;y<field.length;y++) {
			for (int x=0;x<field[y].length;x++) {
				int val=field[y][x];
				if (val!=0) board.setField(posX+x,posY+y,color);
			}
		}
	}
	
	public void take() {
		if (board==null) return;
		for (int y=0;y<field.length;y++) {
			for (int x=0;x<field[y].length;x++) {
				int val=field[y][x];
				if (val!=0) board.setField(posX+x,posY+y,0);
			}
		}
	}

	public boolean mayPlace(int nX, int nY) {
		if (board==null) return false;
		for (int y=0;y<field.length;y++) {
			for (int x=0;x<field[y].length;x++) {
				int val=field[y][x];
				if (val!=0 && board.getField(nX+x,nY+y)!=0) return false;
			}
		}
		return true;		
	}
	
	public void rotate() {
		for (int y=0;y<field.length;y++) {
			for (int x=0;x<field[y].length;x++) {
				newField[4-x][y]=field[y][x];
			}
		}
		for (int y=0;y<field.length;y++) {
			for (int x=0;x<field[y].length;x++) {
				field[y][x]=newField[y][x];
			}
		}
	}
	
	/**
	 * @return
	 */
	public int getColor() {
		return color;
	}

	/**
	 * @return
	 */
	public int[][] getField() {
		return field;
	}

	/**
	 * @param i
	 */
	public void setColor(int i) {
		color = i;
	}

	/**
	 * @param is
	 */
	public void setField(int[][] is) {
		field = is;
	}

	/**
	 * @return
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * @param board
	 */
	public void setBoard(Board board) {
		this.board = board;
	}

}
