package Games;

import Utils.Player;
import Cards.DeckOfCards;

public abstract class TwoPlayerGames 
{
	private final Player m_player1;
	private final Player m_player2;
	
	private final DeckOfCards m_deck = new DeckOfCards(true);
	
	public TwoPlayerGames(Player _player1, Player _player2)
	{
		m_player1 = _player1;
		m_player2 = _player2;
	}
	
	public Player getPlayer1() 
	{
		return m_player1;
	}
	public Player getPlayer2() 
	{
		return m_player2;
	}

	public DeckOfCards getDeck() {
		return m_deck;
	}
}
