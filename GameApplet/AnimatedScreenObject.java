//AnimatedScreenObject
//- abstract class for moving screen objects.
//Created: Sam Scott, April 17, 2006
//Modified:

//SamScott@Canada.com
package GameApplet;

import java.util.Timer;

import GameApplet.MovingScreenObject.MovingScreenObjectTask;

public abstract class AnimatedScreenObject extends MovingScreenObject {
	// local vars
	protected boolean objectAnimated = false;

	// animation
	public void startAnimating() {
		objectAnimated = true;
	}

	public void stopAnimating() {
		objectAnimated = false;
	}

	public boolean animating() {
		return objectAnimated;
	}

	public void startThread() {
		movementThread = new Timer();
		movementThread.scheduleAtFixedRate(new AnimatedScreenObjectTask(), 0,updateDelay);
		threadRunning = true;
	}

	public class AnimatedScreenObjectTask extends MovingScreenObjectTask {
		public void run() {
			super.run();
			if (!pause & objectAnimated)
				animateOneStep();
		}
	}

	public abstract void animateOneStep();
}
