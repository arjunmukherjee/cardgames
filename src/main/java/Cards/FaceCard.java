package Cards;

public class FaceCard extends Card
{
	public static int BlackJackFaceCardValue = 10;
	public static int BlackJackAceCardValue = 11;
	public static int BlackJackAceCardAltValue = 1;
	
	private final FaceCardEnum m_nameCard;
	
	public FaceCard(SuiteEnum _s, int _number)
	{
		super(_s);
		this.m_nameCard = getFaceCard(_number);
	}

	public FaceCard(SuiteEnum _s, FaceCardEnum _faceCard)
	{
		super(_s);
		this.m_nameCard = _faceCard;
	}
	
	private FaceCardEnum getFaceCard(int _number) 
	{
		if (_number < 11 || _number > 14)
			throw new IllegalArgumentException("Incorrect value range for a face card.");
		switch(_number)
		{
			case 11 :
				return FaceCardEnum.Jack;
			case 12 :
				return FaceCardEnum.Queen;
			case 13 :
				return FaceCardEnum.King;
			case 14 :
				return FaceCardEnum.Ace;
		}
		return null;
	}
	
	@Override
	public String toString() 
	{
		return m_nameCard + " of " + this.getSuite().toString();
	}

	@Override
	public int getNumber()
	{
		return this.m_nameCard.getValue();
	}
}
