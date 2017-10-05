package Utils;

import java.util.ArrayList;
import java.util.List;

import Games.Game;
import Games.GameFactory;

public class PlayGames 
{

	public static String playGame(String _game, List<Player> _players) 
	{
		// Play the Card game
		Game game = GameFactory.getGame(_game, _players);
		return game.playGame();
	}
	
	/**
	 * To be used for testing purposes.
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Player p = new Player("Orko",100.0);
		Player p1 = new Player("Som",20.0);
		Player p2 = new Player("Swe",20.0);
		
		List<Player> players = new ArrayList<Player>();
		players.add(p); players.add(p1);players.add(p2);
		playGame("BlackJack", players);
		
		printWinnings(players);
	}

	private static void printWinnings(List<Player> players) 
	{
		System.out.println();
		System.out.println("***********************");
		System.out.println("*****  WINNINGS  ******");
		System.out.println("***********************");
		for (Player player : players)
			System.out.println(player.getName() + "\t:\t$" + (player.getCash()));
		System.out.println("***********************");
		System.out.println();
	}
}
