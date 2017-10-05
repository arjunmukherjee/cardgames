package GameConsole;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JRadioButton;

import net.miginfocom.swing.MigLayout;

import javax.swing.JTextField;

import Utils.PlayGames;
import Utils.Player;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.JLabel;

public class CardGameConsole extends JFrame 
{
	private static final long serialVersionUID = -2727722859942118019L;
	private static final int NUMBER_OF_PLAYERS = 2;
	private static List<Player> m_players = new ArrayList<Player>();
	private Set<String> m_playerNames = new HashSet<String>();
	
	private JPanel contentPane;
	private JTextField txtAddPlayer;
	private JTable playersTable;
	private JTable handsTable;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CardGameConsole frame = new CardGameConsole();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	public CardGameConsole() throws IOException 
	{
		setTitle("Card Game ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 558, 300);
		contentPane = new JPanelWithBackground();
		//contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(Color.black);
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[126px,grow][75px,grow][][51px,grow]", "[grow][29px,grow][][][grow][grow][][][][][][][][]"));
		
		txtAddPlayer = new JTextField();
		contentPane.add(txtAddPlayer, "cell 0 1,growx");
		txtAddPlayer.setColumns(10);
		
		ButtonGroup bGroup = new ButtonGroup();
		
		final JLabel resultLabel = new JLabel("Winner ?");
		resultLabel.setForeground(new Color(255, 255, 0));
		contentPane.add(resultLabel, "cell 2 1");
		
		final JRadioButton oneCardDrawRadioButton = new JRadioButton("One Card Draw");
		oneCardDrawRadioButton.setForeground(new Color(255, 255, 255));
		contentPane.add(oneCardDrawRadioButton, "cell 0 10,alignx left,aligny center");
		bGroup.add(oneCardDrawRadioButton);
		
		final JRadioButton threeCardDrawRadioButton = new JRadioButton("Three Card Draw");
		threeCardDrawRadioButton.setForeground(Color.WHITE);
		contentPane.add(threeCardDrawRadioButton, "cell 0 11");
		bGroup.add(threeCardDrawRadioButton);
		
		final JRadioButton blackJackGameRadioButton = new JRadioButton("BlackJack");
		blackJackGameRadioButton.setForeground(new Color(255, 255, 255));
		threeCardDrawRadioButton.setForeground(Color.WHITE);
		contentPane.add(blackJackGameRadioButton, "cell 0 12");
		bGroup.add(blackJackGameRadioButton);
		
		// Add Player Button
		final JButton addPlayerButton = new JButton("Add Player");
		addPlayerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				String playerName = txtAddPlayer.getText().trim();
				
				// Add the player to the player table
				playerName = playerName.toUpperCase();
				
				if ( (playerName == null) || playerName.isEmpty() || (playerName.length() <= 0)  )
					JOptionPane.showMessageDialog(null, "Please enter a valid payer name!");
				else if (m_playerNames.contains(playerName))
				{
					JOptionPane.showMessageDialog(null, "Player name must be unique");
				}
				else
				{
					m_playerNames.add(playerName);
					m_players.add(new Player(playerName));
					DefaultTableModel model = (DefaultTableModel)playersTable.getModel();
				    model.addRow(new Object[]{playerName,"0"});
				    
				    // For now only allow 2 players
				    if (m_players.size() == NUMBER_OF_PLAYERS)
				    {
				    	addPlayerButton.setEnabled(false);
				    	txtAddPlayer.setEnabled(false);
				    }
				}
				txtAddPlayer.setText("");
			}
		});
		contentPane.add(addPlayerButton, "cell 1 1");
		
		// Players Table
		Object columnNames[] = { "Player", "Points"};
		TableModel model = new DefaultTableModel(null, columnNames);
		playersTable = new JTable(model);
		playersTable.setEnabled(false);
		playersTable.setBackground(new Color(176, 196, 222));
		JScrollPane scrollPane = new JScrollPane(playersTable);
		scrollPane.setToolTipText("List of Players in the game");
		contentPane.add(scrollPane, "cell 0 4,grow");
		
		// Hands Table
		Object columnNamesHands[] = { "Player1 Hand", "Player2 Hand" };
		TableModel modelHands = new DefaultTableModel(null, columnNamesHands);
		handsTable = new JTable(modelHands);
		handsTable.setEnabled(false);
		handsTable.setBackground(new Color(176, 196, 222));
		JScrollPane scrollPaneHands = new JScrollPane(handsTable);
		contentPane.add(scrollPaneHands, "cell 2 4 1 6,grow");
		
		
		// Play the game
		final JButton playButton = new JButton("Deal");
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if (m_players.size() < 2 )
					JOptionPane.showMessageDialog(null, "Please add two players to play the game!");
				else
				{
					if (!oneCardDrawRadioButton.isSelected() && !threeCardDrawRadioButton.isSelected() && !blackJackGameRadioButton.isSelected() )
					{
						JOptionPane.showMessageDialog(null, "Please select a game to play!");
					}
					else
					{
						try 
						{
							AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("media/sounds/shuffling-cards.wav").getAbsoluteFile());
							Clip clip = AudioSystem.getClip();
							clip.open(audioInputStream);
							clip.start();
					
						} catch (UnsupportedAudioFileException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						} catch (LineUnavailableException e1) {
							e1.printStackTrace();
						}
						
						String winner = null;
						if (oneCardDrawRadioButton.isSelected())
							winner = PlayGames.playGame("One Card",m_players);
						else if (threeCardDrawRadioButton.isSelected())
							winner = PlayGames.playGame("Three Card",m_players);
						else if (blackJackGameRadioButton.isSelected())
							winner = PlayGames.playGame("BlackJack",m_players);


						// Update the results label
						if ( (winner != null) && !winner.isEmpty() )
							resultLabel.setText(winner);

						// Add the players and points
						DefaultTableModel model = (DefaultTableModel) playersTable.getModel();
						model.setRowCount(0);
						for (Player player : m_players)
							model.addRow(new Object[]{player.getName(),player.getPoints()});

						// Clear out the hands
						model = (DefaultTableModel) handsTable.getModel();
						model.setRowCount(0);
						// Add the new hands
						int size = 0;
						for (Player player : m_players)
						{
							if( player.getHand().size() > size )
								size = player.getHand().size();
						}
						for (int i=0; i < size; i++)
							model.addRow(new Object[]{m_players.get(0).getHand().get(i).toString(),m_players.get(1).getHand().get(i).toString()});
					}
				}
			}
		});
		contentPane.add(playButton, "cell 0 13,alignx left,aligny top");
		
		JButton quitButton = new JButton("Quit");
		quitButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				if (m_players.size() >=2 )
				{
					String winner;
					Player player1 = m_players.get(0);
					Player player2 = m_players.get(1);
					if (player1.getPoints() > player2.getPoints())
						winner = player1.getName() + " WINS!";
					else if (player1.getPoints() < player2.getPoints())
						winner = player2.getName() + " WINS!";
					else
						winner = "It's a TIE!";
					
					JOptionPane.showMessageDialog(null, winner);
				}
				
				System.exit(0);
			}
		});
		
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				// Clear the players
				DefaultTableModel model = (DefaultTableModel)playersTable.getModel();
				model.setRowCount(0);
			
				// Clear out the hands
				model = (DefaultTableModel)handsTable.getModel();
				model.setRowCount(0);
				
				m_players.clear();
				m_playerNames.clear();
				
				resultLabel.setText("Winner ?");
				oneCardDrawRadioButton.setSelected(false);
				threeCardDrawRadioButton.setSelected(false);
				blackJackGameRadioButton.setSelected(false);
				
				addPlayerButton.setEnabled(true);
				txtAddPlayer.setEnabled(true);
			}
		});
		contentPane.add(resetButton, "cell 1 13");
		contentPane.add(quitButton, "cell 2 13");
	}
	
	static class JPanelWithBackground extends JPanel 
	{
		private static final long serialVersionUID = 8146768804089742552L;
		private Image backgroundImage;

		// Some code to initialize the background image.
		// Here, we use the constructor to load the image. This
		// can vary depending on the use case of the panel.
		public JPanelWithBackground() throws IOException 
		{
			backgroundImage = getToolkit().getImage(ClassLoader.getSystemResource("cardGameImage.jpg"));
		}

		public void paintComponent(Graphics g) 
		{
			super.paintComponent(g);

			// Draw the background image.
			g.drawImage(backgroundImage, 80, 0, this);
		}
	}
	
}



