// VelocityVector
// - defines a velocity vector.
// Created: Sam Scott, April 17, 2006
// Modified:
//
// SamScott@Canada.com
package GameApplet;

public class ScreenVelocityVector extends ScreenVector
{
  private final double yMotionCompression = 0.75; // necessary to compensate for non-square pixels
  
  public ScreenVelocityVector ()
  {
    super ();
  }
  
  
  public ScreenVelocityVector (double x, double y)
  {
    super (x, y);
  }
  
  public ScreenVelocityVector makeCopy()
  {
	  ScreenVelocityVector v = new ScreenVelocityVector(x,y);
	  // ScreenVector stuff
	  v.magnitude = magnitude;
	  v.direction = direction;
	  return v;
  }
  
  // move a point one step
  public void movePoint (ScreenPoint p)
  {
    p.setX (p.getX () + x);
    p.setY (p.getY () + y * yMotionCompression);
  }
}
