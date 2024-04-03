//Class: GameAppletUpdater
//Author: Sam Scott
//Date: April 19, 2006
//- starts a thread to repeatedly call smartRepaint() in a given applet

package GameApplet;

public class GameAppletUpdater implements Runnable
{
	int updateInterval = 30; // how often to update
	boolean update;          //
	GameCanvas updateTarget;
	Thread t = null;
	boolean pause = false;

	public GameAppletUpdater (GameCanvas game)
	{
		updateTarget = game;
		update = true;

		t = new Thread (this);
		t.start ();
	}

	public void pause()
	{
		pause = true;
		updateTarget.smartRepaint ();
		updateTarget.repaint(); // ugh!
		updateTarget.requestFocusInWindow();
	}
	public void resume()
	{
		pause = false;
	}
	public void run ()
	{
		update = true;
		while (update)
		{
			if (!pause)
			{
				try
				{
					updateTarget.smartRepaint ();
					updateTarget.repaint(); // ugh!
					updateTarget.requestFocusInWindow();
					Thread.sleep (updateInterval);
				}
				catch (InterruptedException e)
				{
				}
			}
		}
	}

	public void halt ()
	{
		update = false;
		t = null;
	}
}
