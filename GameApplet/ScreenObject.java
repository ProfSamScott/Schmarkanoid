// ScreenObject
// - stationary, composite, mca_ing, and animated objects.
// Created: Sam Scott, April 17, 2006
// Modified:
//
// SamScott@Canada.com

package GameApplet;

import java.awt.Color;
import java.awt.Graphics;

public abstract class ScreenObject extends ScreenAttribute
{
  // constants for the detectCollision method
  public static final int NOCOLLISION = 0;
  public static final int LEFTCOLLISION = 1;
  public static final int RIGHTCOLLISION = 2;
  public static final int TOPCOLLISION = 3;
  public static final int BOTTOMCOLLISION = 4;
  
  // default color of object
  protected Color defaultColor = Color.white;
  
  protected boolean repaint = false; // set to TRUE if repaint requested or required
  protected int repaintBuffer = 0; // extra space around the edge of the object
  protected double oldLeft = 0, oldRight = 0, oldTop = 0, oldBottom = 0; // keeps track of parameters from last repaint
  protected double oldX, oldY;
  
  protected ScreenObject ()
  {
    super ();
  }
  
  
  protected ScreenObject (double x, double y)
  {
    super (x, y);
    oldLeft = getLeft ();
    oldRight = getRight ();
    oldTop = getTop ();
    oldBottom = getBottom ();
    oldX = x;
    oldY = y;
  }
  
  
  ScreenObject (double x, double y, Color c)
  {
    super (x, y);
    oldLeft = getLeft ();
    oldRight = getRight ();
    oldTop = getTop ();
    oldBottom = getBottom ();
    oldX = x;
    oldY = y;
    defaultColor = c;
  }
  
  
  // new accessor methods
  public Color getDefaultColor ()
  {
    return defaultColor;
  }
  
  
  public void setDefaultColor (Color c)
  {
    defaultColor = c;
  }
  
  
  public int getRepaintBuffer ()
  {
    return repaintBuffer;
  }
  
  
  public void setRepaintBuffer (int newRB)
  {
    repaintBuffer = newRB;
  }
  
  
  public void requestRepaint ()
  {
    repaint = true;
  }
  
  
  // decides if a repaint is needed
  // - yes if repaint has been requested
  // - yes if object's x or y location has changed since last repaint
  // - no otherwise
  public boolean repaintNeeded ()  // override as necessary
  {
    repaint = repaint || ((int) oldX != (int) x) || ((int) oldY != (int) y);
    return repaint;
  }
  
  
  // calls repaint if necessary
  public void smartRepaint (GameCanvas a)  // override as needed
  {
    if (repaintNeeded ())
    {
      a.repaint ((int) getLeft () - repaintBuffer, (int) getTop () - repaintBuffer, (int) getRight () - (int) getLeft () + 1 + repaintBuffer * 2, (int) getBottom () - (int) getTop () + 1 + repaintBuffer * 2);
      a.repaint ((int) oldLeft - repaintBuffer, (int) oldTop - repaintBuffer, (int) oldRight - (int) oldLeft + 1 + repaintBuffer * 2, (int) oldBottom - (int) oldTop + 1 + repaintBuffer * 2);
    }
    repaint = false;
    oldX = x;
    oldY = y;
    oldLeft = getLeft ();
    oldRight = getRight ();
    oldTop = getTop ();
    oldBottom = getBottom ();
  }
  
  
  // paint the object on a graphics context
  public abstract void paint (Graphics g);
  
  // accessors for use with repainting
  public abstract double getLeft ();
  public abstract double getRight ();
  public abstract double getTop ();
  public abstract double getBottom ();
  
