import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import GameApplet.ScreenObject;

public class ScoreBoard extends ScreenObject
{
  private int top, left, bottom, right;
  public Color backgroundColor = new Color(32, 32, 32);
  private final int littleBrickWidth = 15, littleBrickHeight = 7;
  private final int multihitIncrement = 10;
  final int baseScore = 100; // points scored on first brick hit off the paddle at full speed
  private Color ballColor, brickColor;
  private Color overMaxSpeedColor = Color.red, maxSpeedColor = Color.yellow;
  private boolean waitForStarRush = false, gameOver = true, mute = false;
  private String levelName;
  private int score, level, multihit, balls;
  private int maxLevel = 0;
  private Ball ball;
  private double oldSpeedBarWidth = 0;
  private boolean pillNextTime = false;
  
  public ScoreBoard (Ball b, int t, int l, int height, int width, Color text, Color ballcolor, Color brick, Color bc)
  {
    ball = b;
    top = t;
    left = l;
    bottom = top + height - 1;
    right = left + width - 2;
    defaultColor = text;
    ballColor = ballcolor;
    brickColor = brick;
    backgroundColor= bc;
    reset ();
  }
  
  
  
  // ACCESSORS
  
  public void reset ()
  {
    score = 0;
    level = 1;
    multihit = 0;
    balls = 5;
    levelName = " ";
    requestRepaint ();
  }
  
  
  public void setBall (Ball b)
  {
    ball = b;
  }
  
  public void noPillNextTime()
  {
    pillNextTime = false;
  }
  
  public void pillNextTime()
  {
    pillNextTime = true;
  }
  
  public boolean pillThisTime()
  {
    return pillNextTime;
  }
  
