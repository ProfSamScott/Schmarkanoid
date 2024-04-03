import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import GameApplet.AnimatedScreenObject;
import GameApplet.ScreenRectangle;

//Class: Ball
//Author: Sam Scott
//Date: April 19, 2006
//- handles ball objects for Schmarkanoid


public class Ball extends AnimatedScreenObject
{
	// PRIVATE VARIABLES
	Color defaultBallColor = new Color (128, 255, 0);
	Color superBallHaloColor = Color.gray;
	private final int normalRadius = 5; // size of ball
	private double innerRadius = normalRadius;
	private double superBallAnimationSpeed = 0.03;
	final double absoluteMaxSpeed = 1.0; // no faster than this! all levels must be 10% less at first
	final double jumpKickSlowDown = 0.00004; // slowdown rate after jump paddle increases spead above maximum
	private double radius = normalRadius;
	private double targetRadius = normalRadius;
	private double ballAnimationSpeed = 0.05;
	private boolean noBall = false;       // prevents drawing of ball when true
	private boolean superBall = false; // true if in superBall state
	private boolean inPlay = false;    // true if ball is in the air
	private double maxSpeed;           // maximum speed possible
	private double speedIncrement;     // size of speed increment
	private double initialSpeed;       // resets to this speed
	private boolean ballCaught = false;
	private BrickBoard bricks;
	private ScoreBoard score;
	double ballOnPaddleX = 0;
	boolean ballLaunch = false;
	boolean topIncrement = false; // has the ball hit the top of the screen yet?
	// audio
	private AudioClip edgeSoundNormal;
	private AudioClip edgeSoundLow;
	private AudioClip edgeSoundHigh;
	private boolean edgeSoundLowSet = false;
	private boolean edgeSoundHighSet = false;

	// CONSTRUCTORS
	public Ball ()
	{
		super ();
		setFrameRate(75);
	}


	public Ball (BrickBoard b, ScoreBoard s)
	{
		super ();
		bricks = b;
		score = s;
		setRepaintBuffer (2);
		setDefaultColor (defaultBallColor);
		setFrameRate(75);
	}


	public Ball (BrickBoard b, ScoreBoard s, Color c)
	{
		super ();
		bricks = b;
		score = s;
		setRepaintBuffer (2);
		setDefaultColor (c);
		setFrameRate(150);
	}

	public Ball makeCopy()
	{
		Ball b = new Ball(bricks, score, defaultBallColor);
		b.superBallHaloColor = superBallHaloColor;
		//b.normalRadius = normalRadius;
		b.innerRadius = innerRadius;
		b.superBallAnimationSpeed = superBallAnimationSpeed;
		//b.absoluteMaxSpeed = absoluteMaxSpeed;
		//b.jumpKickSlowDown = jumpKickSlowDown;
		b.radius = radius;
		b.targetRadius = targetRadius;
		b.ballAnimationSpeed = ballAnimationSpeed;
		b.noBall = noBall;
		b.superBall = superBall;
		b.inPlay = inPlay;
		b.maxSpeed = maxSpeed;
		b.speedIncrement = speedIncrement;
		b.initialSpeed = initialSpeed;
		b.ballCaught = ballCaught;
		b.ballOnPaddleX = ballOnPaddleX;
		b.ballLaunch = ballLaunch;
		b.edgeSoundNormal = edgeSoundNormal;
		b.edgeSoundHigh = edgeSoundHigh;
		b.edgeSoundLow = edgeSoundLow;
		b.edgeSoundLowSet = edgeSoundLowSet;
		b.edgeSoundHighSet = edgeSoundHighSet;
		b.topIncrement = topIncrement;
		// AnimatedScreenObject Stuff
		b.objectAnimated = objectAnimated;
		// MovingScreenObject Stuff
		b.v = v.makeCopy();
		b.a = a.makeCopy();
		b.updateDelay = updateDelay;
		b.objectMoving = objectMoving;
		synchronized(edges)
		{
			Iterator<ScreenRectangle> itr = edges.iterator();
			while (itr.hasNext())
				b.edges.add((itr.next()).makeCopy());
		}
		b.edgeSound = edgeSound;
		b.edgeSoundSet = edgeSoundSet;
		b.mute = mute;
		b.pause = pause;
		// ScreenObject stuff
		b.defaultColor = defaultColor;
		b.repaint = repaint;
		b.repaintBuffer = repaintBuffer;
		b.oldLeft = oldLeft;
		b.oldRight = oldRight;
		b.oldTop = oldTop;
		b.oldBottom = oldBottom;
		b.oldX = oldX;
		b.oldY = oldY;
		// ScreenAttribute Stuff
		b.x = x;
		b.y = y;
		return b;
	}
	public void ballCaught()
	{
		ballCaught = true;
	}
	public void ballNotCaught()
	{
		ballCaught = false;
	}
	public boolean isBallCaught()
	{
		return ballCaught;
	}
	// RESET BALL
	public void reset ()
	{
		stopMoving ();
		powerDown ();
		notInPlay ();
	}


