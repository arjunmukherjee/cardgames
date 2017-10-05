package Games;

import java.util.List;
import java.util.Scanner;

import Utils.Player;
import Cards.Card;
import Cards.FaceCard;
import Cards.FaceCardEnum;


public class BlackJackGame extends GamesWithDealer implements Game
{
	private Player m_currentPlayer;
	
	public BlackJackGame(List<Player> _players) 
	{
		super(_players);
	}

	@Override
	public String playGame() 
	{
		// Deal the cards
		deal();
				
		// Run the actual game and compare hands
		runGame();
		
		return null;
	}

	/**
	 * 1. Check dealers hand.<br>
	 * 2. Allow each player to pick multiple cards or pass (check value each time).<br>
	 * 3. Make dealer pick cards and compare with player totals.<br>
	 * @return
	 */
	private void runGame() 
	{
		// Print fancy start and player, bet
		printPlayersAndBet();
		
		// Game over : Dealer has blackjack
		if ( playerHasBlackJack(getDealer()) )
		{
			// If other players also have blackjack, they win
			List<Player> playerList = getPlayersList();
			for ( Player player : playerList )
			{
				if ( playerHasBlackJack(player) )
				{
					player.getEarnings(2.5);
					player.setActive(false);
				}
			}
			System.out.println("GAME OVER !!");
			
			// Show the table the dealer's hand
			getDealer().printHand();
			
			return;
		}
		
		// Show the table a single dealer card
		System.out.println("DEALER HAS : " + getDealer().getHand().get(0).toString());

		Scanner in = new Scanner(System.in);

		// First check each players card to see if they have BlackJack
		// Ask each player if they want a card
		List<Player> playerList = getPlayersList();
		for ( Player player : playerList )
		{
			// Only check active players
			if ( !player.getActive() )
				continue;
			
			setCurrentPlayer(player);
			
			player.printHand();
			
			// Keep asking for cards until player says no
			Boolean playerSaysHitMe = true;
			while( playerSaysHitMe )
			{
				// If the player has BlackJack, they get 2 point and are removed form the game
				if ( playerHasBlackJack(player) )
				{
					// Get the bet * 2.5
					player.getEarnings(2.5);
					player.setActive(false);
					
					break;
				}
				// Check if player has crossed BlackJack
				else if ( hasPlayerHasCrossedBlackJack(player) )
				{
					player.setActive(false);
					player.setBet(0.0);			// Reset the bet
					player.setTotal(Card.BLACKJACK+1);
					
					break;
				}
				
				System.out.println("Total : " + getTotal(player));
				System.out.println(player.getName() + ", do you want a card ? <hit|hit me|yes|y|no|n|pass>");
				
				String playerResponse = in.nextLine();
			
				// If player responds Yes/Hit/Hit me/Y deal a card
				if ( playerResponse.equalsIgnoreCase("yes") || playerResponse.equalsIgnoreCase("y") || playerResponse.equalsIgnoreCase("hit") || playerResponse.equalsIgnoreCase("hit me") )
				{
					try 
					{
						Card card = getDeck().drawCard();
						System.out.println("Got card : " + card.toString());
						player.addToHand(card);
					} 
					catch (Exception e) 
					{
						System.out.println(e.getMessage());
					}
				}
				else
					playerSaysHitMe = false;
			}
		}
		
		// Check if there are still active players
		// If there are, deal cards to the dealer
		Player dealer = getDealer();
		try 
		{
			dealer.printHand();
			while( hasActivePlayers() )
			{
				// Compare all players hands with dealers hands
				for ( Player player : playerList )
				{
					if ( player.getActive() )
						compareHandsWithDealer(player);
				}
				
				if ( hasActivePlayers() )
				{
					Card card = getDeck().drawCard();
					System.out.println("DEALER takes card : " + card.toString());
					getDealer().addToHand(card);
					System.out.println(dealer.getName() + " : " + getTotal(dealer));
	
					// If the dealer has BlackJack, they get a point and are removed form the game
					if ( playerHasBlackJack(dealer) )
					{
						System.out.println();
						System.out.println("Game Over !!");
						return;
					}
					// Check if dealer has crossed BlackJack
					else if ( hasPlayerHasCrossedBlackJack(dealer) )
					{
						System.out.println("Game Over... PLAYERS WIN!!");
						for ( Player player : playerList )
						{
							if (player.getActive())
								player.getEarnings(2);
						}
						return;
					}
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}

	private void setCurrentPlayer(Player _player) 
	{
		m_currentPlayer = _player;
	}
	
	public Player getCurrentPlayer() 
	{
		return m_currentPlayer;
	}

	private void printPlayersAndBet() 
	{
		System.out.println("***********************************");
		System.out.println("*********** BLACKJACK *************");
		System.out.println("***********************************");
		System.out.println();
		
		System.out.println("**********************");
		System.out.println("PLAYER\t \tBET");
		System.out.println("**********************");
		for (Player player : getPlayersList())
			System.out.println(player.getName() + "\t \t" + player.getBet());
		System.out.println("**********************");
		System.out.println();
	}

	/**
	 * Get the total of the players hand.<br>
	 * Make sure to account for special logic of the Ace card.
	 * @param _player
	 * @return
	 */
	public int getTotal(Player _player) 
	{
		int playerTotal = 0;
		int aceCount = 0;
		for ( Card card : _player.getHand() )
		{
			if (card instanceof FaceCard )
			{
				if ( card.getNumber() == FaceCardEnum.Ace.getValue() )
				{
					aceCount++;
					playerTotal = playerTotal + FaceCard.BlackJackAceCardValue;
				}
				else
					playerTotal = playerTotal + FaceCard.BlackJackFaceCardValue;
			}
			else
				playerTotal = playerTotal + card.getNumber();
		}
		
		// If an ace was found and the total was more than 21, then use the smaller value of Ace (1)
		while ( ( aceCount > 0 ) && ( playerTotal > Card.BLACKJACK ) )
		{
			playerTotal = playerTotal - FaceCard.BlackJackAceCardValue + FaceCard.BlackJackAceCardAltValue;
			aceCount--;
		}
		
		return playerTotal;
	}

	private void compareHandsWithDealer(Player _player) 
	{
		int playerTotal = getTotal(_player);
		int dealerTotal = getTotal(getDealer());
		
		if ( dealerTotal > playerTotal)
		{
			System.out.println(_player.getName() + " beaten by DEALER - OUT!!");
			_player.setActive(false);
		}
	}

	/**
	 * Check if a player is still in the game
	 * @param _player
	 * @return
	 */
	public boolean hasPlayerHasCrossedBlackJack(Player _player) 
	{
		int total = getTotal(_player);
		if (total > Card.BLACKJACK )
			return true;
		else
			return false;
	}

	/**
	 * Check the player's hand, if he has 21 he wins
	 * @return
	 */
	public boolean playerHasBlackJack(Player _player) 
	{
		int total = getTotal(_player);
		if (total == Card.BLACKJACK)
			return true;
		else
			return false;
	}

	/**
	 * Deal 2 cards per player, and 2 cards to the dealer (Round robin)
	 */
	public void deal() 
	{
		// First reset the players and dealers hands
		for(Player player : getPlayersList())
			player.emptyHand();
		getDealer().emptyHand();

		try 
		{
			// Each Player draw 2 cards
			for (int i = 0; i < 2; i++)
			{
				for(Player player : getPlayersList())
					player.addToHand(getDeck().drawCard());
				
				// Draw a card for the dealer
				getDealer().addToHand(getDeck().drawCard());
			}
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
	}

}
