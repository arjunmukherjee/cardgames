package Utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import Cards.Card;
import Cards.SuiteEnum;

public class Utils 
{
	public static BufferedImage getImage(Card card) 
	{
		int suiteNum = 0;
		if (card.getSuite() == SuiteEnum.Clubs)
			suiteNum = 1;
		else if (card.getSuite() == SuiteEnum.Diamonds)
			suiteNum = 2;
		else if (card.getSuite() == SuiteEnum.Hearts)
			suiteNum = 3;
		else if (card.getSuite() == SuiteEnum.Spades)
			suiteNum = 4;
		
		
		String iconName = "b"+card.getNumber()+"-"+suiteNum+".gif";
		
		URL url = ClassLoader.getSystemResource(iconName);
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(url);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return img;
	}
	
	public  static Map<String, BufferedImage> getImageHash() 
	{
		int COLS = 13;
		int ROWS = 4;
		Map<String, BufferedImage> imageHash = new HashMap<String,BufferedImage>();
		
		final String[] CARDS = {
		      "2 of Hearts","3 of Hearts","4 of Hearts","5 of Hearts","6 of Hearts","7 of Hearts","8 of Hearts","9 of Hearts","10 of Hearts","Jack of Hearts","Queen of Hearts","King of Hearts","Ace of Hearts",
		      "2 of Diamonds","3 of Diamonds","4 of Diamonds","5 of Diamonds","6 of Diamonds","7 of Diamonds","8 of Diamonds","9 of Diamonds","10 of Diamonds","Jack of Diamonds","Queen of Diamonds","King of Diamonds","Ace of Diamonds",
		      "2 of Clubs","3 of Clubs","4 of Clubs","5 of Clubs","6 of Clubs","7 of Clubs","8 of Clubs","9 of Clubs","10 of Clubs","Jack of Clubs","Queen of Clubs","King of Clubs","Ace of Clubs",
		      "2 of Spades","3 of Spades","4 of Spades","5 of Spades","6 of Spades","7 of Spades","8 of Spades","9 of Spades","10 of Spades","Jack of Spades","Queen of Spades","King of Spades","Ace of Spades"  
		   };
		
		File url = new File("images/deckOfCards.png");
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(url);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		img = img.getSubimage(0, 0, img.getWidth(), img.getHeight());

		int w = img.getWidth() / COLS;
		int h = img.getHeight() / ROWS;
		for (int row = 0; row < ROWS; row++) 
		{
			int y = (row * img.getHeight()) / ROWS;
			for (int col = 0; col < COLS; col++) 
			{
				int x = (col * img.getWidth()) / COLS;
				BufferedImage subImg = img.getSubimage(x, y, w, h);

				subImg = subImg.getSubimage(0, 0, subImg.getWidth(), subImg.getHeight());
				String cardText = CARDS[col + row * COLS];
				imageHash.put(cardText, subImg);
			}
		}
		
		return imageHash;
	}

	public static void playAudioClip(String _fileName) 
	{
		// Play the audio clip
		try 
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(ClassLoader.getSystemResource(_fileName));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	        return (BufferedImage) img;
	
	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
	
	/**
	 * Combine two images into one.
	 * @param _image1
	 * @param _image2
	 * @return
	 */
	public static BufferedImage combineImage(BufferedImage _image1, BufferedImage _image2) 
	{
		if (_image1 == null)
			return _image2;
		else
		{
			int w = _image1.getWidth() + _image2.getWidth() - 30;
			int h = Math.max(_image1.getHeight(), _image2.getHeight());
			BufferedImage image = new BufferedImage(w, h,  BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = (Graphics2D) image.getGraphics();
			g2.drawImage(_image1, 0, 0, null);
			g2.drawImage(_image2, _image1.getWidth()-30, 0, null);
			g2.dispose();
			return image;
		}
	}
	
	/**
	 * Check if a string is numeric or not
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str)
	{
	    for (char c : str.toCharArray())
	    {
	        if ( !Character.isDigit(c) ) 
	        	return false;
	    }
	    return true;
	}
	
	/**
	 * Remove the ".00"
	 * Add the "$" sign
	 * @param _cash
	 * @return
	 */
	public static String getDollarString(Double _cash) 
	{
		String cash = String.valueOf(_cash).trim();
		int decimalIndex = cash.indexOf(".");
		cash = "$"+cash.substring(0, decimalIndex);
		return cash;
	}

}