	// BALL IN PLAY
	public void notInPlay ()
	{
		inPlay = false;
	}


	public void inPlay ()
	{
		inPlay = true;
	}


	public boolean isInPlay ()
	{
		return inPlay;
	}


	// SPEED & DIRECTION
	public void incrementSpeed (int n)
	{
		if (v.getMagnitude () < maxSpeed)
		{
			v.setMagnitude (v.getMagnitude () + speedIncrement * n);
			if (v.getMagnitude () > maxSpeed)
				v.setMagnitude (maxSpeed);
		}
	}


	public void decrementSpeed (int n)
	{
		v.setMagnitude (v.getMagnitude () - speedIncrement * n);
		if (v.getMagnitude () < initialSpeed)
			v.setMagnitude (initialSpeed);
	}


	public void resetSpeed ()
	{
		v.setMagnitude (initialSpeed);
	}


	public void randomDirection ()
	{
		double newDirection;
		do
			newDirection = Math.random () * 360;
		while ((Math.abs (newDirection) < 10) || (Math.abs (newDirection - 180) < 10) || (Math.abs (newDirection - 360) < 10));
		v.setDirection (newDirection);
	}


	public void serve ()  // throws ball straight down onto paddle
	{
		v.setDirection (270);
		startMoving ();
	}


	public double getAbsoluteMaxSpeed ()
	{
		return absoluteMaxSpeed ();
	}


	// VISIBILITY
	public void invisible ()
	{
		noBall = true;
	}


	public void visible ()
	{
		noBall = false;
	}


	public boolean isVisible ()
	{
		return !noBall;
	}


	// POWERUPS
	public void powerDown ()
	{
		superBall = false;
		targetRadius = normalRadius;
		startAnimating ();
		if (edgeSoundSet)
			edgeSound = edgeSoundNormal;
	}


	public void superBallOn ()
	{
		superBall = true;
		targetRadius = normalRadius;
		innerRadius = radius;
		startAnimating ();
		if (edgeSoundSet)
			edgeSound = edgeSoundNormal;
	}


	public void superBallOff ()
	{
		superBall = false;
		radius = innerRadius;
		targetRadius = normalRadius;
		startAnimating();
	}


	public boolean superBall ()
	{
		return superBall;
	}


	public void shrink ()
	{
		targetRadius = normalRadius * 5.0 / 9.0;
		startAnimating ();
		if (edgeSoundHighSet)
			edgeSound = edgeSoundHigh;
	}



	public void expand ()
	{
		targetRadius = normalRadius * 3;
		startAnimating ();
		if (edgeSoundLowSet)
			edgeSound = edgeSoundLow;
	}


	public boolean bigBall ()
	{
		return (radius > normalRadius);
	}


	public boolean littleBall ()
	{
		return (radius < normalRadius);
	}


	// SOUNDS
	public void setEdgeSound (AudioClip es)  // overriding MovingScreenObject
	{
		edgeSound = es;
		edgeSoundSet = true;
		edgeSoundNormal = edgeSound;
	}

	public AudioClip getEdgeSound()
	{
		return edgeSound;
	}


