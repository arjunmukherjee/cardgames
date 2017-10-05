package Cards;

public enum FaceCardEnum 
{
	Jack(11), Queen(12), King(13), Ace(14);
	
	private final int m_number;
	
	private FaceCardEnum(int _value) 
    { 
    	this.m_number = _value; 
    }
    public int getValue() 
    { 
    	return m_number; 
    }
}
