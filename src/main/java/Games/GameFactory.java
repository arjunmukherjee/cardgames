package Games;

import java.util.List;

import Utils.Player;

public class GameFactory 
{
	public static Game getGame(String _game, List<Player> _players)
	{
		if ( _game.equals("One Card") )
			return new HighCardGame(_players.get(0), _players.get(1));
		else if ( _game.equals("Three Card") )
			return new HighTotalGame(_players.get(0), _players.get(1));
		else if ( _game.equals("BlackJack") )
			return new BlackJackGame(_players);
			
		return null;
	}
	
}
