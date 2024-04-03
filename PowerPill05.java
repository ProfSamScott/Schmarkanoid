import java.awt.Graphics;


public class PowerPill05 extends PowerPill03 {

	double innerRadius = 4;
	double superBallAnimationSpeed = 0.15;
	
	public PowerPill05(SchmarkanoidCanvas sc, int t, double initialX, double initialY) 
	{
		super(sc, t, initialX, initialY);
		xSpeed = 0.5;
		ySpeed = 0.25;
	}
	public String getTag()
	{
		return "Superball";
	}
	public int getType()
	{
		return 5;
	}
	public void animateOneStep()
	{
		super.animateOneStep();
		innerRadius -= superBallAnimationSpeed;
		if (innerRadius<=1 || innerRadius >= radius)
			superBallAnimationSpeed *= -1;
	}
	public void paint(Graphics g)
	{
		g.setColor (pillColor);
		g.drawRoundRect ((int) x+1, (int) y+1, schmarkanoid.colInc - 2, schmarkanoid.rowInc - 2, schmarkanoid.rowInc / 2, schmarkanoid.rowInc / 2);
		//g.setColor(schmarkanoid.ball.superBallHaloColor);
		//g.drawOval ((int) (x+ballX-1), (int) (y+ballY-1), (int) (radius+1) *2-1, (int) (radius+1) *2-1);
		g.setColor(schmarkanoid.ballColor);
		g.fillOval ((int) (x+ballX+(radius-innerRadius)), (int) (y+ballY+(radius-innerRadius)), (int) innerRadius * 2, (int) innerRadius * 2);
	}
}