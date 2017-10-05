package GameConsole;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import Utils.Player;
import Utils.Utils;
import Cards.Card;
import Games.BlackJackGame;

import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class BlackJackGameConsole 
{

	private static final int MAX_NUMBER_OF_PLAYERS = 5;
	private JFrame frmBlackjack;
	private JPanel contentPane;
	private JTextField txtAddPlayer;
	private JTable playersTable;
	private final JLabel currentPlayerLbl = new JLabel("");
	private final JLabel resultLabel = new JLabel("");
	private final JButton btnHitMe = new JButton("Hit");
	private final JButton btnStay = new JButton("Stay");
	private JLabel dealerHandLbl = new JLabel();
	private JLabel playerHandLbl = new JLabel();
	private JComboBox betBox = new JComboBox();
	
	private BlackJackGame game;
	
	private static List<Player> m_players = new ArrayList<Player>();
	private Set<String> m_playerNames = new HashSet<String>();
	private int m_playerIndex;
	private int m_activePlayers;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					BlackJackGameConsole window = new BlackJackGameConsole();
					window.frmBlackjack.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BlackJackGameConsole() 
	{
		m_activePlayers = 0;
		try {
			initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize()  throws IOException
	{
		frmBlackjack = new JFrame();
		contentPane = new JPanelWithBackgroundImage();
		//contentPane = new JPanel();
				
		frmBlackjack.setContentPane(contentPane);
		frmBlackjack.getContentPane().setBackground(Color.BLACK);
		frmBlackjack.setTitle("BlackJack (Min Bet $10, Max $100)");
		frmBlackjack.setBounds(100, 100, 579, 356);
		frmBlackjack.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBlackjack.getContentPane().setLayout(null);
		
		currentPlayerLbl.setForeground(Color.YELLOW);
		currentPlayerLbl.setBounds(116, 184, 98, 16);
		contentPane.add(currentPlayerLbl);
		
		// BUTTONS
		final JButton btnDeal = new JButton("Deal");
		btnDeal.setFont(new Font("Herculanum", Font.PLAIN, 13));
		final JButton addPlayerButton = new JButton(" Add Player");
		addPlayerButton.setFont(new Font("Herculanum", Font.PLAIN, 12));
		final JButton btnReset = new JButton("Shuffle");
		btnReset.setFont(new Font("Herculanum", Font.PLAIN, 13));
		btnHitMe.setFont(new Font("Herculanum", Font.PLAIN, 13));
		
		// HIT
		btnHitMe.setEnabled(false);
		btnHitMe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				Utils.playAudioClip("HitMe.wav");
				// Will draw a card and check the players hand
				checkPlayersHand(true);
			}
		});
		btnHitMe.setBounds(6, 212, 70, 29);
		frmBlackjack.getContentPane().add(btnHitMe);
		btnStay.setFont(new Font("Herculanum", Font.PLAIN, 13));
		
		// STAY
		btnStay.setEnabled(false);
		btnStay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				incrementPlayerIndex();
				resetPlayerDisplay(getCurrentPlayer(),null);
			}

		});
		btnStay.setBounds(77, 212, 70, 29);
		frmBlackjack.getContentPane().add(btnStay);
		
		// DEAL BUTTON
		btnDeal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if (m_players.size() < 1 )
					JOptionPane.showMessageDialog(null, "Please add a player to play the game!");
				else
				{
					addPlayerButton.setEnabled(false);
					txtAddPlayer.setEnabled(false);
					betBox.setEnabled(false);
					btnDeal.setEnabled(false);
					
					btnHitMe.setEnabled(true);
					btnStay.setEnabled(true);
				
					Utils.playAudioClip("shuffling-cards.wav"); 
					
					// Initialize the game with the correct arguments
					game = new BlackJackGame(m_players);
					game.deal();
					
					// Show dealers hand (1 card only), put it into the dealers table
					//String dealerHand = game.getDealer().getHand().get(0).toString();
					
					// Clear out the Dealer's hands
					// Show the one dealer card
					BufferedImage img = Utils.combineImage(game.getDealer().getHand().get(0).getImage(),Utils.toBufferedImage(game.getDeck().getDeckImage()));
					dealerHandLbl.setIcon(new ImageIcon(img));
					
					// Show current player's name, and hand
					resetPlayerDisplay(getCurrentPlayer(),null);
					
					// Check to see if a player has BlackJack
					checkPlayersHand(false);
					
					// Check if the dealer has BlackJack
					if ( game.playerHasBlackJack(game.getDealer()) )
					{
						// Check to see if dealersHand is greater than players
						compareDealersHandWithPlayers();
						finishRound(game.getDealer().getName() + " BLACKJACK!!");
					}
				}
			}
		});
		btnDeal.setBounds(6, 281, 90, 29);
		frmBlackjack.getContentPane().add(btnDeal);
		
		// RESET
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				DefaultTableModel model = null;
				// Clear out the dealers hand
				dealerHandLbl.setIcon(null);
				
				// Clear out the Player's hand display
				playerHandLbl.setIcon(null);
				
				currentPlayerLbl.setText("?");
				resultLabel.setText("");
				
				addPlayerButton.setEnabled(true);
				txtAddPlayer.setEnabled(true);
				betBox.setEnabled(true);
				
				btnDeal.setEnabled(true);
				
				m_playerIndex = 0;
				
				btnHitMe.setEnabled(false);
				btnStay.setEnabled(false);
				
				// Clear the player's table
				model = (DefaultTableModel) playersTable.getModel();
				model.setRowCount(0);
				
				List<Player> removeList = new ArrayList<Player>();
				
				// Clear the players table and reset it
				// Take the current bet, subtract from the cash
				for(Player player : m_players)
				{
					player.resetHand();
					
					if ( player.getCash() >= player.getBet() )
					{	
						
						Double bet = player.getBet();
						
						// Take the bet out of the cash, only if the player finished the round
						if ( !player.getActive() )
							player.setBet(bet);
						
						player.setActive(true);
						player.setStatus("");
						model.addRow(new Object[]{player.getName(),Utils.getDollarString(player.getCash()),Utils.getDollarString(player.getBet()),"In","--"});
						m_activePlayers++;
					}
					else if ( player.getCash() == 0 )
					{
						player.setActive(false);
						player.setStatus("Out");
						removeList.add(player);
					}
					else // There is some cash left, but not as much as bet
					{
						Double bet = player.getCash();
						
						// Take the bet out of the cash, only if the player finished the round
						if ( !player.getActive() )
							player.setBet(bet);
						
						player.setActive(true);
						player.setStatus("");
						model.addRow(new Object[]{player.getName(),Utils.getDollarString(player.getCash()),Utils.getDollarString(player.getBet()),"In","--"});
						m_activePlayers++;
					}
				}
			
				// Remove the players that have spent all their money
				for (Player player : removeList)
					m_players.remove(player);
			}
		});
		btnReset.setBounds(90, 281, 90, 29);
		frmBlackjack.getContentPane().add(btnReset);
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.setFont(new Font("Herculanum", Font.PLAIN, 13));
		btnQuit.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				System.exit(0);
			}});
		btnQuit.setBounds(173, 281, 90, 29);
		frmBlackjack.getContentPane().add(btnQuit);
		
		txtAddPlayer = new JTextField();
		txtAddPlayer.setBackground(Color.DARK_GRAY);
		txtAddPlayer.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) 
			{
				if (!txtAddPlayer.getText().equalsIgnoreCase("Name"))
					txtAddPlayer.setText("");
			}
		});
		txtAddPlayer.setToolTipText("The player's name");
		txtAddPlayer.setForeground(Color.YELLOW);
		txtAddPlayer.setText("Name");
		txtAddPlayer.setBounds(14, 6, 90, 28);
		frmBlackjack.getContentPane().add(txtAddPlayer);
		txtAddPlayer.setColumns(10);
		
		// ADD PLAYER
		addPlayerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				String playerName = txtAddPlayer.getText().trim();
				String betStr = betBox.getSelectedItem().toString().trim();
				betStr = betStr.substring(1);
				Double bet = 0.0;
				
				// Check to ensure the bet is numeric
				if ( !Utils.isNumeric(betStr) )
				{
					JOptionPane.showMessageDialog(null, "Please enter a valid bet!");
					return;
				}
				
				// Check the bet
				if ( (betStr == null) || betStr.isEmpty() || (betStr.length() <= 0) || betStr.equalsIgnoreCase("Bet") )
					bet = 10.0;
				else 
					bet = Double.valueOf(betStr);
				
				// Add the player to the player table
				playerName = playerName.toUpperCase();
				
				if ( (playerName == null) || playerName.isEmpty() || (playerName.length() <= 0)  )
					JOptionPane.showMessageDialog(null, "Please enter a valid payer name!");
				else if (m_playerNames.contains(playerName))
					JOptionPane.showMessageDialog(null, "Player name must be unique.");
				else if (bet < 10 || bet > 100)
					JOptionPane.showMessageDialog(null, "Min bet is $10, Max bet is $100.");
				else
				{
					
					if (bet == null || bet == 0)
						bet = 10.0;
					
					m_playerNames.add(playerName);
					Player newPlayer = new Player(playerName,bet);
					m_players.add(newPlayer);
					DefaultTableModel model = (DefaultTableModel)playersTable.getModel();
				    model.addRow(new Object[]{playerName,Utils.getDollarString(newPlayer.getCash()),Utils.getDollarString(newPlayer.getBet()),"In","--"});
				    
				    // Only allow a certain number
				    if (m_players.size() == MAX_NUMBER_OF_PLAYERS)
				    {
				    	addPlayerButton.setEnabled(false);
				    	txtAddPlayer.setEnabled(false);
				    }
				    
				    m_activePlayers++;
				}
				
				// Clear out the two text boxes
				txtAddPlayer.setText("");
				betBox.setSelectedIndex(0);
			}
		});
		addPlayerButton.setBounds(165, 7, 98, 29);
		frmBlackjack.getContentPane().add(addPlayerButton);
	
		// Players Table
		Object columnNames[] = { "Player", "Cash", "Bet","Status","Total"};
		TableModel model = new DefaultTableModel(null, columnNames);
		playersTable = new JTable(model);
		playersTable.setFont(new Font("Herculanum", Font.PLAIN, 12));
		playersTable.setShowGrid(false);
		playersTable.setShowVerticalLines(false);
		playersTable.setShowHorizontalLines(false);
		playersTable.setBorder(null);
		playersTable.setForeground(Color.YELLOW);
		playersTable.setEnabled(false);
		playersTable.setBackground(Color.BLACK);
		JScrollPane scrollPane = new JScrollPane(playersTable);
		scrollPane.setToolTipText("Players in the game");
		scrollPane.setBounds(16, 40, 200, 102);
		frmBlackjack.getContentPane().add(scrollPane);
		
		JLabel lblCurrentPlayer = new JLabel("Current Player :");
		lblCurrentPlayer.setForeground(Color.YELLOW);
		lblCurrentPlayer.setBounds(14, 184, 98, 16);
		frmBlackjack.getContentPane().add(lblCurrentPlayer);
		
		resultLabel.setForeground(Color.YELLOW);
		resultLabel.setBounds(14, 253, 237, 16);
		contentPane.add(resultLabel);
		dealerHandLbl.setToolTipText("Dealer's hand");
		
		
		dealerHandLbl.setForeground(Color.YELLOW);
		dealerHandLbl.setBounds(315, 12, 280, 130);
		contentPane.add(dealerHandLbl);
		playerHandLbl.setToolTipText("Player's hand");
		
		playerHandLbl.setForeground(Color.YELLOW);
		playerHandLbl.setBounds(315, 175, 290, 130);
		contentPane.add(playerHandLbl);
		
		JLabel lblDealer = new JLabel("Dealer");
		lblDealer.setFont(new Font("Herculanum", Font.BOLD | Font.ITALIC, 11));
		lblDealer.setForeground(Color.YELLOW);
		lblDealer.setBounds(345, 12, 70, 16);
		contentPane.add(lblDealer);
		betBox.setEditable(true);
		betBox.setToolTipText("Amount the player wants to bet");
		
		betBox.setFont(new Font("Herculanum", Font.PLAIN, 13));
		betBox.setModel(new DefaultComboBoxModel(new String[] {"$10", "$15", "$20", "$25", "$30", "$35", "$40", "$45", "$50", "$55", "$60", "$65", "$70", "$75", "$80", "$85", "$90", "$95", "$100"}));
		betBox.setForeground(Color.YELLOW);
		betBox.setBackground(Color.BLACK);
		betBox.setBounds(101, 8, 64, 27);
		contentPane.add(betBox);
	}

	private void reduceActivePlayerCount() 
	{
		// Reduce the number of active players
		m_activePlayers--;
	}

	private Player getCurrentPlayer() 
	{
		return m_players.get(m_playerIndex);
	}
	
	private void incrementPlayerIndex() 
	{
		// Now only dealer has to play (only if there are still active players remaining
		if ( m_activePlayers > 0)
		{
			if ( m_playerIndex == (m_players.size() - 1) )
			{
				dealerPlay();
				frmBlackjack.repaint();
			}
			else
			{
				m_playerIndex = m_playerIndex + 1;
				checkPlayersHand(false);
			}
		}
		else
			finishRound("GAME OVER - HOUSE WINS!!");
	}

	private void finishRound(String _result) 
	{
		resetDealerDisplay(_result);
		
		btnHitMe.setEnabled(false);
		btnStay.setEnabled(false);
	}

	private void payPlayers() 
	{
		// Give the players their money
		for(Player player : m_players)
		{
			if ( player.getActive() )
			{
				player.getEarnings(2);
				player.setStatus("WON");
				player.setActive(false);
			}
		}
		
		resetPlayersTable(null);
	}

	private void resetPlayersTable(String _result) 
	{
		// Clear the player table
		DefaultTableModel model = (DefaultTableModel) playersTable.getModel();
		model.setRowCount(0);
		// Clear the players table and reset it
		for(Player player : game.getPlayersList())
		{
			String status = "In";
			
			if ( ( player.getSatus() == null ) || (player.getSatus().trim().length() <= 0) )
			{
				if (!player.getActive())
					status = "Out";
			}
			else 
				status = player.getSatus();

			int playerTotal = game.getTotal(player);
			if ( playerTotal == Card.BLACKJACK )
				status = "BlackJack";
			model.addRow(new Object[]{player.getName(),Utils.getDollarString(player.getCash()),Utils.getDollarString(player.getBet()),status,game.getTotal(player)});
		}
		
		updateResultLabel(_result);
	}

	/**
	 * Dealer draws cards, comparing hand with players each time
	 */
	private void dealerPlay() 
	{
		Player dealer = game.getDealer();
		String result = null;

		// Check dealers current hand 
		// Check if dealer has BlackJack
		if ( game.playerHasBlackJack(dealer) )
		{
			// Check to see if dealersHand is greater than players
			compareDealersHandWithPlayers();
			
			finishRound(dealer.getName() + " BLACKJACK!!");
			return;
		}
		else
			// Check to see if dealersHand is greater than players
			compareDealersHandWithPlayers();
			
		while( m_activePlayers > 0 )
		{
			Card card = game.getDeck().drawCard();
			dealer.addToHand(card);
			//System.out.println("Dealer draws : " + card.toString());

			boolean gameOver = false;

			// Check if player has BlackJack
			if ( game.playerHasBlackJack(dealer) )
			{
				result = dealer.getName() + " BLACKJACK!!";
				gameOver = true;
				
				// Check to see if dealersHand is greater than players
				compareDealersHandWithPlayers();
			}
			else
			{
				// Check if dealer has crossed BlackJack
				if ( game.hasPlayerHasCrossedBlackJack(dealer) )
				{
					result = dealer.getName() + " BUST!!";
					gameOver  = true;
					
					// Pay the players
					payPlayers();
				}
				else
					// Check to see if dealersHand is greater than players
					compareDealersHandWithPlayers();
			}
			
			if (gameOver)
			{
				finishRound(result);
				return;
			}
			
			resetDealerDisplay(result);
		}
		
		finishRound(result);
	}
	
	/**
	 * For each player, check their total against the dealers current hand
	 * if less, the player is out
	 */
	private void compareDealersHandWithPlayers() 
	{
		int dealerTotal = game.getTotal(game.getDealer());
		int playerTotal = 0;
		String result = null;
		boolean playersLost = false;
		
		for (Player player : m_players)
		{
			if ( !player.getActive())
				continue;
			
			playerTotal = game.getTotal(player);
			
			//System.out.println("Dealers total = " + dealerTotal + ", " + player.getName() + " Total = " + playerTotal);
			
			if (dealerTotal > playerTotal)
			{
				player.setActive(false);
				player.setStatus("LOST");
				if (result == null)
					result = player.getName();
				else
					result = result + "," + player.getName();
				
				reduceActivePlayerCount();
				playersLost = true;
			}
			
			// TODO : TIE scenario i.e. dealerTotal == playerTotal
		}
		
		if (playersLost)
		{
			result = result + " BEAT BY DEALER!!";
			resetPlayersTable(result);
			resetDealerDisplay(result);
		}
	}

	private void checkPlayersHand(Boolean _takeCard) 
	{
		Player currentPlayer = getCurrentPlayer();
		
		if (_takeCard)
			currentPlayer.addToHand(game.getDeck().drawCard());
		
		String result = null;
		
		// Check if player has crossed BlackJack
		if ( _takeCard && game.hasPlayerHasCrossedBlackJack(currentPlayer) )
		{
			currentPlayer.setActive(false);
			currentPlayer.setStatus("BUST");
			
			result = currentPlayer.getName() + " BUST!!";
			
			reduceActivePlayerCount();
			
			incrementPlayerIndex();
		}
		
		// Check if player has BlackJack
		if ( game.playerHasBlackJack(currentPlayer) )
		{
			// Get the bet * 2.5
			currentPlayer.getEarnings(2.5);
			currentPlayer.setActive(false);
			currentPlayer.setStatus("BLACKJACK");
			
			result = currentPlayer.getName() + " BLACKJACK!!";
			
			reduceActivePlayerCount();
			
			incrementPlayerIndex();
		}
		
		resetPlayerDisplay(currentPlayer,result);
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		resetPlayerDisplay(getCurrentPlayer(),result);
	}

	private void resetDealerDisplay(String _result) 
	{
		updateResultLabel(_result);
		
		// Clear out the Dealer's hands
		dealerHandLbl.setIcon(null);
		
		// Show the dealers hands
		BufferedImage dealerHandImage = null;
		for(Card card : game.getDealer().getHand())
			dealerHandImage = Utils.combineImage(dealerHandImage, card.getImage());
		dealerHandLbl.setIcon(new ImageIcon(dealerHandImage));
	}

	private void resetPlayerDisplay(Player _player, String _result) 
	{
		resetPlayersTable(_result);

		// Set the current player
		currentPlayerLbl.setText(_player.getName());
		
		playersTable.setRowSelectionInterval(m_playerIndex, m_playerIndex);

		// Clear the player's hand
		playerHandLbl.setIcon(null);
		
		BufferedImage playerHandImage = null;
		// Show the players hand
		for(Card card : _player.getHand())
			playerHandImage = Utils.combineImage(playerHandImage, card.getImage());
		playerHandLbl.setIcon(new ImageIcon(playerHandImage));
		
		frmBlackjack.repaint();
	}

	private void updateResultLabel(String _result) 
	{
		// Update the result label
		if ( (_result != null) && ( _result.trim().length() > 0 ) )
		{
			resultLabel.setText(_result);
			resultLabel.repaint();
		}
	}
}



