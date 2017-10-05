package Cards;

import java.awt.image.BufferedImage;

public abstract class Card implements Comparable<Card>
{
	public static final int BLACKJACK = 21;
	private BufferedImage m_cardIcon;
	
	private final SuiteEnum m_suite;

	public Card(SuiteEnum _suite)
	{
		m_suite = _suite;
	}
	
	public Card(SuiteEnum _suite, BufferedImage _image)
	{
		this(_suite);
		m_cardIcon = _image;
	}
	
	public SuiteEnum getSuite() 
	{
		return m_suite;
	}
	
	public BufferedImage getImage()
	{
		return m_cardIcon;
	}
	
	public void setImage(BufferedImage _image)
	{
		m_cardIcon = _image;
	}
	
	@Override
	public abstract String toString();
	
	public abstract int getNumber();

	@Override
	public int compareTo(Card arg0) 
	{
		return Integer.valueOf(this.getNumber()).compareTo((Integer)arg0.getNumber());
	}
}
