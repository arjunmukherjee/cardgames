package Utils;

import java.util.ArrayList;
import java.util.List;


import Cards.Card;


public class Player 
{
	private final String m_name;
	private final List<Card> m_hand;
	private int points;
	private Boolean m_active;
	private double m_cash;
	private double m_bet;
	private int m_total;
	private String m_status;
	
	/**
	 * Each player starts with $100.
	 * Has a name and is active.
	 * Gets 0 points
	 * @param _name
	 */
	public Player(String _name) 
	{
		m_name = _name;
		m_hand = new ArrayList<Card>();
		points = 0;
		m_active = true;
		m_cash = 90;
		m_bet = 10;
		setTotal(0);
	}
	
	/**
	 * Each player starts with $100.
	 * Has a name and is active.
	 * Gets 0 points.
	 * Initialize current bet.
	 * @param _name
	 */
	public Player(String _name, Double bet) 
	{
		m_name = _name;
		m_hand = new ArrayList<Card>();
		points = 0;
		m_active = true;
		m_cash = 100;
		
		if ( bet > m_cash )
			throw new IllegalArgumentException("Bet is higher than the cash player holds.");
		
		m_bet = bet;
		m_cash = m_cash - m_bet;
	}
	
	public String getName()
	{
		return m_name;
	}

	public List<Card> getHand() 
	{
		return m_hand;
	}
	
	public void addToHand(Card _card)
	{
		m_hand.add(_card);
	}
	
	public void printHand()
	{
		System.out.println();
		System.out.println(m_name + "'s HAND : ");
		for(Card card : m_hand)
			System.out.println(card.toString());
	}
	
	public Card getCardFromHand()
	{
		return m_hand.get(0);
	}

	public int getPoints() 
	{
		return points;
	}

	public void addPoint() 
	{
		this.points = points + 1;
	}

	public void emptyHand() 
	{
		m_hand.clear();
	}

	public Boolean getActive() 
	{
		return m_active;
	}

	public void setActive(Boolean m_active) 
	{
		this.m_active = m_active;
	}

	public double getCash() 
	{
		return m_cash;
	}
	
	public void addCash(double _cash) 
	{
		this.m_cash = this.m_cash + _cash;
	}
	public void deductCash(double _cash) 
	{
		this.m_cash = this.m_cash - _cash;
	}

	public double getBet() 
	{
		return m_bet;
	}
	
	public void setBet(Double _bet) 
	{
		m_bet = _bet;
		m_cash = m_cash - m_bet;
	}

	/**
	 * Add winnings to a players bank.
	 * @param d (the factor to multiply the players bet by)
	 */
	public void getEarnings(double d) 
	{
		m_cash = m_cash + (d * m_bet);
		//m_bet = 0;
	}

	public int getTotal() {
		return m_total;
	}

	public void setTotal(int m_total) {
		this.m_total = m_total;
	}

	public String getSatus() {
		return m_status;
	}

	public void setStatus(String m_status) {
		this.m_status = m_status;
	}
	
	public void resetHand()
	{
		m_hand.clear();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) return false;
		if (this == obj) return true;
		if ( this.getClass() != obj.getClass() ) return false;
		
		Player player = (Player) obj;
		return this.m_name.equals(player.getName());
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = result * prime + ( (m_name == null) ? 0 : m_name.hashCode() );
		
		return result;
	}

	
}
