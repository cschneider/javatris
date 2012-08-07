/*
 * Created on 06.01.2004
 *
 */
package net.lr.tetris;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Implements a highscore table.
 * 
 * Supports adding a new score a the correct location in the table and checking
 * if a score makes it into the list.
 * 
 * @author chris
 * 
 */
public class ScoreList extends ArrayList<Score> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1481294261049397840L;

	private int maxSize;

	private URL baseURL;

	public ScoreList() {
		super();
		maxSize = 10;
		baseURL = null;
	}

	/**
	 * 
	 * @param maxSize
	 */
	public ScoreList(int maxSize) {
		super();
		this.maxSize = maxSize;
		baseURL = null;
	}

	/**
	 * Adds the score s into the highscore list. If the score is too low it
	 * won´t be added.
	 * 
	 * @param s
	 * @return
	 */
	public boolean add(Score s) {
		int c;
		for (c = 0; c < size() && s.getScore() <= ((Score) get(c)).getScore(); c++) {
		}
		if (c < maxSize) {
			add(c, s);
		}
		return true;
	}

	public void upload(Score s) {
		try {
			URL fullURL = new URL(getBaseURL() + "?score=" + s.getScore()
					+ "&name=" + s.getName());
			read(fullURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the highscore list from an XML file
	 * 
	 * @param url
	 */
	public void read(URL url) {
		clear();
		try {
			InputStream is = url.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = "";

			while ((line = br.readLine()) != null) {
				StringTokenizer tok = new StringTokenizer(line, ",");
				String name = tok.nextToken();
				int score = Integer.valueOf(tok.nextToken());
				add(new Score(name, score));
			}
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void read() {
		read(baseURL);
	}

	/**
	 * Writes the highscore list into an XML file
	 * 
	 * @param fileName
	 */
	public void write(String fileName) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
			for (int c = 0; c < size(); c++) {
				Score score = (Score) get(c);
				bw.write(score.getName() + "," + score.getScore()+ "\n");
			}			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	public int getMaxSize() {
		return maxSize;
	}

	/**
	 * @param i
	 */
	public void setMaxSize(int i) {
		maxSize = i;
	}

	/**
	 * @return
	 */
	public int getMinScore() {
		int s = size();
		if (s >= maxSize) {
			return ((Score) get(size() - 1)).getScore();
		} else {
			return 0;
		}
	}

	/**
	 * @return
	 */
	public URL getBaseURL() {
		return baseURL;
	}

	/**
	 * @param url
	 */
	public void setBaseURL(URL url) {
		baseURL = url;
	}

}
