import java.awt.Color;

import GameApplet.ScreenRectangle;


public class Bullet extends ScreenRectangle
{
  private static final int height = 5;
  private final static double speed = -0.8;
  private boolean dead = false;
  private Color backGroundColor;
  
  public Bullet (int xCoord, int yCoord, Color c, Color bg)
  {
    super (yCoord - height / 2, yCoord + height / 2 - 1, xCoord, xCoord + 1, c);
    backGroundColor = bg;
    setVelocity (0, speed);
    setFrameRate (1000);
    setRepaintBuffer (1);
    startThread ();
    startMoving ();
  }
  
  
  public void explode ()
  {
    stopMoving ();
    stopThread ();
    defaultColor = backGroundColor;
    dead = true;
  }
  
  
  public boolean dead ()
  {
    return dead;
  }
}
