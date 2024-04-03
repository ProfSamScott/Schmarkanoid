// Collision
// Modified:
//
// SamScott@Canada.com
package GameApplet;

public class ScreenCollision extends ScreenAttribute
{
  protected double strength = 1;
  private int type = ScreenObject.NOCOLLISION;
  private ScreenObject hitObject;
  
  // constructors
  public ScreenCollision (ScreenObject s, int t)
  {
    type = t;
    hitObject = s;
  }
  
  
  public ScreenCollision (ScreenObject s, int t, double initialX, double initialY)
  {
    super (initialX, initialY);
    type = t;
    hitObject = s;
  }
  
  
  public ScreenCollision (ScreenObject s, int t, double initialX, double initialY, double st)
  {
    super (initialX, initialY);
    strength = st;
    type = t;
    hitObject = s;
  }
  
  
  //accessor methods
  public double getStrength ()
  {
    return strength;
  }
  
  
  public int getType ()
  {
    return type;
  }
  
  
  public ScreenObject getHitObject ()
  {
    return hitObject;
  }
}

