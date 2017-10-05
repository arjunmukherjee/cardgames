package Cards;


public class NumberCard extends Card 
{
	private final int m_number;
	
	public NumberCard(SuiteEnum _s, int _number)
	{
		super(_s);
		this.m_number = _number;
	}
	
	@Override
	public String toString() 
	{
		return m_number + " of " + this.getSuite().toString();
	}

	@Override
	public int getNumber() 
	{
		return m_number;
	}
	
}
