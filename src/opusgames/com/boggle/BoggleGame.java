package opusgames.com.boggle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import opusgames.com.boggle.delegates.BogglePrintDelegate;
import opusgames.com.boggle.dictionary.BoggleDictionaryControl;


/**
 * This version of a Boggle solver first stores the dictionary (read from a file of line separated words) 
 * into a tree structure that can quickly parse if a string is in the dictionary.
 * 
 * @author Douglas Lanford
 */
public class BoggleGame {
	
	public static int BOARD_SIZE = 4;
	public static int MAX_WORD_LENGTH = BoggleGame.BOARD_SIZE * BoggleGame.BOARD_SIZE;
	
	
	/**
	 * Stores a single element of a boggle board
	 */
	private class BoggleElement {
		
		private char letter;
		private int x, y;
		private boolean wasVisited = false;
		
		
		public BoggleElement(char letter, int x, int y) {
			this.letter = letter;
			this.x = x;
			this.y = y;
		}
		
		
		public char getLetter() {
			return this.letter;
		}
		
		
		public boolean wasVisited() {
			return this.wasVisited;
		}
		
		
		public void setWasVisited(boolean wasVisited) {
			this.wasVisited = wasVisited;
		}
		
		
		/**
		 * look at surrounding boggle elements on the boggle board for word paths
		 */
		public void parseNeighbors(BoggleDictionaryControl.DictionaryElement dictionaryElement) {
			String word = dictionaryElement.getWord();
			if (word != null) {
				// found a word during our parsing of the dictionary.. add it to the output list.
				BoggleGame.this.wordList.add(word);
			}
			
			this.setWasVisited(true);

			for (int curY = this.y - 1; curY <= this.y + 1; curY++) {
				if (curY < 0 || curY >= BoggleGame.BOARD_SIZE) {
					// off the edge of the board
					continue;
				}
				
				for (int curX = this.x - 1; curX <= this.x + 1; curX++) {
					if ((curX == this.x && curY == this.y) 
						|| curX < 0 || curX >= BoggleGame.BOARD_SIZE) {
						// off the edge of the board
						continue;
					}
					
					BoggleElement neighborElement = BoggleGame.this.getBoggleElementAtPos(curX, curY);
					if (neighborElement == null || neighborElement.wasVisited()) {
						// null element, or this neighbor element has been previously visited
						continue;
					}
					
					BoggleDictionaryControl.DictionaryElement nextDictionaryElement = dictionaryElement.getNextElementForChar(neighborElement.getLetter());
					if (nextDictionaryElement == null) {
						// word path through dictionary ends
						continue;
					}

					neighborElement.parseNeighbors(nextDictionaryElement);
				}
			}
			
			this.setWasVisited(false);
		}
	}


	BogglePrintDelegate printDelegate = null;
	
	BoggleDictionaryControl dictionaryControl = null;
	
	List<List<BoggleElement>> boggleBoard = null;
	List<String> wordList = new ArrayList<String>();
	
	
	/**
	 * Boggle Game setup and play class
	 * 
	 * @param printDelegate
	 */
	public BoggleGame(BogglePrintDelegate printDelegate, BoggleDictionaryControl dictionaryControl) {
		this.printDelegate = printDelegate;
		this.dictionaryControl = dictionaryControl;
	}

	
	/**
	 * generate a random boggle board, BOARD_SIZE x BOARD_SIZE in size
	 */
	public void createRandomBoard() {
		this.printDelegate.clear();
		
		Random rand = new Random();

		this.boggleBoard = new ArrayList<List<BoggleElement>>();
		
		for (int y = 0; y < BOARD_SIZE; y++) {
			List<BoggleElement> boggleRow = new ArrayList<BoggleElement>();
			this.boggleBoard.add(boggleRow);
			
			for (int x = 0; x < BOARD_SIZE; x++) {
				boggleRow.add(new BoggleElement((char)(rand.nextInt(26) + 'a'), x, y));
			}
		}
	}
	
	
	/**
	 * generate a test 4x4 boggle board (has 16 char word covering entire puzzle: "anesthesiologist")
	 */
	public void createTestBoard() {
		this.printDelegate.clear();
		
		this.boggleBoard = new ArrayList<List<BoggleElement>>();

		List<BoggleElement> boggleRow = new ArrayList<BoggleElement>();
		boggleRow.add(new BoggleElement('a', 0, 0));
		boggleRow.add(new BoggleElement('n', 1, 0));
		boggleRow.add(new BoggleElement('t', 2, 0));
		boggleRow.add(new BoggleElement('s', 3, 0));
		this.boggleBoard.add(boggleRow);

		boggleRow = new ArrayList<BoggleElement>();
		boggleRow.add(new BoggleElement('h', 0, 1));
		boggleRow.add(new BoggleElement('t', 1, 1));
		boggleRow.add(new BoggleElement('e', 2, 1));
		boggleRow.add(new BoggleElement('i', 3, 1));
		this.boggleBoard.add(boggleRow);

		boggleRow = new ArrayList<BoggleElement>();
		boggleRow.add(new BoggleElement('e', 0, 2));
		boggleRow.add(new BoggleElement('s', 1, 2));
		boggleRow.add(new BoggleElement('s', 2, 2));
		boggleRow.add(new BoggleElement('g', 3, 2));
		this.boggleBoard.add(boggleRow);

		boggleRow = new ArrayList<BoggleElement>();
		boggleRow.add(new BoggleElement('i', 0, 3));
		boggleRow.add(new BoggleElement('o', 1, 3));
		boggleRow.add(new BoggleElement('l', 2, 3));
		boggleRow.add(new BoggleElement('o', 3, 3));
		this.boggleBoard.add(boggleRow);
	}
	

