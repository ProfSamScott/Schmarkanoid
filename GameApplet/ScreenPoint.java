// ScreenPoint
// - implements a single point object.
// Created: Sam Scott, April 17, 2006
// Modified:
//
// SamScott@Canada.com

package GameApplet;

import java.awt.Color;
import java.awt.Graphics;


public class ScreenPoint extends ScreenObject
{
  public ScreenPoint ()
  {
    super ();
  }
  
  
  public ScreenPoint (double x, double y)
  {
    super (x, y);
  }
  
  
  public ScreenPoint (double x, double y, Color c)
  {
    super (x, y, c);
  }
  
  
  // implement the accessors from ScreenObject
  public double getLeft ()
  {
    return x;
  }
  
  
  public double getRight ()
  {
    return x;
  }
  
  
  public double getTop ()
  {
    return y;
  }
  
  
  public double getBottom ()
  {
    return y;
  }
  
  
  // paint the object
  public void paint (Graphics g)
  {
    g.setColor (defaultColor);
    g.drawLine ((int) x, (int) y, (int) x, (int) y);
    //System.out.println(x+" "+y+" "+defaultColor.toString());
  }
  
  
  // call repaint for object if necessary
  //   public void smartRepaint (Applet a)
  //   {
  //       if (((int) oldX != (int) x) || ((int) oldY != (int) y))
  // {
  //     a.repaint ((int) x, (int) y, 1, 1);
  //      a.repaint ((int) oldX, (int) oldY, 1, 1);
  
  //  }
}



