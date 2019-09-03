package opusgames.com.boggle;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;

import opusgames.com.boggle.delegates.BoggleActionDelegate;
import opusgames.com.boggle.dictionary.BoggleDictionaryControl;
import opusgames.com.boggle.ui.BogglePanel;


public class BoggleGameAsApplet extends JApplet implements BoggleActionDelegate {

	static final long serialVersionUID = 20140915L;
	
	static BoggleGame boggleGame;
	
	
	/**
	 * Called when this applet is loaded into the browser.
	 */
    public void init() {
        //Execute a job on the event-dispatching thread; creating this applet's GUI.
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    createInterface();
                }
            });
        } catch (Exception e) {
            System.err.println("ERROR: Applet creation did not complete successfully");
        }
    }
    
    
    /**
     * creates the applet swing interface
     */
    private void createInterface() {
    	this.setSize(400, 500);
    	
		BogglePanel uiFrame = new BogglePanel(this);
		this.getContentPane().add(uiFrame);
		
		BoggleDictionaryControl dictionaryControl = new BoggleDictionaryControl(uiFrame);
		BoggleGameAsApplet.boggleGame = new BoggleGame(uiFrame, dictionaryControl);
    }

    
	@Override
	public void playRandomBoard() {
		BoggleGameAsApplet.boggleGame.createRandomBoard();
		BoggleGameAsApplet.boggleGame.playBoggle();
	}

	
	@Override
	public void playTest4x4Board() {
		BoggleGameAsApplet.boggleGame.createTestBoard();
		BoggleGameAsApplet.boggleGame.playBoggle();
	}
}
