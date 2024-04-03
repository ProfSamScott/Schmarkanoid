
public class PowerPillPaddle extends PowerPill {

	double paddleX = 2, paddleSpeed = 0.3, width;
	public PowerPillPaddle(SchmarkanoidCanvas sc, int t, double initialX, double initialY) {
		super(sc, t, initialX, initialY);
	}

	public void animateOneStep()
	{
		paddleX += paddleSpeed;
		if (paddleX+width>=schmarkanoid.colInc-1)
		{
			paddleX = Math.max(2,schmarkanoid.colInc-2-width);
			paddleSpeed *= -1;
		}
		else if (paddleX <= 2)
		{
			paddleX-=paddleSpeed;
			paddleSpeed *= -1;
		}
	}
}
