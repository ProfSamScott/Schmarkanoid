// ScreenAttribute
// - top of the graphics and movement stuff for the gameApplet package.
// Created: Sam Scott, April 17, 2006
// Modified:
//
// SamScott@Canada.com
package GameApplet;

public class ScreenAttribute
{
  protected double x, y;
  
  // constructors
  public ScreenAttribute (double initialX, double initialY)
  {
    x = initialX;
    y = initialY;
  }
  
  
  public ScreenAttribute ()
  {
    x = 0;
    y = 0;
  }
  
  
  //accessor methods
  public double getX ()
  {
    return x;
  }
  
  
  public double getY ()
  {
    return y;
  }
  
  
  public void setX (double newX)
  {
    x = newX;
  }
  
  
  public void setY (double newY)
  {
    y = newY;
  }
}

