package Games;

import java.util.List;

import Utils.Player;
import Cards.DeckOfCards;

public abstract class GamesWithDealer 
{
	private final Player m_dealer = new Player("DEALER");
	private final DeckOfCards m_deck = new DeckOfCards(true);
	private final List<Player> m_playersList;
	

	public GamesWithDealer(List<Player> _players) 
	{
		m_playersList = _players;
	}

	public Player getDealer() 
	{
		return m_dealer;
	}

	public DeckOfCards getDeck() 
	{
		return m_deck;
	}

	public List<Player> getPlayersList() 
	{
		return m_playersList;
	}
	
	/**
	 * Are there any active players remaining.
	 * @return
	 */
	public Boolean hasActivePlayers()
	{
		List<Player> playerList = getPlayersList();
		for ( Player player : playerList )
		{
			if (player.getActive())
				return true;
		}
		
		return false;
	}
}