	public void setLowEdgeSound (AudioClip es)
	{
		edgeSoundLow = es;
		edgeSoundLowSet = true;
	}

	public AudioClip getLowEdgeSound ()
	{
		return edgeSoundLow;
	}

	public void setHighEdgeSound (AudioClip es)
	{
		edgeSoundHigh = es;
		edgeSoundHighSet = true;
	}

	public AudioClip getHighEdgeSound ()
	{
		return edgeSoundHigh;
	}
	// MOVE, PAINT
	public void paint (Graphics g)
	{
		g.setColor (getDefaultColor ());
		if (!noBall)
			if (!superBall)
				g.fillOval ((int) x - (int) radius, (int) y - (int) radius, (int) radius * 2, (int) radius * 2);
			else
			{
				//g.setColor(superBallHaloColor);
				//g.drawOval ((int) x - (int) (radius+2), (int) y - (int) (radius+2), (int) (radius+2) *2-1, (int) (radius+2) *2-1);
				//g.setColor(defaultBallColor);
				g.fillOval ((int) x - (int) innerRadius, (int) y - (int) innerRadius, (int) innerRadius * 2, (int) innerRadius * 2);
				//g.drawOval ((int) x - (int) radius + 1, (int) y - (int) radius + 1, ((int) radius - 1) * 2, ((int) radius - 1) * 2);
				//g.drawOval ((int) x - (int) radius / 2, (int) y - (int) radius / 2, (int) radius / 2 * 2, (int) radius / 2 * 2);
				//g.fillOval ((int) x - (int) innerRadius, (int) y - (int) innerRadius, (int) innerRadius*2, (int) innerRadius*2);
			}
	}


	public void animateOneStep ()
	{
		if (radius < targetRadius)
		{
			radius += ballAnimationSpeed;
			if (radius >= targetRadius)
				radius = targetRadius;
		}
		else if (radius > targetRadius)
		{
			radius -= ballAnimationSpeed;
			if (radius <= targetRadius)
				radius = targetRadius;
		}
		else if (superBall)
		{
			innerRadius += superBallAnimationSpeed;
			if ((innerRadius<=1 && superBallAnimationSpeed < 0)|| (innerRadius >= normalRadius && superBallAnimationSpeed > 0))
				superBallAnimationSpeed *= -1;
		}
		else
			stopAnimating ();
		requestRepaint ();
	}


	public void collisionCheck ()
	{
		bricks.detectCollisions (this, score);
	}


	// ACCESSORS
	public double getLeft ()
	{
		return x - radius;
	}


	public double getRight ()
	{
		return x + radius;
	}


	public double getTop ()
	{
		return y - radius;
	}


	public double getBottom ()
	{
		return y + radius;
	}


	public double getCollisionLeft ()
	{
		return x - radius * 4 / 5;
	}


	public double getCollisionRight ()
	{
		return x + radius * 4 / 5;
	}


	public double getCollisionTop ()
	{
		return y - radius * 4 / 5;
	}


	public double getCollisionBottom ()
	{
		return y + radius * 4 / 5;
	}


	public int getRadius ()
	{
		return (int) radius;
	}


	public int getNormalRadius ()
	{
		return (int) normalRadius;
	}


	public void setMaxSpeed (double m)
	{
		maxSpeed = m;
		if (maxSpeed > absoluteMaxSpeed * 0.9)
			maxSpeed = absoluteMaxSpeed * 0.9;
		if (initialSpeed > maxSpeed)
			initialSpeed = maxSpeed;
	}


	public double maxSpeed ()
	{
		return maxSpeed;
	}


	public void setSpeedIncrement (double s)
	{
		speedIncrement = s;
	}

	public double getSpeedIncrement ()
	{
		return speedIncrement;
	}

	public double speedIncrement ()
	{
		return speedIncrement;
	}


	public void setInitialSpeed (double i)
	{
		initialSpeed = i;
		if (initialSpeed > maxSpeed)
			initialSpeed = maxSpeed;
	}


	public double initialSpeed ()
	{
		return initialSpeed;
	}


	public double absoluteMaxSpeed ()
	{
		return absoluteMaxSpeed;
	}
}