  // gameover
  public void gameOver ()
  {
    gameOver = true;
  }
  
  
  public void gameStarts ()
  {
    gameOver = false;
  }
  
  
  public boolean isGameOver ()
  {
    return gameOver;
  }
  
  
  //mute
  public void mute ()
  {
    mute = true;
    requestRepaint ();
  }
  
  
  public void unMute ()
  {
    mute = false;
    requestRepaint ();
  }
  
  
  public boolean isGameMuted ()
  {
    return mute;
  }
  
  
  // multihit
  public void incrementMultihit ()
  {
    multihit++;
    requestRepaint ();
  }
  
  
  public void clearMultihit ()
  {
    multihit = 0;
    requestRepaint ();
  }
  
  
  // balls
  public void addBall ()
  {
    balls++;
    requestRepaint ();
  }
  
  
  public void removeBall ()
  {
    balls--;
    if (balls < 0)
      gameOver = true;
    requestRepaint ();
  }
  
  
  // waitForStarRush
  public void waitingForStarRush ()
  {
    waitForStarRush = true;
    requestRepaint ();
  }
  
  
  public void notWaitingForStarRush ()
  {
    waitForStarRush = false;
    requestRepaint ();
  }
  
  
  // levelName
  public void setLevelName (String ln)
  {
    levelName = ln;
    requestRepaint ();
  }
  
  
  // score
  public void add (int a)
  {
    score += a;
    requestRepaint ();
  }
  
  
  public void hitBrick ()
  {
    if (!gameOver)
    {
      score += baseScore * ball.currentSpeed ();
      if (multihit > 1)
        score += (multihit - 1) * multihitIncrement;
      score -= score % 5;
    }
    multihit++;
    requestRepaint ();
  }
  
  
  public void bulletHitBrick ()
  {
    if (!gameOver)
    {
      score += baseScore;
      requestRepaint ();
    }
  }
  
  
  // level
  public void incrementLevel ()
  {
    level++;
    if (gameOver && (level > maxLevel))
      level = 1;
    if (!gameOver)
      balls++;
    requestRepaint ();
  }
  
  
  public void chooseNextLevel ()  // used when choosing level at start of game
  {
    level++;
    if (level > maxLevel)
      level = 1;
    requestRepaint ();
  }
  
  
  public void decrementLevel ()
  {
    if (level > 1)
    {
      level--;
      requestRepaint ();
    }
  }
  
  
  public void setLevel (int l)
  {
    level = l;
    requestRepaint ();
  }
  
  
  public int getLevel ()
  {
    return level;
  }
  
  
  public void setMaxLevel (int m)
  {
    maxLevel = m;
  }
  
  
  public int getMaxLevel ()
  {
    return maxLevel;
  }
  
  
  // left, right, top, bottom
  public double getLeft ()
  {
    return left;
  }
  
  
  public double getRight ()
  {
    return right;
  }
  
  
  public double getTop ()
  {
    return top;
  }
  
  
  public double getBottom ()
  {
    return bottom;
  }
  
  
  public void smartRepaintSpeedBar ()
  {
    int speedBarWidth = (int) (80 * ball.getVelocity ().getMagnitude () / ball.absoluteMaxSpeed ());
    if (speedBarWidth != oldSpeedBarWidth)
      requestRepaint ();
  }
  
  
  // paint
  public void paint (Graphics g)
  {
		g.setFont(new Font("SansSerif",Font.PLAIN, 12));
	  g.setColor(backgroundColor);
	g.fillRect(left+1, top+1, right-left, bottom-top);
    // the score
    g.setColor (defaultColor);
    g.drawString ("Score: " + score, left + 10, top + 30);
    
    // level
    if (waitForStarRush)
      g.drawString ("Level: ", left + 10, top + 45);
    else
      g.drawString ("Level: " + level, left + 10, top + 45);
    g.setColor (ballColor);
    g.drawString (levelName, left + 10, top + 60);
    
    // speed bar
    g.setColor (defaultColor);
    g.drawString ("Speed", left + 10, top + 93);
    g.drawRect (left + 10, top + 100, 80, 20);
    int speedBarWidth = (int) (80 * ball.getVelocity ().getMagnitude () / ball.absoluteMaxSpeed ());
    int maxSpeedBar = (int) (80 * ball.maxSpeed () / ball.absoluteMaxSpeed ());
    oldSpeedBarWidth = speedBarWidth;
    if (speedBarWidth > maxSpeedBar)
    {
      g.setColor (ballColor);
      g.fillRect (left + 10 + 1, top + 100 + 1, maxSpeedBar, 20 - 1);
      g.setColor (overMaxSpeedColor);
      g.fillRect (left + 10 + maxSpeedBar, top + 100 + 1, speedBarWidth - maxSpeedBar - 1, 20 - 1);
      g.setColor (defaultColor);
    }
    else
    {
      g.setColor (ballColor);
      g.fillRect (left + 10 + 1, top + 100 + 1, speedBarWidth, 20 - 1);
      g.setColor (maxSpeedColor);
      g.drawLine (left + 10 + maxSpeedBar, top + 100 + 1, left + 10 + maxSpeedBar, top + 100 + 20 - 1);
    }
//    g.setColor (defaultColor);
//    g.drawString (new Integer ((int) (baseScore * ball.currentSpeed ()) - (int) (baseScore * ball.currentSpeed ()) % 5).toString (), left + 11, top + 115);
    
    // pill next time
    //g.setColor (maxSpeedColor);
    //if (pillNextTime)
    //  g.drawRoundRect (left + 110, top + 105, 20, 10, 4, 4);
    
    // balls remaining
    g.setColor (defaultColor);
    g.drawString ("Balls Left", left + 10, top + 150);
    g.setColor (ballColor);
    for (int i = 0 ; i < Math.min (balls, 9 * 3 - 1) ; i++)
      g.fillOval (left + 10 + (i % 9) * ball.getNormalRadius () * 3, top + 155 + 15 * (int) (i / 9), ball.getNormalRadius () * 2, ball.getNormalRadius () * 2);
    if (balls > 9 * 3)
      for (int i = 0 ; i < 2 ; i++)
      g.fillOval (left + 10 + (26 % 9) * ball.getNormalRadius () * 3 + i * (ball.getNormalRadius () * 2 / 3 + 2) + 1, top + 155 + 15 * (int) (26 / 9) + ball.getNormalRadius () * 2 / 3, ball.getNormalRadius () * 2 / 3 + 1, ball.getNormalRadius () * 2 / 3 + 1);
    else if (balls == 9 * 3)
      g.fillOval (left + 10 + (26 % 9) * ball.getNormalRadius () * 3, top + 155 + 15 * (int) (26 / 9), ball.getNormalRadius () * 2, ball.getNormalRadius () * 2);
    
    // multibounce indicator
    g.setColor (defaultColor);
    g.drawString ("MultiBounce Bonus", left + 10, top + 210);
    g.setColor (brickColor);
    for (int i = 0 ; i < Math.min (multihit, 8 * 7 - 1) ; i++)
      g.fillRect (left + 10 + (i % 8) * (littleBrickWidth + 1), top + 220 + (littleBrickHeight + 1) * (int) (i / 8), littleBrickWidth, littleBrickHeight);
    if (multihit - 1 > 8 * 7)
      for (int i = 0 ; i < 3 ; i++)
      g.fillRect (left + 10 + ((8 * 7 - 1) % 8) * (littleBrickWidth + 1) + i * (littleBrickWidth / 3) + 1, top + 220 + (littleBrickHeight + 1) * 6 + 1, littleBrickWidth / 3 - 2, littleBrickHeight - 2);
    else if (multihit - 1 == 8 * 7)
      g.fillRect (left + 10 + (55 % 8) * (littleBrickWidth + 1), top + 220 + (littleBrickHeight + 1) * (int) (55 / 8), littleBrickWidth, littleBrickHeight);
    
    g.setColor (defaultColor);
    if ((multihit > 1) && (!gameOver))
      g.drawString ("+" + (multihitIncrement * (multihit - 1)), left + 120, top + 210);
    else
        g.drawString ("+0", left + 120, top + 210);
    
    if (mute)
      g.drawString ("-mute-", left + (right - left) - 40, bottom - 15);
  }
}
