// AccelerationVector
// - defines an acceleration vector.
// Created: Sam Scott, April 17, 2006
// Modified:
//
// SamScott@Canada.com
package GameApplet;

public class ScreenAccelerationVector extends ScreenVector
{
  // one step of velocity adjustment
  public void adjustVelocity (ScreenVelocityVector v)
  {
    double newX = v.getX () + x;
    double newY = v.getY () + y;
    double newMagnitude = Math.sqrt(newX*newX+newY*newY);
    v.setMagnitude(newMagnitude);
    v.setX(newX);
    v.setY(newY);
  }
  public ScreenAccelerationVector makeCopy()
  {
	  ScreenAccelerationVector v = new ScreenAccelerationVector();
	  // ScreenVector stuff
	  v.magnitude = magnitude;
	  v.direction = direction;
	  // ScreenAttribute stuff;
	  v.x = x;
	  v.y = y;
	  return v;
  }
}
