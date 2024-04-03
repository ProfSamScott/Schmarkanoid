import java.awt.Color;

import GameApplet.ScreenRectangle;


public class PlanetDoor extends ScreenRectangle {

	double closedY, openY, planetSpeed = 0.5;

	public PlanetDoor (double t, double b, double l, double r, Color c)
	{
		super(t,b,l,r,c);
		closedY = y;
		openY = y + (b-t);
		stopMoving();
		stopAnimating();
	}
	public void open()
	{
		v.setVector(0,planetSpeed);
		if (!threadRunning())
		{
			startThread();
			startMoving();
			startAnimating();
		}
	}
	public void close()
	{
		v.setVector(0,-planetSpeed);
		if (!threadRunning())
		{
			startThread();
			startMoving();
			startAnimating();
		}
	}
	public void animateOneStep()
	{
		if ((v.getY()<0 && y<= closedY) || (v.getY()>0 && y>=openY))
			stopThread();
	}
}