	/**
	 * Return a boggle element from the boggle board
	 * 
	 * @param x
	 * @param y
	 * @return the boggle element at position (x, y) in the boggle board 
	 */
	private BoggleElement getBoggleElementAtPos(int x, int y) {
		try {
			List<BoggleElement> boggleRow = this.boggleBoard.get(y);
			return boggleRow.get(x);
		}
		catch (IndexOutOfBoundsException ex) {
			this.printDelegate.printf("ERROR - getBoggleElementAtPos(%d, %d), index out of range\n", x, y);
			return null;
		}
	}

	
	/**
	 * print out a list of all words in the current boggle board
	 */
	public void playBoggle() {
		// clear the word list
		this.wordList.clear();
		
		this.printDelegate.println("==== PLAY BOGGLE! ====");
		
		// display the board
		this.printDelegate.println("Boggle Board:");
		for (List<BoggleElement> boggleRow : this.boggleBoard) {
			for (BoggleElement element : boggleRow) {
				this.printDelegate.print(new Character(element.getLetter()).toString());
			}
			this.printDelegate.println();
		}
		this.printDelegate.println("-------------\n");
		
		// parse the board
		for (List<BoggleElement> boggleRow : this.boggleBoard) {
			for (BoggleElement element : boggleRow) {
				element.parseNeighbors(this.dictionaryControl.getDictionary().getNextElementForChar(element.getLetter()));
			}
		}
		
		// sort the output word list
		Collections.sort(this.wordList);
		
		// remove duplicate words
		List<Integer> removalList = new ArrayList<Integer>();
		for (int i = 0; i < this.wordList.size() - 1; i++) {
			String cur = this.wordList.get(i);
			String next = this.wordList.get(i + 1);
			
			if (cur.contentEquals(next)) {
				removalList.add(i + 1);
			}
		}
		
		for (int i = removalList.size() - 1; i >= 0; i--) {
			Integer index = removalList.get(i);
			this.wordList.remove(index.intValue());
		}
		
		// finally, sort by word length (since Boggle scoring is by word length)
		List<List<String>> lengthArray = new ArrayList<List<String>>();
		for (int i = 3; i <= BoggleGame.MAX_WORD_LENGTH; i++) {
			lengthArray.add(new ArrayList<String>());
		}
		
		for (String word : this.wordList) {
			int wordLength = word.length();
			lengthArray.get(wordLength - 3).add(word);
		}

		// display the found words
		this.printDelegate.println("Words from the Boggle Board:");
		for (List<String> words : lengthArray) {
			for (String word : words) {
				this.printDelegate.println(word);
			}
		}
		
		// calculate and display score
		this.printDelegate.println("\n Boggle Score:");
		int totalScore = 0;
		for (int i = 0; i < lengthArray.size(); i++) {
			int length = i + 3;
			int scoreAmount = 0;
			if (length <= 4) {
				scoreAmount = 1;
			}
			else if (length <= 5) {
				scoreAmount = 2;
			}
			else if (length <= 6) {
				scoreAmount = 3;
			}
			else if (length <= 7) {
				scoreAmount = 5;
			}
			else {
				// words of 8+ chars are worth 11
				scoreAmount = 11;
			}
			
			List<String> words = lengthArray.get(i);
			int numWords = words.size();
			if (numWords > 0) {
				this.printDelegate.printf("%d length words (%d points each): %d\n", length, scoreAmount, numWords);
				totalScore += numWords;
			}
		}
		
		this.printDelegate.printf("TOTAL SCORE: %d", totalScore);
	}
}