  // accessors for use with collisions - ca_erride if necessary
  public double getCollisionLeft ()
  {
    return getLeft ();
  }
  
  
  public double getCollisionRight ()
  {
    return getRight ();
  }
  
  
  public double getCollisionTop ()
  {
    return getTop ();
  }
  
  
  public double getCollisionBottom ()
  {
    return getBottom ();
  }
  
  
  final double buffer = 2.0;
  public ScreenCollision detectCollision (MovingScreenObject o, boolean reflect)
  {
    // CHECK FOR OVERLAP
    if (((o.getCollisionRight () < getCollisionLeft ()) || (getCollisionRight () < o.getCollisionLeft ()))
          || ((o.getCollisionBottom () < getCollisionTop ()) || (getCollisionBottom () < o.getCollisionTop ())))
      return new ScreenCollision(this, ScreenObject.NOCOLLISION); // none found
    
    // LEFT AND RIGHT OVERLAP
    double ca_left = 0, ca_right = 0;
    // one interval inside the other
    if (((o.getCollisionLeft () >= getCollisionLeft ()) && (o.getCollisionRight () <= getCollisionRight ())))
    { // o inside this
      ca_left = o.getCollisionLeft ();
      ca_right = o.getCollisionRight ();
    }
    else if (((getCollisionLeft () >= o.getCollisionLeft ()) && (getCollisionRight () <= o.getCollisionRight ())))
    { // this inside o
      ca_left = getCollisionLeft ();
      ca_right = getCollisionRight ();
    }
    // partial ca_erlap
    else if ((getCollisionRight () - o.getCollisionLeft ()) > (o.getCollisionRight () - getCollisionLeft ()))
    { // o left of this
      ca_left = getCollisionLeft ();
      ca_right = o.getCollisionRight ();
    }
    else //if ((getCollisionRight () >= getCollisionLeft ()))
    { // o right of this
      ca_left = o.getCollisionLeft ();
      ca_right = getCollisionRight ();
    }
    
    // TOP AND BOTTOM OVERLAP
    double ca_top = 0, ca_bottom = 0;
    // one interval inside the other
    if (((o.getCollisionTop () >= getCollisionTop ()) && (o.getCollisionBottom () <= getCollisionBottom ())))
    { // o inside this
      ca_top = o.getCollisionTop ();
      ca_bottom = o.getCollisionBottom ();
    }
    else if (((getCollisionTop () >= o.getCollisionTop ()) && (getCollisionBottom () <= o.getCollisionBottom ())))
    { // this inside o
      ca_top = getCollisionTop ();
      ca_bottom = getCollisionBottom ();
    }
    // partial overlap
    else if ((getCollisionBottom () - o.getCollisionTop ()) > (o.getCollisionBottom () - getCollisionTop ()))
    { // o above this
      ca_top = getCollisionTop ();
      ca_bottom = o.getCollisionBottom ();
    }
    else //if ((getCollisionBottom () >= getCollisionTop ()))
    { // o below this
      ca_top = o.getCollisionTop ();
      ca_bottom = getCollisionBottom ();
    }
    
    // VERTICAL COLLISION IF COLLISION AREA WIDER THAN IT IS TALL
    if ((ca_right - ca_left) >= (ca_bottom - ca_top))
    {
      if ((o.movingUp ()) && (withinInterval (o.getCollisionTop (), getCollisionTop ()-buffer, getCollisionBottom ())))
      {
        if (reflect)
          o.reflectY ();
        return new ScreenCollision(this, ScreenObject.BOTTOMCOLLISION, (ca_right+ca_left)/2, (ca_top+ca_bottom)/2, ca_right-ca_left);
      }
      if ((o.movingDown ()) && (withinInterval (o.getCollisionBottom (), getCollisionTop (), getCollisionBottom ()+buffer)))
      {
        if (reflect)
          o.reflectY ();
        return new ScreenCollision(this, ScreenObject.TOPCOLLISION, (ca_right+ca_left)/2, (ca_top+ca_bottom)/2, ca_right-ca_left);
      }
    }
    else
    {
      if ((o.movingLeft ()) && (withinInterval (o.getCollisionLeft (), getCollisionLeft ()-buffer, getCollisionRight ())))
      {
        if (reflect)
          o.reflectX ();
        return new ScreenCollision(this, ScreenObject.RIGHTCOLLISION, (ca_right+ca_left)/2, (ca_top+ca_bottom)/2, ca_bottom-ca_top);
      }
      if ((o.movingRight ()) && (withinInterval (o.getCollisionRight (), getCollisionLeft (), getCollisionRight ()+buffer)))
      {
        if (reflect)
          o.reflectX ();
        return new ScreenCollision(this, ScreenObject.LEFTCOLLISION, (ca_right+ca_left)/2, (ca_top+ca_bottom)/2, ca_bottom-ca_top);
      }
    }
    return new ScreenCollision(this, ScreenObject.NOCOLLISION);
  }
  
  
  private boolean withinInterval (double target, double low, double high)
  {
    return ((target >= low) && (target <= high));
  }
}


