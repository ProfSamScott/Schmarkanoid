import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TimerTask;
import java.util.Timer;

import GameApplet.GameApplet;
import GameApplet.GameAppletUpdater;
import GameApplet.GameCanvas;
import GameApplet.ScreenCollision;
import GameApplet.ScreenObject;
import GameApplet.ScreenRectangle;

//Schmarkanoid! Version 1.0
//<applet code="Schmarkanoid" width=600 height=400> </applet>

//Author: Sam Scott (SamScott@Canada.com)
//Date:   April, 2006

//License: Gnu or something?

//Objectify
//- bullets
//- power pills (a kind of Brick?)
//- paddle
//- planet
//- bottom and side separators

//***********************************************
//MENU OF COMMANDS
//***********************************************

//Mouse move  - moves the paddle when in game mode
//Mouse click - gets you into game mode
//- throws the ball

//'p' - toggles the paddle
//'m' - toggles mute
//'q' - quits the current game
//UP  - increase level (only before first throw)
//DWN - decrease level (only before first throw)
//LFT - move ball left (only before a throw)
//RGT - move ball right (only before a throw)

public class SchmarkanoidCanvas extends GameCanvas implements KeyListener,
		MouseListener, MouseMotionListener {
	// *******************************************************************
	// *******************************************************************
	// INSTANCE VARIABLE DICTIONARY
	// *******************************************************************
	// *******************************************************************

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// **********
	// COLORS
	// **********
	final Color paddleColor = new Color(160, 160, 255);
	final Color brickColor = new Color(200, 20, 0);
	final Color brickExplodeColor = Color.yellow;
	final Color ballColor = new Color(128, 255, 0);
	final Color defaultColor = Color.darkGray;
	final Color backgroundColor = Color.black;
	final Color borderColor = new Color(255, 160, 160);
	final Color textColor = Color.white;
	final Color planetColor = new Color(0, 0, 128);
	final Color extraBallColor = Color.white;
	final Color statusScreenColor = new Color(32, 32, 32);

	// **********
	// Game State
	// **********

	// constants
	final int countDownStart = 300; // how long to wait to end level after last
	// block hit
	final int pauseDuration = 2; // time delay in main thread (milliseconds)
	final int totalMusicClips = 10;
	int totalAudioClips = 40;
	// variables
	ScoreBoard score;
	int countDown = 0; // the countdown timer
	boolean gameOn = false; // true if ball lost without exploding any bricks
	boolean stopFlag = false; // used to stop main thread
	boolean levelChangeOK = false; // set to true before first throw (user can
	// select level)
	Thread t = null; // holds the main game thread
	//URL codeBase; // where's the code?
	//URL documentBase; // where's the document?
	int jumpForBallLaunchOnly = 0;
	LinkedList<SchmarkanoidLevel> levels = new LinkedList<SchmarkanoidLevel>(); // holds
	// the
	// list
	// of
	// levels
	// read
	// in
	// from
	// disk
	boolean levelReady = false;
	boolean pause = false;
	int audioClips = 0;
	boolean initDone = false;

	// **********
	// The Screen
	// **********

	// constants
	final int left = 0, right = 450, top = 50, bottom = 350; // the game
	// playing
	// surface
	final int planetAreaWidth = 50;
	final int appletWidth = 600, appletHeight = 400;
	final int statusWidth = 150; // the width of the status screen
	// variables
	boolean paddleMoved = false; // repaint paddle part of screen when true
	boolean planetMoved = false; // repaint planet when true
	boolean wholeScreen = true; // repaint whole screen
	boolean topStatus = false; // repaint the loading bar at the top
	boolean bricksUpdate = false; // repaint bricks that have changed
	GameAppletUpdater updater; // holds a thread that updates the screen
	ScreenRectangle topEdge; // rectangle object for the top edge of the
	// screen

	// ********
	// The Ball
	// ********

	// variables
	Ball ball; // the main ball!
	LinkedList<Ball> extraBalls = new LinkedList<Ball>();// the extra balls
	// (for multi-ball)
	// double ballOnPaddleX; // position of ball when on paddle
	int topHitSpeedIncrement; // how many increments when we hit the top (per
	// level) - also used for other speed jumps as
	// well
	double jumpKickSlowDown = 0.00005;
	double resumeSpeedProportion; // proportion of current speed to resume
	// after a lost ball (per level)
	double resumeSpeed; // speed to resume after losing ball
	boolean resume; // set to true when speed should be resumed on hitting
	// something
	// boolean topIncrement; // have we hit the top of the screen yet?
	// boolean ballLaunch = false;

	// **********
	// The Paddle
	// **********

	// constants
	final int paddleHeight = 25; // height of paddle
	final int normalPaddleWidth = 50, shrunkPaddleWidth = 25,
			expandedPaddleWidth = 100; // widths of various paddles
	final double paddleSoftener = 1.1; // for the round paddle, makes the edges
	// less harsh (don't go below 1.01)
	final int initialPaddleY = bottom - 15;
	final double catcherAnimationSpeed = 0.1;
	final int finalCatcherLength = 2;
	final double initialCatcherLength = 0;
	final double initialPaddleVV = -0.5; // initial vertical velocity (for
	// jumps)
	final double paddleVA = 0.01; // paddle vertical acceleration (for jumps)
	final int fullLaserWidth = 3; // width when lasers fully out
	final int initialLaserWidth = -1; // width when lasers fully retracted
	final double laserOutSpeed = 0.2; // speed that lasers fold in and out
	final int initialJumpPaddleHeight = paddleHeight / 3;
	final int finalJumpPaddleHeight = paddleHeight;
	final double jumpPaddleAnimationSpeed = 0.5;
	// variables
	int paddleWidth = normalPaddleWidth;
	int roundPaddleHeight = paddleHeight;
	int paddleX = (int) (Math.random() * (right - paddleWidth) + paddleWidth / 2); // paddle
	// position
	double paddleY = initialPaddleY;
	int targetPaddleWidth = paddleWidth; // for animating changes to the
	// paddle
	boolean roundPaddle = true; // toggle paddle type
	boolean freeze = false; // temporarily freeze the paddle during throw
	boolean ballCatcher = false; // true when paddle in catch mode
	boolean laserPaddle = true;
	boolean jumpPaddle = true;
	double paddleVV; // paddle vertical velocity
	double laserWidth = initialLaserWidth; // current laser width
	double jumpPaddleHeight = initialJumpPaddleHeight;
	double catcherLength = initialCatcherLength;

	// *******************
	// The Bricks
	// *******************

	// Constants *** THIS STUFF SHOULD BE OBSOLETE SOON! IT'S MEANT TO BE PART
	// OF BrickBoard
	// STILL USED FOR BULLETS AND PADDLE AUTOPILOT
	final int numRows = 10, numCols = 15; // total number of rows and columns
	final int rowInc = 15; // depth of row in pixels
	final int colInc = right / numCols; // width of column in pixels
	final int bricksYOffset = 10; // space at top of screen before bricks
	// Brick bricks[] [] = new Brick [numRows] [numCols]; // array to store
	// bricks
	BrickBoard bricks;
	// element meaning: 0 = no block
	// fullBlock = regular block
	// 1->fullBlock-1 = block is now disappearing (animation in progress)
	// fullBlock+1 = invincible block

	// *******************
	// The Planet
	// *******************

	// constants
	final int initialPlanetPosition = 35;
	final double planetSpeed = 0.2;
	final int finalPlanetPosition = 15;
	// variables
	boolean gravity = false;
	double planetPosition = initialPlanetPosition;

	// *******************
	// The Bullets
	// *******************
	// Bullet[] bullets = new Bullet [2];
	final double bulletSpeed = -0.6;
	double[][] bulletCoord = new double[2][2]; // two bullets, two coordinates
	// (x and y)
	boolean[] bulletAlive = new boolean[2]; // two bullets, true if bullet is in
	// the air

	// ***********
	// Power Pills
	// ***********

	// constants
	final int numPowerPills = 18;
	// final String[] powerPillTag = new String [numPowerPills];
	final int lastPowerPillInitialCountDown = 100;
	final int initialPillProbability = 10;
	final int pillProbabilityIncrement = 1;
	// variables
	LinkedList<PowerPill> powerPills = new LinkedList<PowerPill>(); // holds the
	// list of
	// levels
	// read in
	// from disk
	// PowerPill powerPill;
	int[] pillProbabilities = new int[numPowerPills];
	double powerPillFrequency; // probability of a power up (per level)
	double powerPillSpeed; // speed of power up fall (per level)
	// double powerPillXIncrement, powerPillYIncrement; // velocity vector for
	// power up
	// double powerPillX, powerPillY; // position of powerUp block
	// boolean powerPillFalling = false;
	// int powerPillType = 0;
	String lastPowerPillTag = "";
	int lastPowerPill = 0;
	int lastPowerPillCountDown = 0;
	boolean showLastPowerPill = false;

	// The Stars

	StarField stars;
	boolean waitForStarRush = false;
	PlanetDoor planetDoor;

	// ***********
	// Audio Clips
	// ***********

	AudioClip goodSound = null, badSound = null, paddleSound = null,
			hardSound = null;
	AudioClip goodSoundHigh = null, badSoundHigh = null,
			paddleSoundHigh = null, hardSoundHigh = null;
	AudioClip goodSoundLow = null, badSoundLow = null, paddleSoundLow = null,
			hardSoundLow = null;
	AudioClip bulletSound = null, bulletHitSound = null;
	AudioClip planetSound = null;
	AudioClip laserSound = null;
	AudioClip catcherSound = null;
	AudioClip pillCatchSound = null;
	AudioClip flatRoundSound = null;
	AudioClip paddleChangeSizeSound = null, paddleChangeSizeShortSound = null;
	AudioClip jumpPaddleSound = null, jumpSound = null;
	AudioClip ballBiggerSound = null, ballSmallerSound = null,
			superBallSound = null, superBallEndSound = null;
	AudioClip shipSound = null;
	AudioClip musicSound = null, musicEndSound = null;
	boolean musicOn = false;
	MusicGenerator musicGenerator = null;
	// goodSound = block disappear
	// badSound = hit wall
	// paddleSound = hit paddle
	// hardSound = lost ball or hit invincible block

	// *************************************************************
	// *************************************************************
	// INSTANCE METHODS
	// *************************************************************
	// *************************************************************

	// **********************
	// Getting Things Started
	// **********************

	// init
	GameApplet a;

	public SchmarkanoidCanvas(GameApplet ap) {
		a = ap;
		levelReady = false;

		// where are we?
		//codeBase = a.getCodeBase();
		//documentBase = a.getDocumentBase();

		// the bricks
		bricks = new BrickBoard(top + bricksYOffset, left, right);

		// the scoreboard
		score = new ScoreBoard(new Ball(), top, right + 1, bottom - top + 1,
				statusWidth, textColor, ballColor, brickColor,
				statusScreenColor);
		planetDoor = new PlanetDoor(bottom + rowInc + 1, appletHeight, 0,
				appletWidth, statusScreenColor);

		// the ball
		ball = new Ball(bricks, score);
		ball.setFrameRate(600);
		score.setBall(ball);

		// screen edges
		ball.addVerticalEdge(left, top, bottom);
		ball.addVerticalEdge(right, top, bottom);
		topEdge = new ScreenRectangle(top - 15 - 1, top, left, right
				+ statusWidth, borderColor);

		// the stars
		setBackground(backgroundColor);
		stars = new StarField(appletWidth / 2, appletHeight / 2, appletWidth,
				appletHeight, 100);

		// the audioClips
		new AudioClipLoader(this, a);

		// levels and score
		loadLevels();
		score.reset();
		newLevel();

		// listeners
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);

		// set important game flags
		score.gameOver();

		resetEverything();

		// Start threads
		ball.startThread();
		updater = new GameAppletUpdater(this);
		Timer t = new Timer();
		t.scheduleAtFixedRate(new SchmarkanoidTimerTask(), 0, pauseDuration);

		initDone = true;

	}

	public void audioClipLoaded() {
		audioClips++;
		topStatus = true;
	}

	// loadLevels
	void loadLevels() {
		int maxLevel = 0;
		// codeBase = getCodeBase ();

		Scanner levelsFileReader = null;
//		try {
			levelsFileReader = new Scanner(this.getClass().getResourceAsStream("data/Levels.txt"));
			//System.out.println(levelsFileReader.next());
					//new InputStreamReader(
					//(new URL(codeBase.toString() + "data/Levels.txt"))
					//		.openConnection().getInputStream()));
//		} catch (FileNotFoundException e) {
//			// System.out.println ("File Not Found");
//		} catch (MalformedURLException e) {
//			// System.out.println ("Bad URL");
//		} catch (IOException e) {
//			// System.out.println ("IOException");
//		}

		boolean noMoreLevels = false;
		SchmarkanoidLevel level = null;

		while (!noMoreLevels) {
			level = new SchmarkanoidLevel(levelsFileReader);
			if (level.isLevelOK()) {
				levels.addLast(level);
				maxLevel++;
			} else
				noMoreLevels = true;
		}
//		try {
			levelsFileReader.close();
//		} catch (IOException e) {
//		}
		score.setMaxLevel(maxLevel);

	}

	public void resetEverything() {
		for (int i = 0; i < numPowerPills; i++)
			pillProbabilities[i] = initialPillProbability;
		levelReady = false;
		// new scoreboard
		score.reset();
		gameOn = false;
		// paddleVV = 0;
		roundPaddle = true;
		for (int i = 0; i < 2; i++)
			bulletAlive[i] = false;
		if (initDone) {
			newLevel();
		}
		newBall();
		levelChangeOK = true;
	}

	// resetBall
	void resetBall() {
		score.noPillNextTime();

		// ball
		if (ball.superBall() && (!score.isGameMuted()) && (!score.isGameOver())) {
			stopSound(superBallSound);
			playSound(superBallEndSound);
		}
		ball.reset();
		synchronized (extraBalls) {
			Iterator<Ball> itr = extraBalls.iterator();
			while (itr.hasNext()) {
				Ball b = itr.next();
				b.stopThread();
				b.invisible();
			}
			extraBalls = new LinkedList<Ball>();
		}
		synchronized (powerPills) {
			Iterator<PowerPill> itr = powerPills.iterator();
			while (itr.hasNext()) {
				PowerPill p = itr.next();
				p.stopThread();
			}
			powerPills = new LinkedList<PowerPill>();
		}
		if (resume)
			ball.setSpeed(resumeSpeed);
		else
			ball.resetSpeed();

		// paddle
		paddlePowerDown();
		jumpPaddle = true;
		roundPaddle = true;

		// put ball on paddle
		ball.ballOnPaddleX = (int) (Math.random() * normalPaddleWidth / 8 + normalPaddleWidth / 8);
		if (!roundPaddle)
			ball.ballOnPaddleX += normalPaddleWidth / 8;
		if (Math.random() > 0.5)
			ball.ballOnPaddleX *= -1;
		ball.setX(paddleX + ball.ballOnPaddleX);
		ball.setY(paddleEdge(ball.getX(), ball.getRadius(), false));

		// game
		gamePowerDown();
		ball.topIncrement = false;

		// no bullets
		for (int i = 0; i < 2; i++)
			bulletAlive[i] = false;

		// scoreboard
		score.clearMultihit();

		// refresh screen
		wholeScreen = true;

	}

	// newBall
	void newBall() {
		ball.invisible();

		if (!score.isGameOver()) {
			score.removeBall();
			synchronized (extraBalls) {
				Iterator<Ball> itr = extraBalls.iterator();
				while (itr.hasNext()) {
					Ball b = itr.next();
					b.stopThread();
					b.invisible();
				}
				extraBalls = new LinkedList<Ball>();
			}
			synchronized (powerPills) {
				Iterator<PowerPill> itr = powerPills.iterator();
				while (itr.hasNext()) {
					PowerPill p = itr.next();
					p.stopThread();
				}
				powerPills = new LinkedList<PowerPill>();
			}

			if (score.isGameOver()) {
				if (!score.isGameMuted()) {
					try {
						stopSound(hardSound);
						playSound(hardSound);
						Thread.sleep(100);
						stopSound(hardSound);
						playSound(hardSound);
					} catch (InterruptedException e) {
					}
				}
				ball.mute();
				synchronized (extraBalls) {
					Iterator<Ball> itr = extraBalls.iterator();
					while (itr.hasNext())
						itr.next().mute();
				}
			}
		}
		if (gameOn) {
			resume = true;
			resumeSpeed = ball.initialSpeed()
					+ (ball.currentSpeed() - ball.initialSpeed())
					* resumeSpeedProportion;
			ball.topIncrement = false;
		} else
			resume = false;

		resetBall();
		ball.visible();
		gameOn = true;
	}

	// throwBall
	void throwBall() {
		if (!waitForStarRush) {
			if (!score.isGameOver() && !score.isGameMuted()) {
				if (musicOn)
					playSound(musicEndSound);
				if (musicGenerator != null)
					musicGenerator.startMusic();
			}
			stopSound(musicSound);
			musicOn = false;
			if (ball.isBallCaught() || !ball.isInPlay()) {
				ball.ballNotCaught();
				ball.inPlay();
				ball.serve(); // serve ball
				ball.ballLaunch = true;
				jumpForBallLaunchOnly++;
			}
			synchronized (extraBalls) {
				Iterator<Ball> itr = extraBalls.iterator();
				while (itr.hasNext()) {
					Ball b = itr.next();
					if (b.isBallCaught()) {
						b.ballNotCaught();
						b.inPlay();
						b.serve(); // serve ball
						b.ballLaunch = true;
						jumpForBallLaunchOnly++;
					}
				}
			}
			levelChangeOK = false;
			freeze = true; // stop the paddle for the serve
			paddleMoved = true;
			jump();
			jumpPaddle = false;
			wholeScreen = true;
		}
	}

	// prepares for a new level
	// - star rush, blank out screen, power down ball and paddle
	void prepareNewLevel(boolean animate) {
		if (!score.isGameOver() && !score.isGameMuted()) {
			loopSound(musicSound);
			musicOn = true;
			if (musicGenerator != null)
				musicGenerator.stopMusic();
		}

		// defaults
		levelReady = false;
		ball.topIncrement = false;

		// blank out level
		score.setLevelName(" ");

		bricks.blankOutBoard();

		if (animate) {
			stars.rush();
			planetDoor.close();
			waitForStarRush = true;
			if (!score.isGameOver() && !score.isGameMuted())
				playSound(shipSound);
		} else
			levelReady = true;
	}

	// newLevel
	void newLevel() {
		planetDoor.open();
		// defaults
		ball.topIncrement = false;

		resumeSpeedProportion = 0.75;
		topHitSpeedIncrement = 20;
		powerPillFrequency = 0.25;
		powerPillSpeed = 1;

		SchmarkanoidLevel newLevel = (SchmarkanoidLevel) (levels.get((score
				.getLevel() - 1)
				% score.getMaxLevel()));

		bricks.loadBoard(newLevel);

		ball.setMaxSpeed(newLevel.getMaxBallSpeed()
				* (int) ((score.getLevel() - 1) / score.getMaxLevel() + 1));
		ball.setInitialSpeed(newLevel.getInitialSpeed()
				* (int) ((score.getLevel() - 1) / score.getMaxLevel() + 1));
		ball.setSpeedIncrement(newLevel.getBallSpeedIncrement()
				* (int) ((score.getLevel() - 1) / score.getMaxLevel() + 1));
		score.setLevelName(newLevel.getLevelName());

		ball.resetSpeed();

		levelReady = true;
		wholeScreen = true;
	}

	// ********************
	// SHUTTING THINGS DOWN
	// ********************

	// stop
	public void stop() {
		stopSound(superBallSound);
		stopSound(musicSound);
		musicGenerator.muteMusic();
		ball.stopThread();
		ball = null;
		synchronized (extraBalls) {
			Iterator<Ball> itr = extraBalls.iterator();
			while (itr.hasNext()) {
				Ball b = itr.next();
				b.stopThread();
			}
			extraBalls = new LinkedList<Ball>();
		}
		bricks.destroyBoard();
		bricks = null;
		topEdge = null;
		stopFlag = true;
		t = null;
		updater.halt();
		updater = null;
	}

	// *********
	// UTILITIES
	// *********

	public void playSound(AudioClip a) {
		if (a != null)
			a.play();
	}

	public void loopSound(AudioClip a) {
		if (a != null)
			a.loop();
	}

	public void stopSound(AudioClip a) {
		if (a != null)
			a.stop();
	}

	// paddleEdge(double x, boolean reflection)
	// - computes the Y-coordinate for the ball center when the ball is on the
	// edge of the paddle
	// - if the paddle is round and x is not between the edges of the paddle,
	// then result is meaningless
	// - if reflection is true and the paddle is round, the ball.getRadius()
	// constant is not subtracted
	// - this is the case where ball collisions are being detected and it has to
	// look nice
	int paddleEdge(double x, int ballRadius, boolean reflection) {
		if (roundPaddleHeight > 0)
			if (reflection)
				return (int) paddleY
						- (roundPaddleHeight / 2)
						+ (int) (Math.pow((paddleX - x)
								/ ((paddleWidth + ballRadius * 2) / 2), 2.0) * (roundPaddleHeight / 2));
			else
				return (int) paddleY
						- (roundPaddleHeight / 2)
						+ (int) (Math.pow((paddleX - x)
								/ ((paddleWidth + ballRadius * 2) / 2), 2.0) * (roundPaddleHeight / 2))
						- ballRadius;
		else if (reflection)
			return (int) paddleY - paddleHeight / 6 - ballRadius / 4;
		else
			return (int) paddleY - paddleHeight / 6 - ballRadius;
	}

	// paddleReflect
	// - computes a new ball vector on paddle reflection
	// - for a flat paddle, uses position + momentup
	// - for a round paddle, uses realistic reflection for ball coming from
	// right, hittint left side of paddle or vice versa
	// - for ball hitting left from left or right from right uses mirror image
	// reflection
	void paddleReflect(Ball b) {
		if (!roundPaddle) {
			double xIncrementChange = (double) (b.getX() - paddleX)
					/ ((double) paddleWidth / 1.5); // change division to make
			// paddle more extreme
			if (xIncrementChange < 0)
				xIncrementChange = (-1) * xIncrementChange * xIncrementChange;
			else
				xIncrementChange = xIncrementChange * xIncrementChange;
			b.getVelocity().setX(b.getVelocity().getX() + xIncrementChange);
			if (b.getVelocity().getX() >= 1)
				b.getVelocity().setX(0.95);
			else if (b.getVelocity().getX() <= -1)
				b.getVelocity().setX(-0.95);
			b.getVelocity().setY(
					(-1)
							* Math.sqrt(1 - b.getVelocity().getX()
									* b.getVelocity().getX()));
			b.getVelocity().scale();
		} else // WORRY ABOUT ANGLE = PI/2...
		{
			double oldXIncrement = b.getVelocity().getX();
			double oldYIncrement = b.getVelocity().getY();

			// note that signs are reversed in some cases because the Y axis is
			// upside down

			// incoming angle relative to the origin (sign reversed)
			double incomingAngle;
			if (b.getVelocity().getX() == 0)
				incomingAngle = Math.PI / 2;
			else
				incomingAngle = Math.atan((-1) * b.getVelocity().getY()
						/ b.getVelocity().getX());
			if (incomingAngle < 0)
				incomingAngle += Math.PI;

			// x coordinate with paddleX at the origin
			double x = b.getX() - paddleX;

			// paddle tangent angle relative to the origin
			double paddleAngle;
			double paddleWidthSoftened = paddleWidth * paddleSoftener;

			if (b.getVelocity().getX() < 0) // assume x is positive (hitting
				// right half)
				paddleAngle = Math
						.atan((-1)
								* roundPaddleHeight
								* Math.abs(x)
								/ (paddleWidthSoftened * paddleWidthSoftened * Math
										.sqrt(0.25
												- x
												* x
												/ (paddleWidthSoftened * paddleWidthSoftened))));
			else
				// assume x is negative (hitting left half)
				paddleAngle = Math
						.atan(roundPaddleHeight
								* Math.abs(x)
								/ (paddleWidthSoftened * paddleWidthSoftened * Math
										.sqrt(0.25
												- x
												* x
												/ (paddleWidthSoftened * paddleWidthSoftened))));
			if (paddleAngle < 0) // no negative angles!
				paddleAngle += Math.PI;

			// angle of incidence (angle between paddle and b trajectory)
			double alpha;
			// angle of reflection (relative to the origin)
			double outgoingAngle;
			if ((b.getVelocity().getX() < 0)
					|| ((b.getVelocity().getX() == 0) && (b.getX() >= paddleX))) // moving
			// left
			{
				// assume we hit right half and compute reflection
				alpha = Math.PI - paddleAngle + incomingAngle;
				outgoingAngle = incomingAngle + Math.PI - 2 * alpha;
				b.getVelocity().setY((-1) * Math.abs(Math.tan(outgoingAngle))); // Y
				// always
				// negative
				// in
				// this
				// case
				if (outgoingAngle <= Math.PI / 2)
					b.getVelocity().setX(1);
				else
					b.getVelocity().setX(-1);
				if (x < 0) { // did we actually hit the left half?
					if (b.getVelocity().getX() > 0) // problem - going right
					// doesn't make sense if we
					// hit the left half going
					// left
					{
						b.getVelocity().setX(b.getVelocity().getX() * (-2)); // solution
						// -
						// reverse
						// and
						// amplify
						// the
						// reflection
					}
					b.getVelocity().scale();
					updateGravityConstant(b);
					if (Math.abs(b.getVelocity().getX()) < Math
							.abs(oldXIncrement)) { // ok, I give up. treat
						// paddle as if it was flat
						b.getVelocity().setX(oldXIncrement);
						b.getVelocity().setY((-1) * oldYIncrement);
					}
				}
				b.getVelocity().scale();
				updateGravityConstant(b);
			} else // moving right
			{
				// assume we hit left half and compute reflection
				alpha = Math.PI - incomingAngle + paddleAngle;
				outgoingAngle = incomingAngle - (Math.PI - 2 * alpha);
				b.getVelocity().setY((-1) * Math.abs(Math.tan(outgoingAngle))); // Y
				// always
				// negative
				// in
				// this
				// case
				if (outgoingAngle <= Math.PI / 2)
					b.getVelocity().setX(1);
				else
					b.getVelocity().setX(-1);
				if (x > 0) { // did we actually hit the right half?
					if (b.getVelocity().getX() < 0) // problem - going left
					// doesn't make sense if we
					// hit the right half going
					// right
					{
						b.getVelocity().setX(b.getVelocity().getX() * (-2)); // solution
						// -
						// reverse
						// and
						// amplify
						// the
						// reflection
					}
					b.getVelocity().scale();
					updateGravityConstant(b);
					if (Math.abs(b.getVelocity().getX()) < Math
							.abs(oldXIncrement)) { // ok, I give up. treat
						// paddle as if it was flat
						b.getVelocity().setX(oldXIncrement);
						b.getVelocity().setY((-1) * oldYIncrement);
					}
				}
				b.getVelocity().scale();
				updateGravityConstant(b);
			}
		}
		// add some paddle momentum if it has any (except on the launch)
		if (jumpForBallLaunchOnly == 0) {
			b.getVelocity().setY(b.getVelocity().getY() + paddleVV);
			b.setSpeed(b.currentSpeed() + (paddleVV / initialPaddleVV)
					* topHitSpeedIncrement * b.speedIncrement() * 2);
			if (b.currentSpeed() > b.absoluteMaxSpeed()) // we can exceed top
				// speed here...
				b.setSpeed(b.absoluteMaxSpeed());
			if (b.currentSpeed() < b.initialSpeed())
				b.setSpeed(b.initialSpeed());
			updateGravityConstant(b);
		} else
			jumpForBallLaunchOnly--;
	}

	void updateGravityConstant(Ball b) {
		double gravityConstant;
		if (planetPosition < initialPlanetPosition) {
			gravityConstant = (b.currentSpeed() * b.currentSpeed()
					/ (2 * (bottom - top)) / 1.2);
			if (planetPosition > finalPlanetPosition)
				gravityConstant /= Math.pow(planetPosition
						- finalPlanetPosition + 1, 2);
		} else
			gravityConstant = 0;
		b.setAcceleration(0, gravityConstant);
	}

	// powerPill
	// - choose a new powerup pill to drop
	void powerPill(Brick hitBrick) {
		if (score.pillThisTime()) {
			startPowerPill(hitBrick);
			score.noPillNextTime();
			score.requestRepaint();
		} else if (Math.random() < powerPillFrequency) {
			score.pillNextTime();
			score.requestRepaint();
		}

		// startPowerPill (hitBrick);

	}

	int getNewPowerPillType() {
		int powerPillType = 0;

		int sumProbabilities = 0;
		for (int i = 0; i < numPowerPills; i++)
			sumProbabilities += pillProbabilities[i];
		double random = Math.random() * sumProbabilities;

		for (int i = numPowerPills - 1; i >= 0; i--) {
			if (random <= sumProbabilities)
				powerPillType = i + 1;
			else
				break;
			sumProbabilities -= pillProbabilities[i];
		}

		pillProbabilities[powerPillType - 1] = 0;
		for (int i = 0; i < numPowerPills; i++)
			pillProbabilities[i]++;
		return powerPillType;
		// return 18;
	}

	// startPowerUp
	// - start dropping the powerup pill
	void startPowerPill(Brick hitBrick) {
		// choose a power pill
		PowerPill powerPill;
		int powerPillType = getNewPowerPillType();
		switch (powerPillType) {
		case 1:
			powerPill = new PowerPill01(this, powerPillType, hitBrick.getX()
					- colInc / 2, hitBrick.getY());
			break;
		case 2:
			powerPill = new PowerPill02(this, powerPillType, hitBrick.getX()
					- colInc / 2, hitBrick.getY());
			break;
		case 3:
			powerPill = new PowerPill03(this, powerPillType, hitBrick.getX()
					- colInc / 2, hitBrick.getY());
			break;
		case 4:
			powerPill = new PowerPill04(this, powerPillType, hitBrick.getX()
					- colInc / 2, hitBrick.getY());
			break;
		case 5:
			powerPill = new PowerPill05(this, powerPillType, hitBrick.getX()
					- colInc / 2, hitBrick.getY());
			break;
		case 6:
			powerPill = new PowerPill06(this, powerPillType, hitBrick.getX()
					- colInc / 2, hitBrick.getY());
			break;
		case 7:
			powerPill = new PowerPill07(this, powerPillType, hitBrick.getX()
					- colInc / 2, hitBrick.getY());
			break;
		case 8:
			powerPill = new PowerPill08(this, powerPillType, hitBrick.getX()
					- colInc / 2, hitBrick.getY());
			break;
		case 9:
			powerPill = new PowerPill09(this, powerPillType, hitBrick.getX()
					- colInc / 2, hitBrick.getY());
			break;
		case 10:
			powerPill = new PowerPill10(this, powerPillType, hitBrick.getX()
					- colInc / 2, hitBrick.getY());
			break;
		case 11:
			powerPill = new PowerPill11(this, powerPillType, hitBrick.getX()
					- colInc / 2, hitBrick.getY());
			break;
		case 12:
			powerPill = new PowerPill12(this, powerPillType, hitBrick.getX()
					- colInc / 2, hitBrick.getY());
			break;
		case 14:
			powerPill = new PowerPill14(this, powerPillType, hitBrick.getX()
					- colInc / 2, hitBrick.getY());
			break;
		case 15:
			powerPill = new PowerPill15(this, powerPillType, hitBrick.getX()
					- colInc / 2, hitBrick.getY());
			break;
		case 16:
			powerPill = new PowerPill16(this, powerPillType, hitBrick.getX()
					- colInc / 2, hitBrick.getY());
			break;
		case 17:
			powerPill = new PowerPill17(this, powerPillType, hitBrick.getX()
					- colInc / 2, hitBrick.getY());
			break;
		case 18:
			powerPill = new PowerPill18(this, powerPillType, hitBrick.getX()
					- colInc / 2, hitBrick.getY());
			break;
		default:
			powerPill = new PowerPill(this, powerPillType, hitBrick.getX()
					- colInc / 2, hitBrick.getY());
		}
		powerPill.startAnimating();
		synchronized (powerPills) {
			powerPills.add(powerPill);
		}
		powerPill.setVelocity(0, powerPillSpeed);
		powerPill.startThread();
		powerPill.startMoving();
	}

	// paddlePowerDown
	void paddlePowerDown() {
		// powerPillFalling = false;
		ballCatcher = false;
		laserPaddle = false;
		jumpPaddle = false;
		targetPaddleWidth = normalPaddleWidth;
		lastPowerPill = 0;
		showLastPowerPill = false;
		lastPowerPillCountDown = 0;
	}

	void gamePowerDown() {
		gravity = false;
		ball.setAcceleration(0, 0);
		synchronized (extraBalls) {
			Iterator<Ball> itr = extraBalls.iterator();
			while (itr.hasNext())
				(itr.next()).setAcceleration(0, 0);
		}
	}

	void ballPowerDown() {
		if (ball.superBall() && (!score.isGameMuted()) && (!score.isGameOver())) {
			stopSound(superBallSound);
			playSound(superBallEndSound);
		}
		ball.powerDown();
		synchronized (extraBalls) {
			Iterator<Ball> itr = extraBalls.iterator();
			while (itr.hasNext()) {
				Ball b = itr.next();
				b.powerDown();
			}
		}
	}

	// paddlePowerUp
	void paddlePowerUp(PowerPill p) {
		// powerPillFalling = false;
		lastPowerPillTag = p.getTag();
		// wholeScreen = true;
		int powerPillType = p.getType();
		// System.out.println(powerPillType);
		// while (powerPillType == numPowerPills)
		// powerPillType = getNewPowerPillType();
		switch (powerPillType) {
		case 1: // expand
			if (targetPaddleWidth != expandedPaddleWidth) {
				paddlePowerDown();
				if (isBallCaught())
					throwBall();
				targetPaddleWidth = expandedPaddleWidth;
				if ((!score.isGameOver()) && (!score.isGameMuted())) {
					stopSound(paddleChangeSizeSound);
					playSound(paddleChangeSizeSound);
				}
			}
			break;
		case 2: // shrink
			if (targetPaddleWidth != shrunkPaddleWidth) {
				paddlePowerDown();
				if (isBallCaught())
					throwBall();
				targetPaddleWidth = shrunkPaddleWidth;
				if ((!score.isGameOver()) && (!score.isGameMuted())) {
					if (paddleWidth > normalPaddleWidth) {
						stopSound(paddleChangeSizeSound);
						playSound(paddleChangeSizeSound);
					} else {
						stopSound(paddleChangeSizeShortSound);
						playSound(paddleChangeSizeShortSound);
					}
				}
			}
			break;
		case 3: // fast
			ballPowerDown();
			ball.incrementSpeed(topHitSpeedIncrement);
			updateGravityConstant(ball);
			synchronized (extraBalls) {
				Iterator<Ball> itr = extraBalls.iterator();
				while (itr.hasNext()) {
					Ball b = itr.next();
					b.incrementSpeed(topHitSpeedIncrement);
					updateGravityConstant(b);
				}
			}
			// }
			break;
		case 4: // slow
			ballPowerDown();
			ball.decrementSpeed(topHitSpeedIncrement);
			ball.topIncrement = false;
			updateGravityConstant(ball);
			synchronized (extraBalls) {
				Iterator<Ball> itr = extraBalls.iterator();
				while (itr.hasNext()) {
					Ball b = itr.next();
					b.decrementSpeed(topHitSpeedIncrement);
					b.topIncrement = false;
					updateGravityConstant(b);
				}
			}
			break;
		case 5: // superball
			if (!ball.superBall()) {
				ballPowerDown();
				ball.superBallOn();
				if ((!score.isGameOver()) && (!score.isGameMuted())) {
					stopSound(superBallSound);
					loopSound(superBallSound);
				}
				synchronized (extraBalls) {
					Iterator<Ball> itr = extraBalls.iterator();
					while (itr.hasNext()) {
						Ball b = itr.next();
						b.superBallOn();
					}
				}
			}
			break;
		case 6: // flat paddle
			if (roundPaddle) {
				roundPaddle = false;
				if ((!score.isGameOver()) && (!score.isGameMuted())) {
					stopSound(flatRoundSound);
					playSound(flatRoundSound);
				}
			}
			break;
		case 7: // round paddle
			if (!roundPaddle) {
				roundPaddle = true;
				if ((!score.isGameOver()) && (!score.isGameMuted())) {
					stopSound(flatRoundSound);
					playSound(flatRoundSound);
				}
			}
			break;
		case 8: // catch
			if (!ballCatcher) {
				paddlePowerDown();
				ballCatcher = true;
				if ((!score.isGameOver()) && (!score.isGameMuted())) {
					stopSound(catcherSound);
					playSound(catcherSound);
				}
			}
			break;
		case 9: // jump
			if ((!jumpPaddle) || (isBallCaught())) {
				paddlePowerDown();
				if (isBallCaught())
					throwBall();
				jumpPaddle = true;
				if ((!score.isGameOver()) && (!score.isGameMuted())) {
					stopSound(jumpPaddleSound);
					playSound(jumpPaddleSound);
				}
			}
			break;
		case 10: // laser
			if (!laserPaddle) {
				paddlePowerDown();
				laserPaddle = true;
				if ((!score.isGameOver()) && (!score.isGameMuted())) {
					stopSound(laserSound);
					playSound(laserSound);
				}
				if (isBallCaught())
					throwBall();
			}
			break;
		case 11: // gravity on
			if (!gravity) {
				gravity = true;
				if ((!score.isGameOver()) && (!score.isGameMuted())) {
					stopSound(planetSound);
					playSound(planetSound);
				}
			}
			break;
		case 12: // gravity off
			gravity = false;
			break;
		case 13: // change ball direction
			ball.randomDirection();
			synchronized (extraBalls) {
				Iterator<Ball> itr = extraBalls.iterator();
				while (itr.hasNext()) {
					Ball b = itr.next();
					b.randomDirection();
				}
			}
			if ((!score.isGameOver()) && (!score.isGameMuted())) {
				stopSound(badSoundHigh);
				stopSound(badSoundLow);
				playSound(badSoundHigh);
				playSound(badSoundLow);
			}
			break;
		case 14: // expand ball
			if (!ball.bigBall()) {
				ballPowerDown();
				ball.expand();
				synchronized (extraBalls) {
					Iterator<Ball> itr = extraBalls.iterator();
					while (itr.hasNext()) {
						Ball b = itr.next();
						b.expand();
					}
				}
				if ((!score.isGameOver()) && (!score.isGameMuted())) {
					stopSound(ballBiggerSound);
					playSound(ballBiggerSound);
				}
			}
			break;
		case 15: // shrink ball
			if (!ball.littleBall()) {
				ballPowerDown();
				ball.shrink();
				synchronized (extraBalls) {
					Iterator<Ball> itr = extraBalls.iterator();
					while (itr.hasNext()) {
						Ball b = itr.next();
						b.shrink();
					}
				}
				if ((!score.isGameOver()) && (!score.isGameMuted())) {
					stopSound(ballSmallerSound);
					playSound(ballSmallerSound);
				}
			}
			break;
		case 16: // reset
			roundPaddle = true;
			ball.topIncrement = false;
			ballPowerDown();
			paddlePowerDown();
			synchronized (extraBalls) {
				Iterator<Ball> itr = extraBalls.iterator();
				while (itr.hasNext()) {
					Ball b = itr.next();
					b.stopThread();
					b.invisible();
				}
				extraBalls = new LinkedList<Ball>();
			}
			gamePowerDown();
			ball.resetSpeed();
			if (isBallCaught())
				throwBall();
			break;
		case 17: // multi-ball
			Ball newBall;
			int angleInc = 10;
			for (int i = 0; i < 2; i++) {
				newBall = ball.makeCopy();
				newBall.setDefaultColor(extraBallColor);
				newBall.startThread();
				newBall.setDirection(ball.getDirection() + angleInc);
				newBall.startMoving();
				extraBalls.add(newBall);
				angleInc -= 20;
			}
			break;
		case 18: // double multi-ball
			angleInc = 30;
			for (int i = 0; i < 4; i++) {
				newBall = ball.makeCopy();
				newBall.setDefaultColor(extraBallColor);
				newBall.startThread();
				newBall.setDirection(ball.getDirection() + angleInc);
				newBall.startMoving();
				extraBalls.add(newBall);
				angleInc -= 20;
			}
		}
		lastPowerPill = p.getType();
		showLastPowerPill = true;
		lastPowerPillCountDown = lastPowerPillInitialCountDown;
	}

	// fire bullets if any are available
	public void fireBullets() {
		boolean shot = false;
		if (!bulletAlive[0]) // left bullet
		{
			bulletCoord[0][0] = paddleX - paddleWidth / 2 - (laserWidth - 1);
			if ((bulletCoord[0][0] >= left) && (bulletCoord[0][0] <= right)) {
				bulletCoord[0][1] = paddleY - 7;
				bulletAlive[0] = true;
				paddleMoved = true;
				shot = true;
			}
		}
		if (!bulletAlive[1]) // right bullet
		{
			bulletCoord[1][0] = paddleX + paddleWidth / 2 + (laserWidth - 2);
			if ((bulletCoord[1][0] >= left) && (bulletCoord[1][0] <= right)) {
				bulletCoord[1][1] = paddleY - 7;
				bulletAlive[1] = true;
				paddleMoved = true;
				shot = true;
			}
		}
		if ((shot) && (!score.isGameOver()) && (!score.isGameMuted())) {
			stopSound(bulletSound);
			playSound(bulletSound);
		}
	}

	void jump() {
		if (paddleY == initialPaddleY) {
			paddleVV = initialPaddleVV;
			if ((!score.isGameOver()) && (!score.isGameMuted())
					&& (jumpForBallLaunchOnly == 0)) {
				stopSound(jumpSound);
				playSound(jumpSound);
			}
		}
	}

	// ***************
	// THE GAME THREAD
	// ***************

	// run
	boolean hitSomething = false;
	boolean playGoodSound, playHardSound, playHitSound;
	int row, col;
	double autoPlayXIncrement = 0;
	int autoPlayWaitTimer = 0;

	public class SchmarkanoidTimerTask extends TimerTask {
		public void run() {
			if (!pause) {
				// if level is over, count down to 0 before stopping
				if (countDown > 0)
					if (--countDown == 0) {
						prepareNewLevel(true);
						resetBall();
						score.incrementLevel();
						if (!score.isGameOver() && !score.isGameMuted()) {
							stopSound(goodSound);
							stopSound(badSound);
							// stopSound(paddleSound.stop ();
							playSound(goodSound);
							playSound(badSound);
							// paddleSound.play ();
						}
					}

				// Check for End of Level
				// if (numBricksExploded > 0)
				if (levelReady) {
					if (bricks.getNumBricks() == 0) {
						countDown = countDownStart;
						levelReady = false;
					}
					// numBricksExploded = 0;
				}

				// Autoplay if there is no game currently on
				if (score.isGameOver()) {
					// choose new paddle hit position if something was hit
					if (hitSomething)
						autoPlayXIncrement = Math.random() * paddleWidth
								- paddleWidth / 2;

					// throw ball if necessary
					if (autoPlayWaitTimer > 0) {
						autoPlayWaitTimer -= pauseDuration;
						if (autoPlayWaitTimer <= 0)
							throwBall();
					} else {
						if ((!ball.isInPlay()) && (!ball.isBallCaught()))
							autoPlayWaitTimer = 1000;
						if (ball.isBallCaught())
							autoPlayWaitTimer = 100;
					}
					// fire bullets
					if (laserPaddle)
						if (Math.random() < 0.01)
							fireBullets();

					// jump
					if (jumpPaddle)
						if ((ball.getY() > paddleY - paddleHeight * 2)
								&& (ball.getVelocity().getY() > 0)
								&& (Math.random() < 0.01))
							jump();

					// move paddle
					if (!freeze) // don't move paddle if frozen
					{
						int oldPaddleX = paddleX;
						if ((ball.getVelocity().getY() > 0)
								&& (ball.getY() > (bottom - top) * 3 / 4)) {
							if (paddleX > (int) (ball.getX() + autoPlayXIncrement))
								paddleX--;
							else if (paddleX < (int) (ball.getX() + autoPlayXIncrement))
								paddleX++;
						} else if (powerPills.size() > 0) {
							synchronized (powerPills) {
								if (paddleX > (int) (powerPills.getFirst()
										.getX() + colInc / 2))
									paddleX--;
								else if (paddleX < (int) (powerPills.getFirst()
										.getX() + colInc / 2))
									paddleX++;
							}
						}
						if (paddleX > right - paddleWidth / 2)
							paddleX = right - paddleWidth / 2;
						else if (paddleX < left + paddleWidth / 2)
							paddleX = left + paddleWidth / 2;
						if (oldPaddleX != paddleX)
							paddleMoved = true;
					}
				}

				// if speed over maximum, slow down!
				if (ball.currentSpeed() > ball.maxSpeed()) {
					ball.setSpeed(ball.currentSpeed() - jumpKickSlowDown);
					if (ball.currentSpeed() < ball.maxSpeed())
						ball.setSpeed(ball.maxSpeed());
					score.smartRepaintSpeedBar();
				}

				// Power Pill collisions
				if (powerPills.size() > 0) {
					LinkedList<PowerPill> pillsToRemove = new LinkedList<PowerPill>();
					synchronized (powerPills) {
						Iterator<PowerPill> itr = powerPills.iterator();
						while (itr.hasNext()) {
							PowerPill p = itr.next();
							if (p.getY() >= bottom) {
								// powerPillFalling = false;
								pillsToRemove.add(p);
								wholeScreen = true;
								score.noPillNextTime();
								score.requestRepaint();
							}
							if ((p.getX() > (paddleX - paddleWidth / 2 - colInc + 1))
									&& (p.getX() < (paddleX + paddleWidth / 2)))
								if ((p.getY() > paddleY - paddleHeight / 2)
										&& (p.getY() < paddleY)) {
									if (!score.isGameOver()
											&& !score.isGameMuted()) {
										stopSound(pillCatchSound);
										playSound(pillCatchSound);
									}
									paddlePowerUp(p);
									pillsToRemove.add(p);
									paddleMoved = true;
									score.pillNextTime();
									// score.requestRepaint();
								}
						}
						itr = pillsToRemove.iterator();
						while (itr.hasNext()) {
							PowerPill p = (PowerPill) itr.next();
							p.stopThread();
							powerPills.remove(p);
						}
					}
				}
				// setup flags for sound playing
				playGoodSound = false;
				playHardSound = false;
				playHitSound = false;

				// move bullets if necessary

				for (int i = 0; i < 2; i++) {
					// move one step
					if (bulletAlive[i]) {
						bulletCoord[i][1] += bulletSpeed;

						// top of screen collision
						if (bulletCoord[i][1] < (top - 5)) {
							bulletAlive[i] = false;
							wholeScreen = true;
						}

						// brick collisions
						col = (int) bulletCoord[i][0] / colInc;
						if ((bulletCoord[i][0] > col * colInc)
								&& (bulletCoord[i][0] < (col + 1) * colInc - 1)) // check
							// for
							// between
							// bricks
							if ((col >= 0) && (col < numCols))
								for (row = numRows - 1; row >= 0; row--)
									if (((int) bulletCoord[i][1] <= top
											+ (row + 1) * rowInc
											+ bricksYOffset - 2)
											&& (bricks.getBrick(row, col)
													.type() != Brick.NOBRICK)) {
										bulletAlive[i] = false;
										wholeScreen = true;
										if ((bricks.getBrick(row, col).type() == Brick.NORMAL)
												|| (bricks.getBrick(row, col)
														.type() == Brick.BOTTOMONLY)) {
											gameOn = true;
											score.bulletHitBrick();
											powerPill(bricks.getBrick(row, col));
											bricks.explodeBrick(row, col);
											playHitSound = true;
										}
									}
					}
				}

				if ((playHitSound) && (!score.isGameOver())
						&& (!score.isGameMuted())) {
					stopSound(bulletHitSound);
					playSound(bulletHitSound);
				}

				// move paddle vertically if necessary
				if ((paddleVV != 0) || (paddleY != initialPaddleY)) {
					paddleY += paddleVV;
					paddleVV += paddleVA;
					if (paddleY >= initialPaddleY) {
						paddleVV = 0;
						paddleY = initialPaddleY;
					}
					paddleMoved = true;
				}

				// is it time for a new level?
				if ((waitForStarRush) && (!stars.moving())) {
					waitForStarRush = false;
					newLevel();
					levelReady = true;
				}

				// set ball on the paddle edge
				if (!ball.isInPlay())
					ball.setY(paddleEdge(ball.getX(), ball.getRadius(), false));

				hitSomething = false; // reset the hitSomething field

				// DEAL WITH ACCUMULATED BALL COLLISIONS

				collisions(ball);
				synchronized (extraBalls) {
					Iterator<Ball> itr = extraBalls.iterator();
					while (itr.hasNext())
						collisions(itr.next());
				}
				if (playGoodSound)
					if (ball.bigBall()) {
						stopSound(goodSoundLow);
						playSound(goodSoundLow);
					} else if (ball.littleBall()) {
						stopSound(goodSoundHigh);
						playSound(goodSoundHigh);
					} else {
						stopSound(goodSound);
						playSound(goodSound);
					}
				if (playHardSound)
					if (ball.bigBall()) {
						stopSound(hardSoundLow);
						playSound(hardSoundLow);
					} else if (ball.littleBall()) {
						stopSound(hardSoundHigh);
						playSound(hardSoundHigh);
					} else {
						stopSound(hardSound);
						playSound(hardSound);
					}

				// check for ball collisions, etc.
				if ((ball.isInPlay()) && (!ball.isBallCaught())) {
					// ball going up
					if (ball.getVelocity().getY() < 0) {
						// top of screen
						if (topEdge.detectCollision(ball, true).getType() != ScreenObject.NOCOLLISION) {
							hitSomething = true;
							if (!ball.topIncrement) {
								ball.topIncrement = true;
								ball.incrementSpeed(topHitSpeedIncrement);
								updateGravityConstant(ball);
								score.requestRepaint();
							}
							if (!score.isGameOver() && !score.isGameMuted()) {
								if (ball.bigBall()) {
									stopSound(badSoundLow);
									playSound(badSoundLow);
								} else if (ball.littleBall()) {
									stopSound(badSoundHigh);
									playSound(badSoundHigh);
								} else {
									stopSound(badSound);
									playSound(badSound);
								}
							}
						}
					}
					// ball going down
					else {
						// bottom of screen
						if (ball.getY() >= bottom /*- ball.getRadius () + 2*/) {
							hitSomething = true;
							newBall();
							if (musicGenerator != null)
								musicGenerator.stopMusic();
							if (!score.isGameOver() && !score.isGameMuted()) {
								stopSound(paddleSoundLow);
								stopSound(hardSoundLow);
								stopSound(badSoundLow);
								playSound(paddleSoundLow);
								playSound(hardSoundLow);
								playSound(badSoundLow);
								// goodSoundLow.play ();
							}
						}
						// paddle
						else if ((int) ball.getCollisionBottom() - 2 >= paddleEdge(
								ball.getX(), ball.getRadius(), true)) {
							if (((int) ball.getX() > (paddleX - (paddleWidth / 2)))
									&& ((int) ball.getX() < (paddleX + (paddleWidth / 2)))) {
								if (!score.isGameOver() && !score.isGameMuted()
										&& (musicGenerator != null))
									musicGenerator.startMusic(); // if clips
																	// are
								// loading, this
								// gets the
								// music going

								if ((ballCatcher) && (!ball.ballLaunch)) {
									ball.stopMoving();
									ball.ballCaught();
									jumpPaddle = true;
									ball.setY(paddleEdge(ball.getX(), ball
											.getRadius(), false));
									ball.ballOnPaddleX = ball.getX() - paddleX;
								} else {
									ball.ballLaunch = false;
									ball.setY(ball.getY()
											- ball.getVelocity().getY() * 2
											* 0.75);
									paddleReflect(ball);
								}
								hitSomething = true;
								score.clearMultihit();
								if (!score.isGameOver() && !score.isGameMuted()) {
									if (ball.bigBall()) {
										stopSound(paddleSoundLow);
										playSound(paddleSoundLow);
									} else if (ball.littleBall()) {
										stopSound(paddleSoundHigh);
										playSound(paddleSoundHigh);
									} else {
										stopSound(paddleSound);
										playSound(paddleSound);
									}
								}
								freeze = false; // if paddle frozen, unfreeze it
								// (serve must be over)
							}
						}
					}
				}

				// other balls
				// System.out.println(extraBalls.size());
				LinkedList<Ball> ballsToRemove = new LinkedList<Ball>();
				synchronized (extraBalls) {
					Iterator<Ball> itr = extraBalls.iterator();
					while (itr.hasNext()) {
						Ball b = itr.next();
						if ((b.isInPlay()) && (!b.isBallCaught())) {
							// ball going up
							if (b.getVelocity().getY() < 0) {
								// top of screen
								if (topEdge.detectCollision(b, true).getType() != ScreenObject.NOCOLLISION) {
									if (!b.topIncrement) {
										b.topIncrement = true;
										b.incrementSpeed(topHitSpeedIncrement);
										updateGravityConstant(b);
										score.requestRepaint();
									}
									if (!score.isGameOver()
											&& !score.isGameMuted()) {
										if (b.bigBall()) {
											stopSound(badSoundLow);
											playSound(badSoundLow);
										} else if (b.littleBall()) {
											stopSound(badSoundHigh);
											playSound(badSoundHigh);
										} else {
											stopSound(badSound);
											playSound(badSound);
										}
									}
								}
							}
							// ball going down
							else {
								// bottom of screen
								if (b.getY() >= bottom /*- ball.getRadius () + 2*/) {
									hitSomething = true;
									ballsToRemove.add(b);
									if (!score.isGameOver()
											&& !score.isGameMuted()) {
										stopSound(paddleSoundLow);
										stopSound(hardSoundLow);
										// stopSound(badSoundLow);
										playSound(paddleSoundLow);
										playSound(hardSoundLow);
										// playSound(badSoundLow);
									}
								}
								// paddle
								else if ((int) b.getCollisionBottom() - 2 >= paddleEdge(
										b.getX(), b.getRadius(), true)) {
									if (((int) b.getX() > (paddleX - (paddleWidth / 2)))
											&& ((int) b.getX() < (paddleX + (paddleWidth / 2)))) {
										if ((ballCatcher) && (!b.ballLaunch)) {
											b.stopMoving();
											b.ballCaught();
											jumpPaddle = true;
											b.setY(paddleEdge(b.getX(), b
													.getRadius(), false));
											b.ballOnPaddleX = b.getX()
													- paddleX;
										} else {
											b.ballLaunch = false;
											b.setY(b.getY()
													- b.getVelocity().getY()
													* 2 * 0.75);
											paddleReflect(b);
										}
										hitSomething = true;
										score.clearMultihit();
										if (!score.isGameOver()
												&& !score.isGameMuted()) {
											if (b.bigBall()) {
												stopSound(paddleSoundLow);
												playSound(paddleSoundLow);
											} else if (b.littleBall()) {
												stopSound(paddleSoundHigh);
												playSound(paddleSoundHigh);
											} else {
												stopSound(paddleSound);
												playSound(paddleSound);
											}
										}
									}
									freeze = false; // if paddle frozen,
													// unfreeze it
									// (serve must be over)
								}
							}
						}
					}
					itr = ballsToRemove.iterator();
					while (itr.hasNext()) {
						Ball b = (Ball) itr.next();
						b.stopThread();
						b.invisible();
						extraBalls.remove(b);
					}
				}
			}
		}
	}

	void collisions(Ball b) {
		ScreenCollision collision = b.getNextCollision();
		while (collision != null) {
			hitSomething = true;
			if ((((Brick) (collision.getHitObject())).type() == Brick.EXPLODING)
					|| (((Brick) (collision.getHitObject())).type() == Brick.NOBRICK)) {
				gameOn = true;
				b.getVelocity().scale();
				updateGravityConstant(b);
				// numBricksExploded++;
				powerPill((Brick) (collision.getHitObject()));
				if (!score.isGameOver() && !score.isGameMuted())
					playGoodSound = true;
			} else if (!score.isGameOver() && !score.isGameMuted())
				playHardSound = true;
			collision = b.getNextCollision();
		}
	}

	public boolean isBallCaught() {
		if (ball.isBallCaught())
			return true;
		synchronized (extraBalls) {
			Iterator<Ball> itr = extraBalls.iterator();
			while (itr.hasNext())
				if (itr.next().isBallCaught())
					return true;
		}
		return false;
	}

	// *****************************************
	// MOUSE AND MOUSE MOTION LISTENER INTERFACE
	// *****************************************

	public void mousePressed(MouseEvent m) {

		if (initDone && !pause) {
			if (((!score.isGameOver()) && (!ball.isInPlay())) || isBallCaught())
				throwBall();
			else if (score.isGameOver()) {
				if (!score.isGameMuted())
					loopSound(musicSound);
				musicOn = true;
				// musicGenerator.startMusic();
				resetEverything();
				score.gameStarts();
				if (!score.isGameMuted()) {
					ball.unMute();
					synchronized (extraBalls) {
						Iterator<Ball> itr = extraBalls.iterator();
						while (itr.hasNext())
							itr.next().unMute();
					}
				}
			} else if (laserPaddle)
				fireBullets();
			else if (jumpPaddle)
				jump();
		}
	}

	Cursor hiddenCursor = Toolkit.getDefaultToolkit().createCustomCursor(
			new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
			new Point(0, 0), "Invisible Cursor");

	public void mouseMoved(MouseEvent m) {
		if (m.getX() > 0 && m.getX() < right && m.getY() > top - 20
				&& m.getY() < bottom && !score.isGameOver() && !pause)
			setCursor(hiddenCursor);
		else
			setCursor(Cursor.getDefaultCursor());
		if (initDone && !pause) {
			if ((!score.isGameOver()) && (!freeze)) // don't allow movement if
			// game over or paddle
			// frozen
			{
				paddleMoved = true;
				paddleX = m.getX();
				if (paddleX > right - paddleWidth / 2)
					paddleX = right - paddleWidth / 2;
				else if (paddleX < left + paddleWidth / 2)
					paddleX = left + paddleWidth / 2;
				if ((!ball.isInPlay()) || (ball.isBallCaught())) {
					ball.setX(paddleX + ball.ballOnPaddleX);
					ball.setY(paddleEdge(ball.getX(), ball.getRadius(), false));
				}
				synchronized (extraBalls) {
					Iterator<Ball> itr = extraBalls.iterator();
					while (itr.hasNext()) {
						Ball b = itr.next();
						if (b.isBallCaught()) {
							b.setX(paddleX + b.ballOnPaddleX);
							b.setY(paddleEdge(b.getX(), b.getRadius(), false));
						}
					}
				}
			}
		}
	}

	public void mouseClicked(MouseEvent m) {
	}

	public void mouseReleased(MouseEvent m) {
	}

	public void mouseEntered(MouseEvent m) {
	}

	public void mouseExited(MouseEvent m) {
	}

	public void mouseDragged(MouseEvent m) {
		mouseMoved(m);
	}

	// **********************
	// KEY LISTENER INTERFACE
	// **********************

	public void keyPressed(KeyEvent k) {
		if (initDone) {
			if ((levelChangeOK) && (!waitForStarRush)) {
				if ((k.getKeyCode() == KeyEvent.VK_DOWN)) {
					score.decrementLevel();
					newLevel();
					wholeScreen = true;
				}
				if (k.getKeyCode() == KeyEvent.VK_UP) {
					score.chooseNextLevel();
					newLevel();
					wholeScreen = true;
				}
			}

			if (k.getKeyChar() == 'p' && ball.isInPlay() && !score.isGameOver())
				if (!pause) {
					ball.pause();
					pause = true;
					updater.pause();
					musicGenerator.pause();
					stopSound(superBallSound);
					synchronized (extraBalls) {
						Iterator<Ball> itr = extraBalls.iterator();
						while (itr.hasNext())
							itr.next().pause();
					}
					synchronized (powerPills) {
						Iterator<PowerPill> itr = powerPills.iterator();
						while (itr.hasNext()) {
							itr.next().pause();
						}
					}
				} else {
					pause = false;
					updater.resume();
					musicGenerator.resume();
					ball.resume();
					if (ball.superBall() && !score.isGameMuted())
						loopSound(superBallSound);
					synchronized (extraBalls) {
						Iterator<Ball> itr = extraBalls.iterator();
						while (itr.hasNext())
							itr.next().resume();
					}
					synchronized (powerPills) {
						Iterator<PowerPill> itr = powerPills.iterator();
						while (itr.hasNext()) {
							itr.next().resume();
						}
					}
				}
			else if (k.getKeyChar() == 'm' && !pause) {
				if (score.isGameMuted()) {
					score.unMute();
					if (!score.isGameOver()) {
						if (musicGenerator != null && !musicOn)
							musicGenerator.startMusic();
						ball.unMute();
						synchronized (extraBalls) {
							Iterator<Ball> itr = extraBalls.iterator();
							while (itr.hasNext())
								itr.next().unMute();
						}
						if (musicOn)
							loopSound(musicSound);
						if (ball.superBall())
							loopSound(superBallSound);
					}
				} else {
					score.mute();
					ball.mute();
					synchronized (extraBalls) {
						Iterator<Ball> itr = extraBalls.iterator();
						while (itr.hasNext())
							itr.next().mute();
					}
					stopSound(superBallSound);
					if (musicGenerator != null)
						musicGenerator.muteMusic();
					if (musicOn)
						stopSound(musicSound);
				}
			} else if (k.getKeyChar() == 'q' && !pause) {
				if (!score.isGameOver()) {
					score.gameOver();
					ball.mute();
					synchronized (extraBalls) {
						Iterator<Ball> itr = extraBalls.iterator();
						while (itr.hasNext())
							itr.next().mute();
					}
					stopSound(superBallSound);
					if (musicGenerator != null)
						musicGenerator.stopMusic();
				}
				wholeScreen = true;
			} else if (k.getKeyChar() == 'k' && !pause) {
				if (!score.isGameOver()) {
					newBall();
					if (musicGenerator != null)
						musicGenerator.stopMusic();
					stopSound(superBallSound);
					if (!score.isGameOver() && !score.isGameMuted()) {
						stopSound(paddleSoundLow);
						stopSound(hardSoundLow);
						stopSound(badSoundLow);
						playSound(paddleSoundLow);
						playSound(hardSoundLow);
						playSound(badSoundLow);
					}
				}
			} else if ((!score.isGameOver()) && (!ball.isInPlay())) {
				if ((k.getKeyCode() == KeyEvent.VK_LEFT)
						&& (ball.ballOnPaddleX > (-1) * paddleWidth / 2 + 3)) {
					ball.ballOnPaddleX -= 1;
					ball.setX(paddleX + ball.ballOnPaddleX);
					ball.setY(paddleEdge(ball.getX(), ball.getRadius(), false));
					paddleMoved = true;
				} else if ((k.getKeyCode() == KeyEvent.VK_RIGHT)
						&& (ball.ballOnPaddleX < paddleWidth / 2 - 3)) {
					ball.ballOnPaddleX += 1;
					ball.setX(paddleX + ball.ballOnPaddleX);
					ball.setY(paddleEdge(ball.getX(), ball.getRadius(), false));
					paddleMoved = true;
				}
			}
		}
	}

	public void keyReleased(KeyEvent k) {
	}

	public void keyTyped(KeyEvent k) {
	}

	// *************
	// SCREEN STUFF
	// ************

	int savedPaddleX = paddleX, savedPaddleY = (int) paddleY; // globals, but

	// only used by
	// smartRepaint()
	// smartRepaint
	// - called by the updater, reads the flags saying what needs to be
	// updated
	// and calls repaint for those parts of the screen

	public void smartRepaint() {
		// first do a little animation control...
		if (lastPowerPillCountDown > 0) {
			if (--lastPowerPillCountDown == 0) {
				showLastPowerPill = false;
				paddleMoved = true;
			} else if (lastPowerPillCountDown % 25 == 0)
				if (showLastPowerPill) {
					showLastPowerPill = false;
					paddleMoved = true;
				} else {
					showLastPowerPill = true;
					paddleMoved = true;
				}
		}

		if (paddleWidth < targetPaddleWidth) // paddle morphing - expand
		{
			paddleWidth++;
			if (paddleX + paddleWidth / 2 > right)
				paddleX--;
			else if (paddleX - paddleWidth / 2 < left)
				paddleX++;
			paddleMoved = true;
		}

		else if (paddleWidth > targetPaddleWidth) // paddle morphing - shrink
		{
			paddleWidth--;
			paddleMoved = true;
			if (!ball.isInPlay())
				ball.setY(paddleEdge(ball.getX(), ball.getRadius(), false));
			if (!ball.isInPlay())
				ball.setY(paddleEdge(ball.getX(), ball.getRadius(), false));
		}

		if ((roundPaddle) && (roundPaddleHeight < paddleHeight)) // paddle
		// morphing
		// - flat to
		// round
		{
			roundPaddleHeight++;
			if (ball.isBallCaught())
				ball.setY(paddleEdge(ball.getX(), ball.getRadius(), false));

			paddleMoved = true;
			if (!ball.isInPlay())
				ball.setY(paddleEdge(ball.getX(), ball.getRadius(), false));
		}

		else if ((!roundPaddle) && (roundPaddleHeight > 0)) // paddle morphing -
		// round to flat
		{
			roundPaddleHeight--;
			if (ball.isBallCaught())
				ball.setY(paddleEdge(ball.getX(), ball.getRadius(), false));
			synchronized (extraBalls) {
				Iterator<Ball> itr = extraBalls.iterator();
				while (itr.hasNext()) {
					Ball b = itr.next();
					if (b.isBallCaught())
						b.setY(paddleEdge(b.getX(), b.getRadius(), false));
				}
			}
			paddleMoved = true;
			if (!ball.isInPlay())
				ball.setY(paddleEdge(ball.getX(), ball.getRadius(), false));
		}

		// animate laser in and out
		if (laserPaddle) {
			if (laserWidth < fullLaserWidth) {
				int oldLaserWidth = (int) laserWidth;
				laserWidth += laserOutSpeed;
				if (oldLaserWidth != (int) laserWidth)
					paddleMoved = true;
			}
		}

		else if (laserWidth > initialLaserWidth) {
			int oldLaserWidth = (int) laserWidth;
			laserWidth -= laserOutSpeed;
			if (oldLaserWidth != (int) laserWidth)
				paddleMoved = true;
		}

		// animate planet in and out
		if (gravity) {
			if (planetPosition > finalPlanetPosition) {
				int oldPlanetPosition = (int) planetPosition;
				planetPosition -= planetSpeed;
				if (oldPlanetPosition != (int) planetPosition)
					planetMoved = true;
				updateGravityConstant(ball);
			}
		}

		else if (planetPosition < initialPlanetPosition) {
			int oldPlanetPosition = (int) planetPosition;
			planetPosition += planetSpeed;
			if (oldPlanetPosition != (int) planetPosition)
				planetMoved = true;
			updateGravityConstant(ball);
		}

		if (ballCatcher) {
			if (catcherLength < finalCatcherLength) {
				int oldCatcherLength = (int) catcherLength;
				catcherLength += catcherAnimationSpeed;
				if (oldCatcherLength != (int) catcherLength)
					paddleMoved = true;
			}
		}

		else if (catcherLength > initialCatcherLength) {
			int oldCatcherLength = (int) catcherLength;
			catcherLength -= catcherAnimationSpeed;
			if (oldCatcherLength != (int) catcherLength)
				paddleMoved = true;
		}

		// animate jumper in and out
		if (jumpPaddle) {
			if (jumpPaddleHeight < finalJumpPaddleHeight) {
				int oldJumpPaddleHeight = (int) jumpPaddleHeight;
				jumpPaddleHeight += jumpPaddleAnimationSpeed;
				if (oldJumpPaddleHeight != (int) jumpPaddleHeight)
					paddleMoved = true;
			}
		}

		else if (jumpPaddleHeight > initialJumpPaddleHeight) {
			int oldJumpPaddleHeight = (int) jumpPaddleHeight;
			jumpPaddleHeight -= jumpPaddleAnimationSpeed;
			if (oldJumpPaddleHeight != (int) jumpPaddleHeight)
				paddleMoved = true;
		}

		// then repainting
		repaint();
		/*
		 * if (wholeScreen) // whole screen needs repainting? { wholeScreen =
		 * false; // no further repainting necessary any more paddleMoved =
		 * false; repaint (); } else // piecemeal repainting {
		 * stars.smartRepaint (this); ball.smartRepaint (this);
		 * bricks.smartRepaint (this); score.smartRepaint (this);
		 * synchronized(extraBalls) { Iterator itr = extraBalls.iterator ();
		 * while (itr.hasNext ()) ((Ball) (itr.next ())).smartRepaint(this); } //
		 * top status repainting if (topStatus) { topStatus = false;
		 * repaint(left, 0, (right-left), top); } // bullet repainting for (int
		 * i = 0 ; i < 2 ; i++) if (bulletAlive [i]) repaint ((int) bulletCoord
		 * [i] [0], (int) bulletCoord [i] [1], 1, (int) (bulletCoord [i] [1] + 5 -
		 * bulletSpeed)); // paddle repainting if (paddleMoved) { paddleMoved =
		 * false; repaint (savedPaddleX - paddleWidth * 2, savedPaddleY -
		 * paddleHeight / 2 - ball.getRadius () * 2, paddleWidth * 4, bottom -
		 * (savedPaddleY - paddleHeight / 2 - ball.getRadius () * 2));
		 * savedPaddleX = paddleX; savedPaddleY = (int) paddleY; repaint
		 * (savedPaddleX - paddleWidth * 2, savedPaddleY - paddleHeight / 2 -
		 * ball.getRadius () * 2, paddleWidth * 4, bottom - (savedPaddleY -
		 * paddleHeight / 2 - ball.getRadius () * 2)); } // power pill repaint
		 * if (powerPills.size()>0) synchronized(powerPills) { Iterator itr =
		 * powerPills.iterator(); while (itr.hasNext())
		 * ((PowerPill)itr.next()).smartRepaint(this); } if (planetMoved) {
		 * planetMoved = false; repaint (left, bottom + rowInc, (right - left) +
		 * statusWidth, 50); } }
		 */
	}

	// paint
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// stars
		stars.paint(g);

		// status screen
		score.paint(g);

		// banner
		g.setColor(score.backgroundColor);
		g.fillRect(0, 0, appletWidth, top);
		g.setColor(textColor);
		g.setFont(new Font("Monospaced", Font.BOLD, 32));
		g.drawString("Schmarkanoid", 2, 31);
		g.setFont(new Font("Monospaced", Font.PLAIN, 12));
		g.drawString("by sam.scott@sheridancollege.ca", 240, 31);

		g.setFont(new Font("SansSerif", Font.PLAIN, 12));

		// bottom door
		planetDoor.paint(g);

		// power pill
		if (powerPills.size() > 0)
			synchronized (powerPills) {
				Iterator<PowerPill> itr = powerPills.iterator();
				while (itr.hasNext())
					itr.next().paint(g);
			}

		// balls
		ball.paint(g);
		synchronized (extraBalls) {
			Iterator<Ball> itr = extraBalls.iterator();
			while (itr.hasNext())
				itr.next().paint(g);
		}
		// bricks
		bricks.paint(g);

		// paddle
		g.setColor(paddleColor);
		if (roundPaddleHeight > 0)
			g.fillArc(paddleX - paddleWidth / 2, (int) paddleY
					- roundPaddleHeight / 2, paddleWidth, roundPaddleHeight, 0,
					180);
		g.fillRoundRect(paddleX - paddleWidth / 2, (int) paddleY - paddleHeight
				/ 6, paddleWidth, paddleHeight / 2, paddleHeight / 4,
				paddleHeight / 2);

		// jump paddle base
		if (jumpPaddleHeight > initialJumpPaddleHeight) {
			g.fillRect(paddleX - paddleWidth / 4, initialPaddleY
					+ (int) jumpPaddleHeight / 2, paddleWidth / 2, 3);
			g.fillRect(paddleX - 2, (int) paddleY, 4, initialPaddleY
					+ (int) jumpPaddleHeight / 2 - (int) paddleY);
		}

		// catcher paddle spikes
		if (ballCatcher)
			for (double i = paddleX - paddleWidth / 2 + 1; i < paddleX
					+ paddleWidth / 2 - 1; i += paddleWidth / 15.0)
				g.drawLine((int) i, paddleEdge(i, ball.getNormalRadius(), true)
						- (int) catcherLength, (int) i, paddleEdge(i, ball
						.getNormalRadius(), true) + 2);

		// lasers
		if (laserWidth > initialLaserWidth) {
			g.fillRect(paddleX - paddleWidth / 2 - ((int) laserWidth + 1),
					(int) paddleY, paddleWidth + ((int) laserWidth + 1) * 2,
					paddleHeight / 6);
			g.fillRect(paddleX - paddleWidth / 2 - (int) laserWidth,
					(int) paddleY - 2, (int) laserWidth, 2);
			g.fillRect(paddleX + paddleWidth / 2 + ((int) laserWidth - 3),
					(int) paddleY - 2, (int) laserWidth, 2);
			if (!bulletAlive[0])
				g.fillRect(paddleX - paddleWidth / 2 - ((int) laserWidth - 1),
						(int) paddleY - 7, (int) laserWidth - 2, 5);
			if (!bulletAlive[1])
				g.fillRect(paddleX + paddleWidth / 2 + ((int) laserWidth - 2),
						(int) paddleY - 7, (int) laserWidth - 2, 5);
		}

		// bullets
		for (int i = 0; i < 2; i++)
			if (bulletAlive[i])
				g.fillRect((int) bulletCoord[i][0], (int) bulletCoord[i][1], 1,
						5);

		// power pill caught
		g.setColor(brickColor);
		if ((lastPowerPill > 0) && (showLastPowerPill))
			g.drawString(lastPowerPillTag, (int) right + 10, (int) 235);

		// status screen separator bar
		g.setColor(borderColor);
		g.drawLine(right, top, right, bottom);
		g.drawLine(right + 1, top, right + 1, bottom);

		// top and bottom bars
		topEdge.paint(g);
		g.fillRect(left, bottom + 1, right + statusWidth, rowInc);

		// planet
		g.setColor(planetColor);
		g.fillArc(left - (right - left + statusWidth) * 2, bottom + rowInc
				+ (int) planetPosition, (right - left + statusWidth) * 5,
				(right - left + statusWidth) * 5, 0, 180);

		// planet door
		planetDoor.paint(g);

		// announcements
		g.setColor(textColor);
		if (audioClips < totalAudioClips) {
			g.setColor(Color.white);
			g.drawRect(left + 450, top - 33, 100, 15);
			g.setColor(Color.red);
			g.fillRect(left + 451, top - 32,
					(int) (99.0 / totalAudioClips * audioClips), 14);
			g.setColor(textColor);
			g.drawString("Loading Audio:", left + 450, top - 36);
		}
		if (score.isGameOver()) {
			g.drawString("Game Over", (right + left) / 2 - 30,
					(top + bottom) / 2 + 50);
			g.drawString("Click for New Game", (right + left) / 2 - 50,
					(top + bottom) / 2 + 80);
		} else if (!waitForStarRush) {
			if (!ball.isInPlay()) {
				g.drawString("Click to Throw!", (right + left) / 2 - 40,
						(top + bottom) / 2 + 50);
				g.drawString("Left or Right Arrow to Aim Ball",
						(right + left) / 2 - 79, (top + bottom) / 2 + 70);
				if (levelChangeOK)
					g.drawString("Up or Down Arrow to Choose Level",
							(right + left) / 2 - 95, (top + bottom) / 2 + 90);
			}
		}
		if (pause) {
			g.setColor(Color.white);
			g.drawString("PAUSED", (right + left) / 2 - 30,
					(top + bottom) / 2 + 50);
			// g.setColor(textColor);
			// String tempout = "";
			// for (int i=0;i<numPowerPills;i++)
			// g.drawString(pillProbabilities[i]+" ",20*i,bottom+30);
		}

	}
}
