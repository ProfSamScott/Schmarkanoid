import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;

import GameApplet.GameCanvas;
import GameApplet.ScreenCollision;
import GameApplet.ScreenObject;

public class BrickBoard extends ScreenObject
{
  private int numRows = 10, numCols = 15;  // total number of rows and columns
  private int rowInc = 15, colInc;  // depth of row & width of column in pixels
  private int left, right, top, bottom; // dimensions of the brick board
  private int numBricks; // number of bricks on screen
  private Brick bricks[] [] = new Brick [numRows] [numCols]; // array to store bricks all
//  private final int fullBlock = rowInc + 4; // the constant for a regular block
  
  // CONSTRUCTOR
  public BrickBoard (int t, int l, int r)
  {
    top = t;
    bottom = numRows * (rowInc + 1) + top - 1;
    left = l;
    right = r;
    colInc = (right - left) / numCols;
    
    for (int row = 0 ; row < numRows ; row++)
      for (int col = 0 ; col < numCols ; col++)
      bricks [row] [col] = new Brick ((double) (col * colInc + 1), (double) (top + row * rowInc + 1), (double) (colInc - 2), (double) (rowInc - 2), Brick.NOBRICK);
  }
  
  
  public void blankOutBoard ()
  {
    for (int row = 0 ; row < numRows ; row++)
      for (int col = 0 ; col < numCols ; col++)
      bricks [row] [col].changeType (Brick.NOBRICK);
  }
  
  
  public void loadBoard (SchmarkanoidLevel newLevel)
  {
    for (int row = 0 ; row < numRows ; row++)
      for (int col = 0 ; col < numCols ; col++)
      bricks [row] [col] = new Brick (col * colInc + 1, top + row * rowInc + 1, colInc - 2, rowInc - 2, newLevel.getBlock (row, col));
    // count the bricks
    numBricks = 0;
    for (int row = 0 ; row < numRows ; row++)
      for (int col = 0 ; col < numCols ; col++)
    {
      if (bricks [row] [col].type () != Brick.NOBRICK)
      {
        if (bricks [row] [col].type () != Brick.INDESTRUCTIBLE)
          numBricks++;
      }
    }
  }
  
  
  public void destroyBoard ()
  {
    for (int row = 0 ; row < numRows ; row++)
      for (int col = 0 ; col < numCols ; col++)
    {
      bricks [col] [row].stopThread ();
      bricks [col] [row] = null;
    }
  }
  
  
  public Brick getBrick (int row, int col)
  {
    return bricks [row] [col];
  }
  
  
  private int getCol (double xCoord)
  {
    return Math.min (numCols - 1, Math.max (0, (int) (xCoord - left) / colInc));
  }
  
  
  private int getRow (double yCoord)
  {
    return Math.min (numRows - 1, Math.max (0, (int) (yCoord - top) / rowInc));
  }
  
  
  public Brick bulletCollision (Bullet bullet, ScoreBoard score)
  {
    ScreenCollision collision = null;
    Brick hitBrick = null;
    int col = getCol (bullet.getCollisionLeft ());
    int row = getRow (bullet.getCollisionTop ());
    if ((bricks [row] [col].type () != Brick.EXPLODING) && (bricks [row] [col].type () != Brick.NOBRICK))
    {
      collision = bricks [row] [col].detectCollision (bullet, false);
      if (collision.getType () != ScreenObject.NOCOLLISION)
      {
        bullet.explode ();
        if (((Brick) (collision.getHitObject ())).type () == Brick.NORMAL)
        {
          ((Brick) collision.getHitObject ()).explode ();
          score.bulletHitBrick ();
          numBricks--;
          hitBrick = (Brick) collision.getHitObject ();
        }
      }
    }
    return hitBrick;
  }
  
  
  public void detectCollisions (Ball ball, ScoreBoard score)
  {
    boolean reflectX = false;                              // set to true if action should be performed
    boolean reflectY = false;
    ScreenCollision collision;                             // stores result of collision detection
    LinkedList<ScreenCollision> newCollisions = new LinkedList<ScreenCollision>  ();          // stores list of new collisions
    
    //  Iterator brickitr = activeBricks.iterator ();
    //  while (brickitr.hasNext ())
    for (int col = getCol (ball.getCollisionLeft ()) ; col <= getCol (ball.getCollisionRight ()) ; col++)
      for (int row = getRow (ball.getCollisionTop ()) ; row <= getRow (ball.getCollisionBottom ()) ; row++)
    {
      // get next brick
      //Brick currentBrick = (Brick) (brickitr.next ());
      Brick currentBrick = bricks [row] [col];
      
      if ((currentBrick.type () != Brick.NOBRICK) && (currentBrick.type () != Brick.EXPLODING))
      { // ask the brick if it collided with the ball
        collision = currentBrick.detectCollision (ball, false);
        
        // deal with collisions
        if (collision.getType () != ScreenObject.NOCOLLISION)
        {
          ball.addCollision (collision);   // add to the total list of collisions
          newCollisions.add (collision);    // add to list of new collisions
          if ((currentBrick.type () == Brick.NORMAL)
                || ((currentBrick.type () == Brick.LEFTONLY) && (collision.getType () == ScreenObject.LEFTCOLLISION))
                || ((currentBrick.type () == Brick.RIGHTONLY) && (collision.getType () == ScreenObject.RIGHTCOLLISION))
                || ((currentBrick.type () == Brick.TOPONLY) && (collision.getType () == ScreenObject.TOPCOLLISION))
                || ((currentBrick.type () == Brick.BOTTOMONLY) && (collision.getType () == ScreenObject.BOTTOMCOLLISION))
                )
          { // explosion!
            currentBrick.explode ();
            numBricks--;
            score.hitBrick ();
            ball.incrementSpeed (1);
          }
          else if (ball.superBall ())
          { // superball blasts through everything
            if ((currentBrick.type () == Brick.INDESTRUCTIBLE))
              numBricks++;
            numBricks--;
            currentBrick.explode ();
            score.hitBrick ();
            ball.incrementSpeed (1);
          }
          else
            // indestructible brick
            score.incrementMultihit ();
          //System.out.println (numBricks);
        }
      }
    }
    
    // deal with the new collisions (reflections, remove bricks from active list, etc.)
    if (!newCollisions.isEmpty () && !ball.superBall ())
    {
      Iterator<ScreenCollision> collisionitr = newCollisions.iterator ();
      ScreenCollision strongestHard = null; // only one reflection from an indestructible brick
      while (collisionitr.hasNext ())
      {
        ScreenCollision element = collisionitr.next ();
        if (((Brick) (element.getHitObject ())).type () == Brick.INDESTRUCTIBLE)
        {
          if (strongestHard == null)
            strongestHard = element;
          else if (strongestHard.getStrength () < element.getStrength ())
            strongestHard = element;
        }
        else
        {
          if ((element.getType () == ScreenObject.LEFTCOLLISION) || (element.getType () == ScreenObject.RIGHTCOLLISION))
            reflectX = true;
          else if ((element.getType () == ScreenObject.TOPCOLLISION) || (element.getType () == ScreenObject.BOTTOMCOLLISION))
            reflectY = true;
        }
      }
      if (strongestHard != null)
      {
        if ((strongestHard.getType () == ScreenObject.LEFTCOLLISION) || (strongestHard.getType () == ScreenObject.RIGHTCOLLISION))
          reflectX = true;
        else if ((strongestHard.getType () == ScreenObject.TOPCOLLISION) || (strongestHard.getType () == ScreenObject.BOTTOMCOLLISION))
          reflectY = true;
      }
      
      if (reflectX)
        ball.reflectX ();
      if (reflectY)
        ball.reflectY ();
    }
  }
  
  
  /*  private void removeExplodedBricks ()
   { // remove hit bricks from active list
   // there's got to be a better way to do this, but remove() didn't seem to work!
   int size = activeBricks.size ();
   for (int i = 0 ; i < size ; i++)
   {
   Brick b = (Brick) (activeBricks.removeFirst ());
   if ((b.type () == Brick.NORMAL) || (b.type () == Brick.INDESTRUCTIBLE))
   activeBricks.addLast (b);
   }
   }*/
  
  
  public int getNumBricks ()
  {
    // *** here's an embarassing workaround...
    numBricks=0;
    for (int row = 0 ; row < numRows ; row++)
      for (int col = 0 ; col < numCols ; col++)
    {
      if (bricks [row] [col].type () != Brick.NOBRICK)
      {
        if (bricks [row] [col].type () != Brick.INDESTRUCTIBLE)
          numBricks++;
      }
    }
    
    //*** multiple threads updating causing problems maybe?
    return numBricks;
  }
  
  
  // EVENTUALLY OBSOLETE? USED BY BULLETS RIGHT NOW...
  public void explodeBrick (int row, int col)
  {
    synchronized(bricks)
    {
      bricks [row] [col].explode ();
      numBricks--;
    }
  }
  
  
  public double getLeft ()
  {
    return (double) left;
  }
  
  
  public double getRight ()
  {
    return (double) right;
  }
  
  
  public double getTop ()
  {
    return (double) top;
  }
  
  
  public double getBottom ()
  {
    return (double) bottom;
  }
  
  
  public void smartRepaint (GameCanvas a)
  {
    for (int row = 0 ; row < numRows ; row++)
      for (int col = 0 ; col < numCols ; col++)
      bricks [row] [col].smartRepaint (a);
  }
  
  
  public void paint (Graphics g)
  {
    for (int row = 0 ; row < numRows ; row++)
      for (int col = 0 ; col < numCols ; col++)
      bricks [row] [col].paint (g);
  }
}


