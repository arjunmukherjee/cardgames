package Games;

import Utils.Player;
import Cards.Card;

public class HighCardGame extends TwoPlayerGames implements Game
{
	public HighCardGame(Player _player1, Player _player2)
	{
		super(_player1, _player2);
	}
	
	@Override
	public String playGame() 
	{
		//System.out.println();
		//System.out.println("Playing game : ONE CARD DRAW");
		
		// Deal the cards
		this.deal();
		
		// Compare hands
		return this.compareHands();
	}

	private String compareHands() 
	{
		return getPlayer1().getName() + compare(getPlayer1(), getPlayer2(), true) + getPlayer2().getName();
	}

	/**
	 * Compare two cards. Returning the result.
	 * @param _player1
	 * @param _player2
	 * @return WIN/TIE/LOSS
	 */
	private String compare(Player _player1, Player _player2, Boolean _addOrNot)
	{
		Card card1 = _player1.getCardFromHand();
		Card card2 = _player2.getCardFromHand();
		int result = card1.compareTo(card2);
		
		if ( result > 0 )
		{
			addPoint(getPlayer1(),_addOrNot);
			return " WINS over ";
		}
		else if ( result == 0 )
			return " TIES with ";
		else
		{
			addPoint(getPlayer2(),_addOrNot);
			return " LOSES to ";	
		}
	}
	
	private void addPoint(Player _player, Boolean _addOrNot)
	{
		if (_addOrNot)
			_player.addPoint();
	}
	

	/**
	 * Deal a card to each player.
	 */
	private void deal() 
	{
		// First reset the players hands
		getPlayer1().emptyHand();
		getPlayer2().emptyHand();
				
		try 
		{
			getPlayer1().addToHand(getDeck().drawCard());
			getPlayer2().addToHand(getDeck().drawCard());
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
	}
}
