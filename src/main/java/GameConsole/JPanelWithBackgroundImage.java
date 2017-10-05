package GameConsole;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.swing.JPanel;

class JPanelWithBackgroundImage extends JPanel 
{
	private static final long serialVersionUID = 2501276224573454044L;
	private Image backgroundImage;

	// Some code to initialize the background image.
	// Here, we use the constructor to load the image. This
	// can vary depending on the use case of the panel.
	public JPanelWithBackgroundImage() throws IOException 
	{
		backgroundImage = getToolkit().getImage(ClassLoader.getSystemResource("cardGameImage.jpg"));
	}

	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);

		// Draw the background image.
		g.drawImage(backgroundImage, 10, 0, this);
	}
}