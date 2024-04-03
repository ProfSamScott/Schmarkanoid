import java.applet.Applet;
import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;

import GameApplet.GameApplet;


public class MusicGenerator implements Runnable
{
	private static boolean disabled = false;
	AudioClip[] sounds;
	int numSounds;
	double probability;
	Thread t = null;
	boolean stopFlag = true;
	final int pauseDuration = 70;
	GameApplet app = null;
	SchmarkanoidCanvas s;
	String filebase;
	String[] filenames;
	boolean pause = false;

	MusicGenerator (String fb, String[] fn, double frequency, GameApplet a, SchmarkanoidCanvas sc)
	{
		s=sc;
		filebase = fb;
		filenames = fn;
		numSounds = fn.length;
		sounds = new AudioClip[numSounds];
		for (int i=0;i<numSounds;i++)
			sounds[i]=null;

		app = a;

		probability = frequency/(1000.0/(double)pauseDuration);
	}

	public boolean isDisabled()
	{
		return disabled;
	}
	public void pause()
	{
		pause = true;
	}
	public void resume()
	{
		pause = false;
	}
	public void loadClips()
	{
		if (!disabled)
		{
//			try
//			{
				for (int i=0;i<numSounds;i++)
				{
					sounds [i] = Applet.newAudioClip(AudioClipLoader.class.getResource(filebase + filenames[i]));
					//sounds [i] = app.getAudioClip (new URL (filebase + filenames[i]));
					s.audioClipLoaded();
				}
//			}
//			catch (MalformedURLException e)
//			{
//				numSounds = 0;
//			}
		}
	}

	public void run ()
	{
		int [] playedRecently = {-1,-1,-1,-1,-1,-1,-1};
		int s = 0;
		boolean ok = false;

		try
		{
			Thread.sleep(pauseDuration*3);
		}
		catch (InterruptedException e)
		{
		}

		while (!stopFlag && !disabled)
		{
			if (!pause)
			{
				if (Math.random()<probability)
				{
					ok=false;
					while (!ok)
					{
						s = (int)(Math.random()*numSounds);
						ok = true;
						for (int i=0;i<playedRecently.length;i++)
							if (s==playedRecently[i]) ok=false;
					}
					this.s.stopSound(sounds[s]);
					this.s.playSound(sounds[s]);
					for (int i=1;i<playedRecently.length;i++)
						playedRecently[i] = playedRecently[i-1];
					playedRecently[0]=s;
				}
				try 
				{
					Thread.sleep(pauseDuration);
				}
				catch (InterruptedException e)
				{
				} 
			}
		}
	}

	public void stopMusic ()
	{
		stopFlag = true;
		t = null;
	}

	public void muteMusic ()
	{
		stopMusic ();
		for (int i=0;i<numSounds;i++)
			this.s.stopSound(sounds[i]);
	}

	public void startMusic ()
	{
		if (stopFlag)
		{
			stopFlag = false;
			int s = (int)(Math.random()*numSounds);
			this.s.playSound(sounds[s]);
			t = new Thread (this);
			t.start ();
		}
	}
}