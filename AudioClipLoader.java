import java.applet.Applet;
import java.net.URL;

import GameApplet.GameApplet;



public class AudioClipLoader implements Runnable
{
	GameApplet app = null;
	SchmarkanoidCanvas s; 
	Thread t = null;

	AudioClipLoader(SchmarkanoidCanvas sc, GameApplet a)
	{
		app = a;
		s =sc;
		t = new Thread(this);
		t.start();
	}

	public void run ()
	{
		String[] notes = {"note1.wav","note2.wav","note3.wav","note4.wav","note5.wav","note6.wav","note7.wav","note8.wav","note9.wav","note10.wav"};
		s.musicGenerator = new MusicGenerator("data/music/", notes, 0.9 , app, s);
		if (s.musicGenerator.isDisabled())
			s.totalAudioClips -= s.totalMusicClips;
		try
		{
			// the other sounds
			s.goodSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/GOOD.WAV"));
			//s.goodSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/good.wav"));   
			s.audioClipLoaded ();
			s.paddleSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/PADDLE.WAV"));
			//s.paddleSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/paddle.wav"));
			s.audioClipLoaded ();
			s.badSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/BAD.WAV"));
			//s.badSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/bad.wav"));
			s.audioClipLoaded ();
			s.ball.setEdgeSound (s.badSound);
			s.hardSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/HARD.WAV"));
			//s.hardSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/hard.wav"));    
			s.audioClipLoaded ();

			s.goodSoundHigh = Applet.newAudioClip(AudioClipLoader.class.getResource("data/GOODHIGH.WAV"));
			//s.goodSoundHigh = app.getAudioClip (new URL (s.codeBase.toString () + "data/goodhigh.wav"));
			s.audioClipLoaded ();
			s.paddleSoundHigh = Applet.newAudioClip(AudioClipLoader.class.getResource("data/paddlehigh.wav"));
			//s.paddleSoundHigh = app.getAudioClip (new URL (s.codeBase.toString () + "data/paddlehigh.wav"));
			s.audioClipLoaded ();
			s.badSoundHigh = Applet.newAudioClip(AudioClipLoader.class.getResource("data/BADHIGH.WAV"));
			//s.badSoundHigh = app.getAudioClip (new URL (s.codeBase.toString () + "data/badhigh.wav"));
			s.audioClipLoaded ();
			s.ball.setHighEdgeSound (s.badSoundHigh);
			s.hardSoundHigh = Applet.newAudioClip(AudioClipLoader.class.getResource("data/HARDHIGH.WAV"));
			//s.hardSoundHigh = app.getAudioClip (new URL (s.codeBase.toString () + "data/hardhigh.wav"));
			s.audioClipLoaded ();

			s.goodSoundLow = Applet.newAudioClip(AudioClipLoader.class.getResource("data/GOODLOW.WAV"));
			//s.goodSoundLow = app.getAudioClip (new URL (s.codeBase.toString () + "data/goodlow.wav"));
			s.audioClipLoaded ();
			s.paddleSoundLow = Applet.newAudioClip(AudioClipLoader.class.getResource("data/paddlelow.wav"));
			//s.paddleSoundLow = app.getAudioClip (new URL (s.codeBase.toString () + "data/paddlelow.wav"));
			s.audioClipLoaded ();
			s.badSoundLow = Applet.newAudioClip(AudioClipLoader.class.getResource("data/BADLOW.WAV"));
			//s.badSoundLow = app.getAudioClip (new URL (s.codeBase.toString () + "data/badlow.wav"));
			s.audioClipLoaded ();
			s.ball.setLowEdgeSound (s.badSoundLow);
			s.hardSoundLow = Applet.newAudioClip(AudioClipLoader.class.getResource("data/HARDLOW.WAV"));
			//s.hardSoundLow = app.getAudioClip (new URL (s.codeBase.toString () + "data/hardlow.wav"));
			s.audioClipLoaded ();

			// the music
			s.musicSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/schmarkanoid.wav"));
			//s.musicSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/schmarkanoid.wav"));
			s.audioClipLoaded ();
			s.musicEndSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/schmarkanoid_end.wav"));
			//s.musicEndSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/schmarkanoid_end.wav"));
			s.audioClipLoaded ();

			s.musicGenerator.loadClips();

			s.pillCatchSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/pillcatch.wav"));
			//s.pillCatchSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/pillcatch.wav"));
			s.audioClipLoaded ();

			s.flatRoundSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/flatround.wav"));
			//s.flatRoundSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/flatround.wav"));
			s.audioClipLoaded ();


			s.bulletSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/SHOT.WAV"));
			//s.bulletSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/shot.wav"));
			s.audioClipLoaded ();
			s.bulletHitSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/shotexplosion.wav"));
			//s.bulletHitSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/shotexplosion.wav"));
			s.audioClipLoaded ();

			s.planetSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/PLANET.WAV"));
			//s.planetSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/planet.wav"));      
			s.audioClipLoaded ();
			s.laserSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/LASER.WAV"));
			//s.laserSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/laser.wav"));
			s.audioClipLoaded ();
			s.catcherSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/CATCHER.WAV"));
			//s.catcherSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/catcher.wav"));
			s.audioClipLoaded ();

			s.jumpPaddleSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/jumppaddle.wav"));
			//s.jumpPaddleSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/jumppaddle.wav"));
			s.audioClipLoaded ();
			s.jumpSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/JUMP.WAV"));
			//s.jumpSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/jump.wav"));
			s.audioClipLoaded ();

			s.paddleChangeSizeSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/paddlechangesizelong.wav"));
			//s.paddleChangeSizeSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/paddlechangesizelong.wav"));
			s.audioClipLoaded ();
			s.paddleChangeSizeShortSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/paddlechangesizeshort.wav"));
			//	s.paddleChangeSizeShortSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/paddlechangesizeshort.wav"));
			s.audioClipLoaded ();

			s.ballBiggerSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/ballbigger.wav"));
			//s.ballBiggerSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/ballbigger.wav"));
			s.audioClipLoaded ();
			s.ballSmallerSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/ballsmaller.wav"));
			//	s.ballSmallerSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/ballsmaller.wav"));
			s.audioClipLoaded ();
			s.superBallSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/superball.wav"));
			//	s.superBallSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/superball.wav"));
			s.audioClipLoaded ();
			s.superBallEndSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/superballend.wav"));
			//	s.superBallEndSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/superballend.wav"));    
			s.audioClipLoaded ();

			s.shipSound = Applet.newAudioClip(AudioClipLoader.class.getResource("data/SHIP.WAV"));
			//s.shipSound = app.getAudioClip (new URL (s.codeBase.toString () + "data/ship.wav"));
			s.audioClipLoaded ();
		}
		catch (Exception e)
		{
			System.err.println("Problem Loading AudioClips\n");
			e.printStackTrace(System.err);
		}
	}
}