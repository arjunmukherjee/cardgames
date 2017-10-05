package Cards;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import Utils.Utils;

public class DeckOfCards 
{
	private final List<Card> cards = new ArrayList<Card>(NUMBER_OF_CARDS_IN_DECK);
	private int m_cardsRemaining;
	public static final int NUMBER_OF_CARDS_IN_DECK = 52;
	private Image m_deckImage;
	
	public DeckOfCards(Boolean _shuffle, String _filePath)
	{
		this(_shuffle);
		initDeckImage(_filePath);
	}

	/**
	 * Creates a deck of 52 cards (13 * 4 suites)
	 * @param _shuffle : Should the deck be shuffled or not.
	 */
	public DeckOfCards(Boolean _shuffle) 
	{
		initDeckImage(null);
		m_cardsRemaining = NUMBER_OF_CARDS_IN_DECK;
		
		int j = 0;
		for (SuiteEnum s : SuiteEnum.values())
		{
			// 13 cards per suite
			// 9 number cards, 4 face cards
			for (int i=0; i<9; i++)
				cards.add(i+j, new NumberCard(s,i+2)); 
			j = j + 13;
			
			// Add the Jack
			cards.add(j-4,new FaceCard(s,FaceCardEnum.Jack));
			// Add the Queen
			cards.add(j-3,new FaceCard(s,FaceCardEnum.Queen));
			// Add the King
			cards.add(j-2,new FaceCard(s,FaceCardEnum.King));
			// Add the Ace
			cards.add(j-1,new FaceCard(s,FaceCardEnum.Ace));
		}
		
		// Shuffle the cards
		if ( _shuffle )
			Collections.shuffle(cards);
		
		// Add the image to each card
		for (Card card : cards )
			card.setImage(Utils.getImage(card));
	}
	
	private void initDeckImage(String _filePath) 
	{
		URL url = null;
		BufferedImage sampleImage = null;
		
		if (_filePath != null && _filePath.trim().length() > 0)
			url = ClassLoader.getSystemResource(_filePath);
		else
			url = ClassLoader.getSystemResource("playing-card-back.jpg");
		
		try 
		{
			setDeckImage(ImageIO.read(url));
			sampleImage = ImageIO.read(ClassLoader.getSystemResource("b8-2.gif"));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		
		m_deckImage =  m_deckImage.getScaledInstance(sampleImage.getWidth(), sampleImage.getHeight(), Image.SCALE_REPLICATE);
	}
	
	/**
	 * Return the top card form the deck of remaining cards.
	 * @return
	 * @throws Exception 
	 */
	public Card drawCard() 
	{
		//if (m_cardsRemaining <= 0 )
		//	throw new Exception("No more cards in deck.");
		
		return cards.get(--m_cardsRemaining);
	}
	
	/**
	 * Reset the deck of cards. The cards will be reset to the original order (i.e. shuffled or not).
	 */
	public void resetDeck()
	{
		m_cardsRemaining = NUMBER_OF_CARDS_IN_DECK;
	}
	
	public void printDeck()
	{
		System.out.println();
		System.out.println("CURRENT DECK OF CARDS : [" + m_cardsRemaining + "]");
		for (int i=0; i < m_cardsRemaining; i++)
			System.out.println(cards.get(i).toString());
		
	}

	public Image getDeckImage() {
		return m_deckImage;
	}

	public void setDeckImage(BufferedImage m_deckImage) {
		this.m_deckImage = m_deckImage;
	}
	
}
