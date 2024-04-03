import java.awt.Graphics;

public class PowerPill03 extends PowerPill {

	protected double ballX, ballY, xSpeed = 1, ySpeed=0.5;
	protected int radius = 4;

	public PowerPill03(SchmarkanoidCanvas sc, int t, double initialX, double initialY) 
	{
		super(sc, t, initialX, initialY);
		ballX = Math.random()*(schmarkanoid.colInc-radius*2-2)+1;		
		ballY = Math.random()*(schmarkanoid.rowInc-radius*2-2)+1;
		if (Math.random() < 0.5)
			xSpeed *= -1;
		if (Math.random() < 0.5)
			ySpeed *= -1;	
	}
	public String getTag()
	{
		return "Faster Ball";
	}
	public int getType()
	{
		return 3;
	}
	public void animateOneStep()
	{
		ballX+=xSpeed;
		if (ballX >= schmarkanoid.colInc-radius*2 || ballX <= 0)
		{
			ballX-=xSpeed;
			xSpeed *= -1;
		}
		ballY+=ySpeed;
		if (ballY >= schmarkanoid.rowInc-radius*2 || ballY <= 1)
		{
			ballY -= ySpeed;
			ySpeed *= -1;
		}
	}

	public void paint(Graphics g)
	{
		g.setColor (pillColor);
		g.drawRoundRect ((int) x+1, (int) y+1, schmarkanoid.colInc - 2, schmarkanoid.rowInc - 2, schmarkanoid.rowInc / 2, schmarkanoid.rowInc / 2);
		g.setColor (schmarkanoid.ballColor);
		g.fillOval((int)(x+ballX), (int)(y+ballY), radius*2, radius*2);
	}

}
