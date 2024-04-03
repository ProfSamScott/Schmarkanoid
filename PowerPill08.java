import java.awt.Graphics;

public class PowerPill08 extends PowerPillPaddle {

	private double catcherLength, finalCatcherLength, height, roundHeight, 
	animationSpeed = 0.1, numSpikes = 6;
	int pause=0, pauseDuration = 40;

	public PowerPill08(SchmarkanoidCanvas sc, int t, double initialX, double initialY) 
	{
		super(sc, t, initialX, initialY);
		width = schmarkanoid.colInc/4*2;
		height = width/5*3;
		roundHeight = height;
		catcherLength = 0;
		finalCatcherLength = 2;
	}
	public String getTag()
	{
		return "Catch Paddle";
	}
	public int getType()
	{
		return 8;
	}
	public void animateOneStep()
	{
		if (pause == 0)
		{
			catcherLength+=animationSpeed;
			if (catcherLength >= finalCatcherLength)
				pause = pauseDuration;
		}
		else
		{
			pause--;
			if (pause == 0)
				catcherLength = 0;
		}
		super.animateOneStep();
	}

	public void paint(Graphics g)
	{
		g.setColor (pillColor);
		g.drawRoundRect ((int) x+1, (int) y+1, schmarkanoid.colInc - 2, schmarkanoid.rowInc - 2, schmarkanoid.rowInc / 2, schmarkanoid.rowInc / 2);
		g.setColor (schmarkanoid.paddleColor);
		g.fillArc ((int)(x + paddleX), (int) y + schmarkanoid.rowInc/2- (int)roundHeight / 2+3, (int)width, (int)roundHeight, 0, 180);
		g.fillRoundRect ((int)(x + paddleX), (int) y +schmarkanoid.rowInc/2- (int)height / 6+3, (int)width, (int)height / 2, (int)height / 4, (int)height / 2);
		for (double i = x + paddleX + 1; i < x + paddleX + width - 1 ; i += (width-3) / numSpikes)
			g.drawLine ((int) i, paddleEdge (i, schmarkanoid.ball.getNormalRadius ()) - (int) catcherLength+2+3, (int) i, paddleEdge (i, schmarkanoid.ball.getNormalRadius ())+2+3);
	}

	int paddleEdge (double spikeX, int ballRadius)
	{
		return (int)(y  + (Math.pow ((x + paddleX + width/2 - spikeX) / ((width + ballRadius * 2) / 2), 2.0) * (roundHeight / 2)))+2;
	}
}
