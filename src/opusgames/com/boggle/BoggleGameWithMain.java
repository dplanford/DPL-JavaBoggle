package opusgames.com.boggle;

import javax.swing.JFrame;

import opusgames.com.boggle.delegates.BoggleActionDelegate;
import opusgames.com.boggle.dictionary.BoggleDictionaryControl;
import opusgames.com.boggle.ui.BogglePanel;


/**
 * Creates a stand alone swing generated interface for playing the Boggle game
 * 
 * @author Douglas Lanford
 *
 */
public class BoggleGameWithMain implements BoggleActionDelegate {

	
	static BoggleGame boggleGame;
	
	
	public static void main(String[] args) {
		BoggleGameWithMain boggleMain = new BoggleGameWithMain();
		
		BogglePanel uiPanel = new BogglePanel(boggleMain);
		
		JFrame mainFrame = new JFrame();
		mainFrame.setSize(400, 768);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().add(uiPanel);
		mainFrame.setVisible(true);
		
		BoggleDictionaryControl dictionaryControl = new BoggleDictionaryControl(uiPanel);
		BoggleGameWithMain.boggleGame = new BoggleGame(uiPanel, dictionaryControl);
	}

	
	@Override
	public void playRandomBoard() {
		BoggleGameWithMain.boggleGame.createRandomBoard();
		BoggleGameWithMain.boggleGame.playBoggle();
	}

	
	@Override
	public void playTest4x4Board() {
		BoggleGameWithMain.boggleGame.createTestBoard();
		BoggleGameWithMain.boggleGame.playBoggle();
	}
}
