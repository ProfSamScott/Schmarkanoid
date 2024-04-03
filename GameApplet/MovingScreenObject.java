package GameApplet;

import java.applet.AudioClip;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public abstract class MovingScreenObject extends ScreenObject {
	protected boolean pause = false;

	// velocity and acceleration
	protected ScreenVelocityVector v;
	protected ScreenAccelerationVector a;
	protected final double yMotionCompression = 0.75; // for non-square pixels

	// movement control
	protected Timer movementThread = null; // movement thread
	protected int updateDelay = 10; // movement thread pause length
	protected boolean objectMoving = false; // true if object set to move
	protected boolean threadRunning = false;

	// edges (can define any number of reflection edges)
	protected LinkedList<ScreenRectangle> edges = new LinkedList<ScreenRectangle>();
	protected AudioClip edgeSound;
	protected boolean edgeSoundSet = false;
	protected boolean mute = true;

	// collison buffer
	private LinkedList<ScreenCollision> collisionBuffer = new LinkedList<ScreenCollision>();

	// private boolean collisionBufferLocked = false;

	// constructors
	public MovingScreenObject() {
		super();
		v = new ScreenVelocityVector();
		a = new ScreenAccelerationVector();
	}

	// accessors
	public void setVelocity(double x, double y) {
		v.setX(x);
		v.setY(y);
	}

	public void setVelocity(ScreenVelocityVector newV) {
		v.setX(newV.getX());
		v.setY(newV.getY());
	}

	public void setSpeed(double newSpeed) {
		v.setMagnitude(newSpeed);
	}

	public double getSpeed() {
		return v.getMagnitude();
	}

	public double currentSpeed() {
		return v.getMagnitude();
	}

	public void setAcceleration(double x, double y) {
		a.setX(x);
		a.setY(y);
	}

	public void setAcceleration(ScreenAccelerationVector newA) {
		a.setX(newA.getX());
		a.setY(newA.getY());
	}

	public ScreenVelocityVector getVelocity() {
		return v;
	}

	public ScreenAccelerationVector getAcceleration() {
		return a;
	}

	public void setFrameRate(double framesPerSecond) {
		updateDelay = (int) (1000 / framesPerSecond);
	}

	public double getFrameRate() {
		return 1000.0 / updateDelay;
	}

	// edges
	public void addVerticalEdge(int edgeX, int top, int bottom) // edgeDirection...
	// 1=right,
	// -1=left,
	// 0=both
	{
		edges.add(new ScreenRectangle((double) top, (double) bottom,
				(double) edgeX, (double) edgeX));
	}

	public void addHorizontalEdge(int edgeY, int left, int right) // edgeDirection...
	// 1=down,
	// -1=up,
	// 0=both
	{
		edges.add(new ScreenRectangle((double) edgeY, (double) edgeY,
				(double) left, (double) right));
	}

	protected void edgeCollisions() // called from the movement thread
	{
		Iterator<ScreenRectangle> itr = edges.iterator();
		while (itr.hasNext()) {
			ScreenRectangle sr = itr.next();
			if ((sr.detectCollision(this, true).getType() != ScreenObject.NOCOLLISION)
					&& (edgeSoundSet) && (!mute)) {
				edgeSound.stop();
				edgeSound.play();
				// System.out.println("boom "+edgeSoundSet);
			}
			// else
			// System.out.println("boom? "+edgeSoundSet+" "+mute);
		}
	}

	// edge reflection sounds
	public void setEdgeSound(AudioClip es) {
		edgeSound = es;
		edgeSoundSet = true;
	}

	public AudioClip getEdgeSound() {
		return edgeSound;
	}

	public void mute() {
		mute = true;
	}

	public void unMute() {
		mute = false;
	}

	// threads & movement control
	public void startThread() {
		movementThread = new Timer();
		movementThread.scheduleAtFixedRate(new MovingScreenObjectTask(), 0,
				updateDelay);
		threadRunning = true;
	}

	public void stopThread() {
		threadRunning = false;
		movementThread.cancel();
	}

	public boolean threadRunning() {
		return threadRunning;
	}

	public void startMoving() {
		objectMoving = true;
	}

	public void stopMoving() {
		objectMoving = false;
	}

	public boolean moving() {
		return objectMoving;
	}

	public void pause() {
		pause = true;
	}

	public void resume() {
		pause = false;
	}

	// the thread itself
	public class MovingScreenObjectTask extends TimerTask {
		public void run() {
			if (!pause & objectMoving) {
				moveOneStep();
				accelerate();
				edgeCollisions();
				collisionCheck();
			}
		}
	}

	// collision buffer
	public ScreenCollision getNextCollision() {
		ScreenCollision returnValue = null;
		synchronized (collisionBuffer) {
			if (!collisionBuffer.isEmpty()) {
				returnValue = (ScreenCollision) (collisionBuffer.getFirst());
				collisionBuffer.removeFirst();
			}
		}
		return returnValue;
	}

	public void addCollision(ScreenCollision newCollision) {
		synchronized (collisionBuffer) {
			collisionBuffer.addLast(newCollision);
		}
	}

	// default movement and acceleration methods
	// overload if necessary
	public void moveOneStep() {
		setX(getX() + v.getX());
		setY(getY() + v.getY() * yMotionCompression);
	}

	public void accelerate() {
		v.setX(v.getX() + a.getX());
		v.setY(v.getY() + a.getY());
	}

	public void collisionCheck() {
	}

	// movement accessors
	public boolean movingLeft() {
		return v.getX() < 0;
	}

	public boolean movingRight() {
		return v.getX() > 0;
	}

	public boolean movingUp() {
		return v.getY() < 0;
	}

	public boolean movingDown() {
		return v.getY() > 0;
	}

	public void reflectX() {
		v.reflectX();
	}

	public void reflectY() {
		v.reflectY();
	}

	public void setDirection(double newDirection) {
		v.setDirection(newDirection);
	}

	public double getDirection() {
		return v.getDirection();
	}
}
