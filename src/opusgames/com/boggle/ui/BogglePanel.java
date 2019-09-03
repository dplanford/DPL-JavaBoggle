package opusgames.com.boggle.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import opusgames.com.boggle.BoggleGame;
import opusgames.com.boggle.delegates.BoggleActionDelegate;
import opusgames.com.boggle.delegates.BogglePrintDelegate;


/**
 * 
 * 
 * @author Douglas Lanford
 *
 */
public class BogglePanel extends JPanel implements BogglePrintDelegate {

	static final long serialVersionUID = 20140915L;
	
	private BoggleActionDelegate actionDelegate;
	
	
	private JTextArea textArea;
	
	
	public BogglePanel(BoggleActionDelegate actionDelegate) {
		this.actionDelegate = actionDelegate;
		
		this.setLayout(new BorderLayout());
		
		this.textArea = new JTextArea();
		this.textArea.setEditable(false);
		this.textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		JScrollPane scrollPane = new JScrollPane(this.textArea, 
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setSize(1024, 60);

		JButton playRandomButton = new JButton("Play Random Board");
		playRandomButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BogglePanel.this.actionDelegate.playRandomBoard();
			}
		});
		buttonPanel.add(playRandomButton);
		
		if (BoggleGame.BOARD_SIZE == 4) {
			JButton playTestButton = new JButton("Play Test Board");
			playTestButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					BogglePanel.this.actionDelegate.playTest4x4Board();
				}
			});
			buttonPanel.add(playTestButton);
		}
		
		this.add(buttonPanel, BorderLayout.PAGE_START);
		this.add(scrollPane, BorderLayout.CENTER);
		
		this.setVisible(true);
	}
	
	
	@Override
	public void print(String str) {
		this.textArea.append(str);
	}
	
	
	@Override
	public void println() {
		this.textArea.append("\n");
	}
	
	
	@Override
	public void println(String str) {
		this.textArea.append(str);
		this.println();
	}
	
	
	@Override
	public void printf(String format, Object... args) {
		this.textArea.append(String.format(format, args));
	}


	@Override
	public void clear() {
		this.textArea.setText("");
	}
}
