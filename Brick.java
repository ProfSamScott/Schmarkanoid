import java.awt.Color;
import java.awt.Graphics;

import GameApplet.ScreenRectangle;


public class Brick extends ScreenRectangle
{
	private int brickType;
	public final static int NOBRICK = 0;
	public final static int NORMAL = 1;
	public final static int INDESTRUCTIBLE = 2;
	public final static int LEFTONLY = 3;
	public final static int RIGHTONLY = 4;
	public static final int TOPONLY = 5;
	public static final int BOTTOMONLY = 6;
	public final static int EXPLODING = 7;
	private final Color defaultBrickColor = new Color (200, 20, 0);
	private final Color shadowColor = new Color(100,10,0);
	private final Color crackColor = Color.black;
	private final Color brickExplodeColor = Color.yellow;
	private final int startExplosionOffset = -3;
	private double explosionOffset;
	private final double brickExplodeSpeed = 1;

	public Brick (double left, double top, double width, double height)
	{
		super (top, top + height - 1, left, left + width - 1);
		defaultColor = defaultBrickColor;
		brickType = NORMAL;
	}


	public Brick (double left, double top, double width, double height, int type)
	{
		super (top, top + height - 1, left, left + width - 1);
		defaultColor = defaultBrickColor;
		// if (type == INDESTRUCTIBLE)
			//     unFill ();
		brickType = type;
	}


	// accessors
	public int type ()
	{
		return brickType;
	}


	public void changeType (int newType)
	{
		if (newType == EXPLODING)
			explode ();
		else
		{
			setDefaultColor (defaultBrickColor);
			brickType = newType;
			requestRepaint ();
		}
	}


	// overriding accessors for collision
	public double getCollisionLeft ()
	{
		return getLeft () - 1;
	}


	public double getCollisionRight ()
	{
		return getRight () + 1;
	}


	public double getCollisionTop ()
	{
		return getTop () - 1;
	}


	public double getCollisionBottom ()
	{
		return getBottom () + 1;
	}


	// destruction!
	public void explode ()
	{
		brickType = EXPLODING;
		setDefaultColor (brickExplodeColor);
		explosionOffset = startExplosionOffset;
		setRepaintBuffer((int)((-1)*explosionOffset));
		startAnimating ();
		startThread ();
	}


	public void animateOneStep ()
	{
		explosionOffset += brickExplodeSpeed;
		if (explosionOffset >= (getBottom () - getTop () + 1) / 2)
		{
			brickType = NOBRICK;
			stopAnimating ();
			stopThread ();
		}
		requestRepaint ();
	}


	public void paint (Graphics g)
	{
		g.setColor (getDefaultColor ());
		if (brickType == NORMAL)
		{
			g.setColor (shadowColor);
			g.fillRect ((int) getLeft (), (int) getTop (), (int) (getRight () - getLeft () + 1), (int) (getBottom () - getTop () + 1));
			g.setColor (getDefaultColor());
			g.fillRect ((int) getLeft ()+2, (int) getTop ()+2, (int) (getRight () - getLeft () + 1)-4, (int) (getBottom () - getTop () + 1)-4);
			//g.setColor(crackColor);
			//g.drawLine((int)getLeft(), (int)getTop(), (int)getRight(), (int)getBottom());
			//g.drawLine((int)getRight(), (int)getTop(), (int)getLeft(), (int)getBottom());      
		}
		else if (brickType == INDESTRUCTIBLE)
		{
			g.setColor (shadowColor);
			g.fillRect ((int) getLeft (), (int) getTop (), (int) (getRight () - getLeft ()+1), (int) (getBottom () - getTop () + 1));
			g.setColor (crackColor);
			g.fillRect ((int) getLeft ()+2, (int) getTop ()+2, (int) (getRight () - getLeft () + 1)-4, (int) (getBottom () - getTop () + 1)-4);
		}
		else if (brickType == LEFTONLY)
		{
			g.setColor (shadowColor);
			g.fillRect ((int) getLeft (), (int) getTop (), (int) (getRight () - getLeft ()+1), (int) (getBottom () - getTop () + 1));
			g.setColor (crackColor);
			g.fillRect ((int) getLeft ()+2, (int) getTop ()+2, (int) (getRight () - getLeft () + 1)-4, (int) (getBottom () - getTop () + 1)-4);
			g.setColor (getDefaultColor());
			int xPoints[] = { (int) getLeft ()+2, (int) getLeft ()+2, (int) getX ()+1, (int) getLeft ()+2 };
			int yPoints[] = { (int) getTop ()+1, (int) getBottom ()-1, (int) getY (), (int) getTop ()+1 };
			g.fillPolygon (xPoints, yPoints, 4);
		}
		else if (brickType == RIGHTONLY)
		{
			g.setColor (shadowColor);
			g.fillRect ((int) getLeft (), (int) getTop (), (int) (getRight () - getLeft ()+1), (int) (getBottom () - getTop () + 1));
			g.setColor (crackColor);
			g.fillRect ((int) getLeft ()+2, (int) getTop ()+2, (int) (getRight () - getLeft () + 1)-4, (int) (getBottom () - getTop () + 1)-4);
			g.setColor (getDefaultColor());
			int xPoints[] = { (int) getRight ()-1, (int) getRight ()-1, (int) getX (), (int) getRight ()-1 };
			int yPoints[] = { (int) getTop ()+1, (int) getBottom ()-1, (int) getY (), (int) getTop ()+1 };
			g.fillPolygon (xPoints, yPoints, 4);
		}
		else if (brickType == TOPONLY)
		{
			g.setColor (shadowColor);
			g.fillRect ((int) getLeft (), (int) getTop (), (int) (getRight () - getLeft ()+1), (int) (getBottom () - getTop () + 1));
			g.setColor (crackColor);
			g.fillRect ((int) getLeft ()+2, (int) getTop ()+2, (int) (getRight () - getLeft () + 1)-4, (int) (getBottom () - getTop () + 1)-4);
			g.setColor (getDefaultColor());
			int xPoints[] = { (int) getLeft ()+2, (int) getRight ()-1, (int) getX (), (int) getLeft ()+2 };
			int yPoints[] = { (int) getTop ()+2, (int) getTop ()+2, (int) getY ()+2, (int) getTop ()+2 };
			g.fillPolygon (xPoints, yPoints, 4);
		}
		else if (brickType == BOTTOMONLY)
		{
			g.setColor (shadowColor);
			g.fillRect ((int) getLeft (), (int) getTop (), (int) (getRight () - getLeft ()+1), (int) (getBottom () - getTop () + 1));
			g.setColor (crackColor);
			g.fillRect ((int) getLeft ()+2, (int) getTop ()+2, (int) (getRight () - getLeft () + 1)-4, (int) (getBottom () - getTop () + 1)-4);
			g.setColor (getDefaultColor());
			int xPoints[] = { (int) getLeft ()+1, (int) getRight (), (int) getX (), (int) getLeft ()+1 };
			int yPoints[] = { (int) getBottom ()-1, (int) getBottom ()-1, (int) getY ()-1, (int) getBottom ()-1 };
			g.fillPolygon (xPoints, yPoints, 4);
		}
		else if (brickType == EXPLODING)
			g.fillRect ((int) (getLeft () + explosionOffset), (int) (getTop () + explosionOffset), (int) (getRight () - getLeft () + 1 - explosionOffset * 2), (int) (getBottom () - getTop () + 1 - explosionOffset * 2));
	}
}
