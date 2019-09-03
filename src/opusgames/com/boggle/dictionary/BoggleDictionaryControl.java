package opusgames.com.boggle.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import opusgames.com.boggle.BoggleGame;
import opusgames.com.boggle.delegates.BogglePrintDelegate;


/**
 * This class provides access to the Boggle dictionary, both for loading the dictionary, and 
 * checking strings against the dictionary
 * 
 * @author Douglas Lanford
 *
 */
public class BoggleDictionaryControl {
	
	
	/** 
	 * this class is used to store a single element in the dictionary.
	 * If the "word" field is non-null, this element represents a complete word (which 
	 * may be the first part of a longer word)
	 *
	 * If the "word" field is null, then this element represents an incomplete pathway 
	 * to a word
	 *
	 * The "nextLetters" array holds next dictionary elements for any further letters in 
	 * dictionary words
	 */
	public static class DictionaryElement
	{
		private String word;
		private DictionaryElement[] nextLetters = new DictionaryElement[26];

		
		public DictionaryElement() {
			this.word = null;
			for (int i = 0; i < 26; i++) {
				this.nextLetters[i] = null;
			}
		}
		
		
		public String getWord() {
			return this.word;
		}
		
		
		public void setWord(String word) {
			this.word = word;
		}
		
		
		public DictionaryElement getNextElementForChar(char ch) {
			int chIndex = ch - 'a';
			return this.nextLetters[chIndex];
		}
		
		
		public DictionaryElement setNextElementForChar(char ch) {
			int chIndex = ch - 'a';
			if (this.nextLetters[chIndex] == null) {
				DictionaryElement newElement = new DictionaryElement();
				this.nextLetters[chIndex] = newElement;
			}
			
			return this.nextLetters[chIndex];
		}
	}

	
	private BogglePrintDelegate printDelegate;
	
	private DictionaryElement dictionary = null;
	
	private int numWordsInDictionary;
	private int numWordsParsed;
	
	
	/**
	 * Boggle Dictionary class constructor
	 * 
	 * @param printDelegate
	 */
	public BoggleDictionaryControl(BogglePrintDelegate printDelegate) {
		this.printDelegate = printDelegate;
		
		try {
			this.loadDictionary();
		}
		catch (IOException ex) {
			this.printDelegate.println("ERROR - could not load dictionary");
		}
	}


	/**
	 * parse a new word into the dictionary, generating dictionary elements for each 
	 * letter as needed
	 * NOTE: only allows words between 3 and 9 characters in length (given Boggle limits), 
	 * and no proper names (no capitalized characters in a word)
	 * 
	 * @param the new word to add to the dictionary
	 */
	private void parseDictionaryWord(String newWord) {
		if (newWord == null) {
			this.printDelegate.println("Error: parsing null dictionary word");
			return;
		}
		
		this.numWordsParsed++;
		
		int wordLength = newWord.length();
		
		if (wordLength < 3 || wordLength > BoggleGame.MAX_WORD_LENGTH) {
			// Boggle words are at least 3 chars, and no more than the total chars in the boggle board.
			// Skip this dictionary word....
			return;
		}
		
		// filter this dictionary word for non lower-case letters... proper names and illegal chars are not allowed!
		for (int i = 0; i < wordLength; i++) {
			char ch = newWord.charAt(i);
			if (ch < 'a' || ch > 'z') {
				// This dictionary word is invalid for Boggle... skip it.
				return;
			}
		}
		
		if (this.dictionary == null) {
			// dictionary does not yet exist... this is the first word... create the initial dictionary root
			this.dictionary = new DictionaryElement();
		}
		
		DictionaryElement curElement = this.dictionary;

		// walk through the chars in the word, building a dictionary path for the word.
		for (int i = 0; i < wordLength; i++) {
			char ch = newWord.charAt(i);
			curElement = curElement.setNextElementForChar(ch);
		}
		
		// hit end of pathway for this word... set this dictionary element to match this new word
		curElement.setWord(newWord);
		
		this.numWordsInDictionary++;
	}

	
	/** 
	 * load the dictionary from a predefined line separated file
	 */
	private void loadDictionary() throws IOException
	{
		this.printDelegate.println("==== Parsing Dictionary File ====");
		
		this.numWordsInDictionary = 0;
		this.numWordsParsed = 0;
		
		// open the resource file for streaming
		InputStream inStream = this.getClass().getResourceAsStream("/opusgames/com/boggle/resources/boggleDictionary.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(inStream));

		String strLine;

		// read the file line by line... each line is a new word for the dictionary.
		// NOTE: some words in the file will be ignored for being proper names, etc.
		while ((strLine = br.readLine()) != null) {
			this.parseDictionaryWord(strLine);
		}

		// close the input stream
		br.close();
		
		this.printDelegate.printf("%d words used in dictionary, of %d parsed from file\n", this.numWordsInDictionary, this.numWordsParsed);
		this.printDelegate.println("==== Finished Parsing Dictionary File ====\n");
	}
	
	
	public DictionaryElement getDictionary() {
		return this.dictionary;
	}
}
