import java.io.BufferedReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;



public class SchmarkanoidLevel
{
  int numRows = 10;
  int numCols = 15;
  private double initialSpeed;
  private double maxBallSpeed;
  private double ballSpeedIncrement;
  private String levelName;
  int[] [] blocks = new int [numRows] [numCols];
  boolean noLevel = false;
  
  SchmarkanoidLevel (Scanner levelReader)
  {
    try
    {
      levelName = levelReader.nextLine();
    }
    catch (NoSuchElementException e)
    {
      levelName = null;
      noLevel = true;
    }
    
    //System.out.println (levelName);
    if (levelName != null)
    {
      String[] rawBlocks = new String [numRows];
//      try
//      {
        for (int row = 0 ; row < numRows ; row++)
          rawBlocks [row] = levelReader.nextLine ();
//      }
//      catch (IOException e)
//      {
//        System.out.println ("IO Exception: Can't Read Blocks");
//      }
//      try
//      {
        initialSpeed = Double.parseDouble (levelReader.nextLine ());
        maxBallSpeed = Double.parseDouble (levelReader.nextLine ());
        ballSpeedIncrement = Double.parseDouble (levelReader.nextLine ());
//      }
//      catch (IOException e)
//      {
//        System.out.println ("IO Exception: Can't Read Parameters");
//      }
      
      for (int row = 0 ; row < numRows ; row++)
        for (int col = 0 ; col < numCols ; col++)
        if (rawBlocks [row].charAt (col) == '1')
        blocks [row] [col] = Brick.NORMAL;
      else if (rawBlocks [row].charAt (col) == '2')
        blocks [row] [col] = Brick.INDESTRUCTIBLE;
      else if (rawBlocks [row].charAt (col) == '3')
        blocks [row] [col] = Brick.LEFTONLY;
      else if (rawBlocks [row].charAt (col) == '4')
        blocks [row] [col] = Brick.RIGHTONLY;
      else if (rawBlocks [row].charAt (col) == '5')
        blocks [row] [col] = Brick.TOPONLY;
      else if (rawBlocks [row].charAt (col) == '6')
        blocks [row] [col] = Brick.BOTTOMONLY;
      else
        blocks [row] [col] = Brick.NOBRICK;
    }
    else
      noLevel = true;
  }
  public double getInitialSpeed ()
  {
    return initialSpeed;
  }
  public double getMaxBallSpeed ()
  {
    return maxBallSpeed;
  }
  public double getBallSpeedIncrement ()
  {
    return ballSpeedIncrement;
  }
  public String getLevelName ()
  {
    return levelName;
  }
  public boolean isLevelOK ()
  {
    return (!noLevel);
  }
  public int getBlock (int row, int col)
  {
    if ((row >= 0) && (row < numRows) && (col >= 0) && (col < numCols))
      return blocks [row] [col];
    else
      return 0;
  }
}

