import java.awt.Color;
import java.awt.Graphics;

import GameApplet.AnimatedScreenObject;
import GameApplet.GameCanvas;
import GameApplet.ScreenPoint;
import GameApplet.ScreenVelocityVector;

//Class: StarField
//Author: Sam Scott
//Date: April 19, 2006
//- draws a sky of blinking stars
//- has a "rush forward" effect for changing levels or whatever



public class StarField extends AnimatedScreenObject
{
	// PRIVATE VARIABLES

	// the stars
	private int numStars = 50;
	private ScreenPoint[] stars;
	private Color starColor = Color.white;        // colors
	private Color backgroundColor = Color.black;

	// the starfield size
	private int width, height;      // size of star field (centered around x and y)

	// parameters for the "rush" effect
	private double maxYSpeed = 10;   // for the rush forward effect
	private double yAcceleration = 0.04;

	// animation (star blinking)
	private int starBlink = -1;        // current star that's blinking
	private double starBlinkProbability = 0.8; // probability of star blinking on frame 0
	private int animationCounter = 0; // current animation frame
	private int numFrames;   // total number of frames to cycle through in animation


	// CONSTRUCTORS ETC
	StarField ()
	{
		x = 100;
		y = 100;
		width = 100;
		height = 100;
		initStars ();
	}


	StarField (int newX, int newY, int newWidth, int newHeight)
	{
		x = newX;
		y = newY;
		width = newWidth;
		height = newHeight;
		initStars ();
	}

	StarField (int newX, int newY, int newWidth, int newHeight, int ns)
	{
		x = newX;
		y = newY;
		width = newWidth;
		height = newHeight;
		numStars = ns;
		initStars ();
	}
	private void initStars ()
	{
		stars = new ScreenPoint [numStars];
		for (int i = 0 ; i < numStars ; i++)
			stars [i] = new ScreenPoint ((int) (Math.random () * (width) - width / 2 + x), (int) (Math.random () * (height) - height / 2 + y), starColor);
		v = new ScreenVelocityVector (0, 0);
		numFrames = (int) getFrameRate ();
		startThread ();
		startAnimating ();
	}


	// ACCESSORS
	public double getLeft ()
	{
		return x - width / 2;
	}


	public double getRight ()
	{
		return  x + (width + 1) / 2;
	}


	public double getTop ()
	{
		return  y - height / 2;
	}


	public double getBottom ()
	{
		return  y + (height + 1) / 2;
	}


	// THE RUSH EFFECT
	public void rush ()
	{
		stopAnimating ();
		v.zero ();
		a.setX (0);
		a.setY (yAcceleration);
		startMoving ();
	}


	// MOVE, ANIMATE, PAINT
	public void moveOneStep ()
	{
		for (int i = 0 ; i < numStars ; i++) // move each star separately
		{
			v.movePoint (stars [i]);
			if (stars [i].getY () > y + height / 2) // check for stars moving off screen
			{
				stars [i].setX ((int) (Math.random () * width - width / 2 + x));
				stars [i].setY (stars [i].getY () - height);
			}
			else if (stars [i].getY () < y - height / 2)
			{
				stars [i].setX ((int) (Math.random () * width - width / 2 + x));
				stars [i].setY (stars [i].getY () + height);
			}
			if (stars [i].getX () > x + width / 2)
			{
				stars [i].setX (stars [i].getX () - width);
				stars [i].setY ((int) (Math.random () * (height) - height / 2 + y));
			}
			else if (stars [i].getX () < x - width / 2)
			{
				stars [i].setX (stars [i].getX () + width);
				stars [i].setY ((int) (Math.random () * (height) - height / 2 + y));
			}
		}
		// handle the rush effect
		if ((a.getY () > 0) && (v.getY () >= maxYSpeed))
			a.reflectY ();
		if ((a.getY () < 0) && (v.getY () <= 0))
		{
			a.zero ();
			v.zero ();
			stopMoving ();
			startAnimating ();
		}
	}


	public void animateOneStep ()
	{
		animationCounter = (++animationCounter) % (numFrames / 10);
		if (animationCounter == 0)  // blink out a star in frame 0
		{
			if (starBlink != -1) // unblink
			{
				stars [starBlink].setDefaultColor (starColor);
				stars [starBlink].requestRepaint ();
			}
			if (Math.random () < starBlinkProbability) //blink
			{
				starBlink = (int) (Math.random () * numStars);
				stars [starBlink].setDefaultColor (backgroundColor);
				stars [starBlink].requestRepaint ();
			}
			else
				starBlink = -1;
		}
	}


	public void paint (Graphics g)
	{
		for (int i = 0 ; i < numStars ; i++)
			stars [i].paint (g);
	}


	public void smartRepaint (GameCanvas a)
	{
		for (int i = 0 ; i < numStars ; i++)
			stars [i].smartRepaint (a);
	}
}


