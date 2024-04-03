// VelocityVector
// - defines a velocity vector.
// Created: Sam Scott, April 17, 2006
// Modified:
//
// SamScott@Canada.com
package GameApplet;

public class ScreenVector extends ScreenAttribute
{
  protected double magnitude = 0;
  protected double direction = 0; // 0 = right, 90 = up, 180 = left, 270 = down
  
  public ScreenVector ()
  {
    super ();
  }
  
  
  public ScreenVector (double x, double y)
  {
    super (x, y);
  }
  
  public void setVector(double newX, double newY)
  {
	x = newX;
	y = newY;
	magnitude = Math.sqrt(x*x+y*y);
	scale();
  }

  // accessors
  public double getMagnitude ()
  {
    return magnitude;
  }
  
  
  public void setMagnitude (double newMagnitude)
  {
    magnitude = newMagnitude;
    scale ();
  }
  
  
  public double getDirection ()
  {
    computeDirection ();
    return direction;
  }
  
  
  public void setDirection (double newDirection)
  {
    direction = newDirection;
    while (direction > 360)
      direction -= 360;
    while (direction < 0)
      direction += 360;
    redirectVector ();
  }
  
  
  public void zero ()  // should this reset magnitude and direction instead?
  {
    x = 0;
    y = 0;
    magnitude = 0;
  }
  
  
  // reflections
  public void reflectX ()
  {
    x = -x;
    computeDirection ();
  }
  
  
  public void reflectY ()
  {
    y = -y;
    computeDirection ();
  }
  
  
  // computing direction
  protected void redirectVector ()
  {
    double directionRadians = direction / 180 * Math.PI;
    
    if (direction == 90)
    {
      y = -1;
      x = 0;
    }
    else if (direction == 270)
    {
      y = 1;
      x = 0;
    }
    else
    {
      y = Math.tan (directionRadians);
      if ((direction >= 0) && (direction < 90)) // first quad
        x = 1;
      else if ((direction > 90) && (direction <= 180)) // second quad
      {
        x = -1;
        y *= -1;
      }
      else if ((direction > 180) && (direction < 270)) // third quad
        x = -1;
      else // fourth quad
      {
        x = 1;
        y *= -1;
      }
    }
    
    if (direction < 180)
      y *= -1;
    
    scale ();
  }
  
  
  protected void computeDirection ()
  {
    double newDirection;
    if (x == 0)
      newDirection = 90;
    else
    {
      newDirection = Math.atan (-y / x);
      newDirection = newDirection / Math.PI * 180;
    }
    if (newDirection < 0)
      newDirection += 180;
    
    if (y > 0)
      newDirection += 180;
    
    direction = newDirection;
  }
  
  
  // normalize the vector
  public void normalize ()
  {
    double length = Math.sqrt (x * x + y * y);
    if (length > 0)
    {
      x = x / length;
      y = y / length;
    }
    computeDirection ();
  }
  
  
  // scale vector according to magnitude
  public void scale ()
  {
    normalize ();
    x = x * magnitude;
    y = y * magnitude;
  }
}
