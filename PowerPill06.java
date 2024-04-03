import java.awt.Graphics;

public class PowerPill06 extends PowerPillPaddle {

	private double /*initialRoundHeight,*/ height/*, roundHeight, animationSpeed = 0.2*/;
	int pause, pauseDuration = 40;

	public PowerPill06(SchmarkanoidCanvas sc, int t, double initialX, double initialY) 
	{
		super(sc, t, initialX, initialY);
		width = schmarkanoid.colInc/4*2;
		height = width/5*3;
		//roundHeight = 0;
		//initialRoundHeight = height;
	}
	public String getTag()
	{
		return "Flat Paddle";
	}
	public int getType()
	{
		return 6;
	}
/*	public void animateOneStep()
	{
		if (pause == 0)
		{
			roundHeight-=animationSpeed;
			if (roundHeight <= 0)
			{
				roundHeight = 0;
				pause = pauseDuration;
			}
		}
		else
		{
			pause--;
			if (pause == 0)
				roundHeight = initialRoundHeight;
		}
	}*/

	public void paint(Graphics g)
	{
		g.setColor (pillColor);
		g.drawRoundRect ((int) x+1, (int) y+1, schmarkanoid.colInc - 2, schmarkanoid.rowInc - 2, schmarkanoid.rowInc / 2, schmarkanoid.rowInc / 2);
		g.setColor (schmarkanoid.paddleColor);
		//g.fillArc ((int)(x + paddleX), (int) y + schmarkanoid.rowInc/2- (int)roundHeight / 2+3, (int)width, (int)roundHeight, 0, 180);
		g.fillRoundRect ((int)(x + paddleX), (int) y +schmarkanoid.rowInc/2- (int)height / 6+3, (int)width, (int)height / 2, (int)height / 4, (int)height / 2);
	}
}
