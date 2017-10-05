package Games;

import Utils.Player;
import Cards.Card;

public class HighTotalGame extends TwoPlayerGames implements Game
{
	public HighTotalGame(Player _player1, Player _player2)
	{
		super(_player1, _player2);
	}
	
	@Override
	public String playGame() 
	{
		//System.out.println();
		//System.out.println("Playing game : THREE CARD DRAW");
		
		// Deal the cards
		this.deal();
		
		// Compare hands
		return this.compareHands();
	}

	private String compareHands() 
	{
		int player1Total = 0;
		int player2Total = 0;
		String result;
		
		// Get the total from Player1
		for (Card card : this.getPlayer1().getHand())
			player1Total = player1Total + card.getNumber();
		
		// Get the total from Player2
		for (Card card : this.getPlayer2().getHand())
			player2Total = player2Total + card.getNumber();
	
		// Compare the results and award points to the winner
		if (player1Total > player2Total)
		{
			getPlayer1().addPoint();
			result = getPlayer1().getName() + " WINS over " + getPlayer2().getName();
		}
		else if (player1Total == player2Total)
			result = getPlayer1().getName() + " TIES with " + getPlayer2().getName();
		else
		{
			getPlayer2().addPoint();
			result = getPlayer2().getName() + " WINS over " + getPlayer1().getName();
		}
		
		//System.out.println(result);
		//getPlayer1().printHand();
		//getPlayer2().printHand();
		
		return result;
	}

	/**
	 * Deal three cards to each player.
	 */
	private void deal() 
	{
		// First reset the players hands
		getPlayer1().emptyHand();
		getPlayer2().emptyHand();
		
		try 
		{
			for (int i = 0; i < 6; i++)
			{
				if (i%2 == 0)	
					getPlayer1().addToHand(getDeck().drawCard());
				else
					getPlayer2().addToHand(getDeck().drawCard());
			}
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
	}

}
