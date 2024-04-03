package GameApplet;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;


public class ScreenRectangle extends AnimatedScreenObject
{
	double top, bottom, left, right;
	boolean filled = true;

	public ScreenRectangle (double t, double b, double l, double r)
	{
		top = t;
		bottom = b;
		left = l;
		right = r;
		x = (left + right) / 2;
		y = (bottom + top) / 2;
	}


	public ScreenRectangle (double t, double b, double l, double r, Color c)
	{
		top = t;
		bottom = b;
		left = l;
		right = r;
		x = (left + right) / 2;
		y = (bottom + top) / 2;
		defaultColor = c;
	}

	public ScreenRectangle makeCopy()
	{
		ScreenRectangle b = new ScreenRectangle(top, bottom, left, right, defaultColor);
		b.filled = filled;
		// AnimatedScreenObject Stuff
		b.objectAnimated = objectAnimated;
		// MovingScreenObject Stuff
		b.v = v.makeCopy();
		b.a = a.makeCopy();
		b.updateDelay = updateDelay;
		b.objectMoving = objectMoving;
		synchronized(edges)
		{
			Iterator<ScreenRectangle> itr = edges.iterator();
			while (itr.hasNext())
				edges.add(itr.next().makeCopy());
		}
		b.edgeSound = edgeSound;
		b.edgeSoundSet = edgeSoundSet;
		b.mute = mute;
		b.pause = pause;
		// ScreenObject stuff
		b.defaultColor = defaultColor;
		b.repaint = repaint;
		b.repaintBuffer = repaintBuffer;
		b.oldLeft = oldLeft;
		b.oldRight = oldRight;
		b.oldTop = oldTop;
		b.oldBottom = oldBottom;
		b.oldX = oldX;
		b.oldY = oldY;
		// ScreenAttribute Stuff
		b.x = x;
		b.y = y;
		return b;
	}

	// accessors
	public double getLeft ()
	{
		return left;
	}


	public double getRight ()
	{
		return  right;
	}


	public double getTop ()
	{
		return top;
	}


	public double getBottom ()
	{
		return  bottom;
	}


	public void setX (double newX)
	{
		left = left + (newX - x);
		right = right + (newX - x);
		x = newX;
	}


	public void setY (double newY)
	{
		top = top + (newY - y);
		bottom = bottom + (newY - y);
		y = newY;
	}


	public void fill ()
	{
		filled = true;
	}


	public void unFill ()
	{
		filled = false;
	}


	// painting
	public void paint (Graphics g)
	{
		g.setColor (getDefaultColor ());
		if (filled)
			g.fillRect ((int) left, (int) top, (int) (right - left + 1), (int) (bottom - top + 1));
		else
			g.drawRect ((int) left, (int) top, (int) (right - left + 1), (int) (bottom - top + 1));
	}


	// animation - override if necessary
	public void animateOneStep ()
	{
	}
}


