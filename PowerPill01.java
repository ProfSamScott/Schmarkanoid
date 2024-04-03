import java.awt.Graphics;

public class PowerPill01 extends PowerPillPaddle {

	private double initialWidth, height, roundHeight/*, animationSpeed = 0.2*/;
	int pause, pauseDuration = 25;

	public PowerPill01(SchmarkanoidCanvas sc, int t, double initialX,
			double initialY) {
		super(sc, t, initialX, initialY);
		initialWidth = schmarkanoid.colInc*2/4;
		width = initialWidth*1.6;
		height = initialWidth/5*3;
		roundHeight = height;
	}

	public String getTag()
	{
		return "Big Paddle";
	}
	public int getType()
	{
		return 1;
	}
/*	public void animateOneStep()
	{
		if (pause == 0)
		{
			width+=animationSpeed;
			if (width >= initialWidth*1.75)
				pause = pauseDuration;
		}
		else
		{
			pause--;
			if (pause == 0)
				width = initialWidth;
		}
		super.animateOneStep();
	}*/

	public void paint(Graphics g)
	{
		g.setColor (pillColor);
		g.drawRoundRect ((int) x+1, (int) y+1, schmarkanoid.colInc - 2, schmarkanoid.rowInc - 2, schmarkanoid.rowInc / 2, schmarkanoid.rowInc / 2);
		g.setColor (schmarkanoid.paddleColor);
		//g.fillArc ((int)x + schmarkanoid.colInc/2 - (int)width / 2, (int) y + schmarkanoid.rowInc/2- (int)roundHeight / 2, (int)width, (int)roundHeight, 0, 180);
		g.fillArc ((int)(x + paddleX), (int) y + schmarkanoid.rowInc/2- (int)roundHeight / 2+3, (int)width, (int)roundHeight, 0, 180);
		//g.fillRoundRect ((int)x + schmarkanoid.colInc/2- (int)width / 2, (int) y +schmarkanoid.rowInc/2- (int)height / 6, (int)width, (int)height / 2, (int)height / 4, (int)height / 2);
		g.fillRoundRect ((int)(x + paddleX), (int) y +schmarkanoid.rowInc/2- (int)height / 6+3, (int)width, (int)height / 2, (int)height / 4, (int)height / 2);
	}
}
