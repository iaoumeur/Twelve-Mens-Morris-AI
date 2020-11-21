package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class Game {

	static final int frameWidth = 1000;
	static final int frameHeight = 750;
	static final int numberOfPieces = 12;
	private JFrame frame;
	private JLayeredPane lpane;
	
	private BoardLabel board;
	private ArrayList<JLabel> whitePieces;
	private ArrayList<JLabel> blackPieces;
	private JPanel whitePiecePanel;
	private JPanel blackPiecePanel;
	private JLabel turnLabel;
	private JLabel msgLabel;
	
	ImageIcon whitePieceImage; 
	ImageIcon blackPieceImage;
	

	private String turn = "white";
	private String[] gameMessages = {"A Mill is formed! Select a piece not in a mill to remove", "Invalid Piece Removal", 
			};
	private String[] turnMessages = {"<html><span style=\"font-size:23px;color:rgb(211,211,211);font-weight: bold;"
			+ "\">Turn:   </span><span style=\"color:white;font-size:23px;\">White</span></html>", 
			"<html><span style=\"font-size:23px;color:rgb(211,211,211);font-weight: bold;"
					+ "\">Turn:   </span><span style=\"color:black;font-size:23px;\">Black</span></html>"};
	
	private int[][] millLocations = {{0,1,2}, {3,4,5}, {6,7,8}, {9,10,11}, {12,13,14}, {15,16,17}, {18,19,20}, {21,22, 23},
			{0,9,21}, {3,10,18}, {6,11,15}, {1,4,7}, {16,19,22}, {8,12,17}, {5,13,20}, {2,14,23}, {0,3,6}, {2,5,8}, {21,18,15},
			{23,20,17}
	};
	String[] millsFound = new String[millLocations.length];
	private boolean pieceRemovalTurn = false;
	
	//1 = phase 1 - placing stage
	//2 = phase 2 - movement stage
	//3 = phase 3 - "flying" movement stage
	//4 = mill created - remove opponent's piece
	//5 = game end - draw, or player wins
	private int gameState = 1; 
	private int prevGameState = 1;
	
	public Game(String p1Name, String p2Name) {
		
		frame = new JFrame("Twelve Men's Morris");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0, 0, frameWidth, frameHeight);
		frame.setLocationRelativeTo(null);
		lpane = new JLayeredPane();
		frame.add(lpane, BorderLayout.CENTER);
		frame.setResizable(false);
		frame.setVisible(true);

		lpane.setBounds(0, 0, frameWidth, frameHeight);
		
		JPanel backgroundPanel = new JPanel();
		backgroundPanel.setBounds(0, 0, frameWidth, frameHeight);
		ImageIcon backgroundImage = new ImageIcon(this.getClass().getResource("/GameBackground.jpg"));
		JLabel backgroundLabel = new JLabel(backgroundImage);
		backgroundPanel.add(backgroundLabel);
		
		JPanel boardPanel = new JPanel();
		ImageIcon boardImage = new ImageIcon(this.getClass().getResource("/BoardInvisible.png"));
		board = new BoardLabel(boardImage, this);
		boardPanel.setBounds(250, 100, boardImage.getIconWidth(), boardImage.getIconHeight());
		boardPanel.add(board, BorderLayout.CENTER);
		boardPanel.setOpaque(false);
		
		whitePieceImage = new ImageIcon(this.getClass().getResource("/WhitePiece.png"));
		whitePieces = new ArrayList<JLabel>();
		whitePiecePanel = new JPanel();
		for(int i=0; i<numberOfPieces; i++) {
			whitePieces.add(new JLabel(whitePieceImage));
			whitePiecePanel.add(whitePieces.get(i));
		}
		whitePiecePanel.setBounds(165, 120, 70, 300);
		whitePiecePanel.setOpaque(false);
		
		blackPieceImage = new ImageIcon(this.getClass().getResource("/BlackPiece.png"));
		blackPieces = new ArrayList<JLabel>();
		blackPiecePanel = new JPanel();
		for(int i=0; i<numberOfPieces; i++) {
			blackPieces.add(new JLabel(blackPieceImage));
			blackPiecePanel.add(blackPieces.get(i));
		}
		blackPiecePanel.setBounds(730, 345, 70, 300);
		blackPiecePanel.setOpaque(false);
		
		JPanel p1Panel = new JPanel();
		String p1Text = "<html><span style=\"font-family:Arial;font-size:32px;color:white;font-weight: bold;"
				+ "\">Player 1</span><br><span style=\"color:white;font-size:16px;font-style:italic\">" + p1Name +
				"</span></html>";
		JLabel p1Label = new JLabel(p1Text);
		p1Panel.setBounds(270, 10, 200, 300);
		p1Panel.add(p1Label, BorderLayout.CENTER);
		p1Panel.setOpaque(false);
		
		JPanel p2Panel = new JPanel();
		String p2Text = "<html><span style=\"font-family:Arial;font-size:32px;color:black;font-weight: bold;"
				+ "\">Player 2</span><br><span style=\"color:black;font-size:16px;font-style:italic\">" + p2Name + 
				"</span></html>";
		JLabel p2Label = new JLabel(p2Text);
		p2Panel.setBounds(500, 10, 200, 300);
		p2Panel.add(p2Label, BorderLayout.CENTER);
		p2Panel.setOpaque(false);
		
		JPanel msgPanel = new JPanel();
		msgLabel= new JLabel();
		msgPanel.setBounds(30, 650, 650, 100);
		msgPanel.add(msgLabel, BorderLayout.CENTER);
		msgPanel.setOpaque(false);
		
		JPanel turnPanel = new JPanel();
		turnLabel = new JLabel("Turn: White");
		turnLabel.setForeground(Color.WHITE);
		turnPanel.setBounds(700, 650, 300, 100);
		turnPanel.add(turnLabel, BorderLayout.CENTER);
		turnPanel.setOpaque(false);
		
		/*JPanel exitPanel = new JPanel();
		ImageIcon exitImage = new ImageIcon(this.getClass().getResource("/ExitButton.png"));
		exitPanel.setBounds(frameWidth - exitImage.getIconWidth()-30, exitImage.getIconHeight()-40, exitImage.getIconWidth(), exitImage.getIconHeight()+10);
		JButton exitButton = new JButton(exitImage);
		exitPanel.add(exitButton);
		exitPanel.setOpaque(false);
		exitButton.setBorderPainted( false );*/
		
		
		
		lpane.add(backgroundPanel, 0, 0);
		lpane.add(boardPanel, 1, 0);
		lpane.add(whitePiecePanel, 1, 0);
		lpane.add(blackPiecePanel, 1, 0);
		lpane.add(p1Panel, 1, 0);
		lpane.add(p2Panel, 1, 0);
		lpane.add(msgPanel, 1, 0);
		lpane.add(turnPanel,1, 0);
		//lpane.add(exitPanel, 1, 0);
		
	}
	
	public String getTurn() {
		return turn;
	}
	
	public void switchTurn() {
		if(turn=="white") { 
			turn = "black";
			turnLabel.setText(turnMessages[1]);
		}
		else if(turn=="black") {
			turn = "white";
			turnLabel.setText(turnMessages[0]);
		}
	}
	
	public void removeWhitePieceFromPanel() {
		if(whitePieces.size()==0) {
			return;
		}
		whitePieces.remove(whitePieces.size()-1);
		whitePieces.trimToSize();
		whitePiecePanel.removeAll();
		for(int i=0; i<whitePieces.size(); i++) {
			whitePiecePanel.add(whitePieces.get(i));				
		}
		whitePiecePanel.repaint();
	}
	
	public void removeBlackPieceFromPanel() {
		if(blackPieces.size()==0) {
			return;
		}
		blackPieces.remove(blackPieces.size()-1);
		blackPieces.trimToSize();
		blackPiecePanel.removeAll();
		for(int i=0; i<blackPieces.size(); i++) {
			blackPiecePanel.add(blackPieces.get(i));				
		}
		blackPiecePanel.repaint();
	}

	public int getGameState() {
		return gameState;
	}
	
	public void setGameState(int state) {
		prevGameState = gameState;
		gameState = state;
		if(gameState==4) {
			msgLabel.setText("<html><span style=\"font-size:16px;color:white;"
					+ "\">" + gameMessages[0] + "</span></html>");
		}
		else {
			msgLabel.setText("");
		}
		
	}
	
	public void invalidPieceRemoval() {
		msgLabel.setText("<html><span style=\"font-size:16px;color:white;"
				+ "\">" + gameMessages[1] + "</span></html>");
	}

	public boolean checkForMill(String[] boardPieces) {
		for(int i=0; i<millLocations.length; i++) {
			if(boardPieces[millLocations[i][0]]=="white" && boardPieces[millLocations[i][1]]=="white" && boardPieces[millLocations[i][2]]=="white") {
				if(millsFound[i]==null) {
					//new mill
					millsFound[i] = "white";
					return true;
				}
				else {
					//already existing mill
				}
			}
			if(boardPieces[millLocations[i][0]]=="black" && boardPieces[millLocations[i][1]]=="black" && boardPieces[millLocations[i][2]]=="black") {
				if(millsFound[i]==null) {
					//new mill
					millsFound[i] = "black";
					return true;
				}
				else {
					//already existing mill
				}
			}
		}
		
		return false;
		
	}


	public boolean inMill(int piecePosition) {
		for(int i=0; i<millLocations.length; i++) {
			for(int j=0; j<millLocations[i].length; j++) {
				if(piecePosition==millLocations[i][j]) {
					if(millsFound[i]!=null) {
						return true;
					}
				}
			}
		}
		return false;
	}


}
