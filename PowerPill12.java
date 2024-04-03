import java.awt.Color;
import java.awt.Graphics;

public class PowerPill12 extends PowerPill {

	int pause = 50, pauseDuration = 50, toggle = 1;
	Color xColor;
	public PowerPill12(SchmarkanoidCanvas sc, int t, double initialX, double initialY)
	{
		super(sc,t,initialX,initialY);
		xColor = new Color(200,0,0);
	}
	public int getType()
	{
		return 12;
	}

	public String getTag()
	{
		return "Gravity Off";
	}
	public void animateOneStep()
	{
		pause--;
		if (pause==0)
		{
			toggle *= -1;
			pause = pauseDuration;
		}
	}
	public void paint(Graphics g)
	{
		if (toggle == 1)
		{
			g.setColor(xColor);
			g.fillRoundRect ((int) x+1, (int) y+1, schmarkanoid.colInc - 2, schmarkanoid.rowInc - 2, schmarkanoid.rowInc / 2, schmarkanoid.rowInc / 2);
		}
		g.setColor (schmarkanoid.planetColor);
		//g.fillOval((int)(x+(schmarkanoid.colInc-(schmarkanoid.rowInc-2))/2), (int)y+1,schmarkanoid.rowInc-2, schmarkanoid.rowInc-2);
		g.fillArc((int)x+3, (int)y+schmarkanoid.rowInc*3/5, schmarkanoid.colInc-6, schmarkanoid.rowInc*4/5, 0, 180);
		g.setColor (pillColor);
		g.drawRoundRect ((int) x+1, (int) y+1, schmarkanoid.colInc - 2, schmarkanoid.rowInc - 2, schmarkanoid.rowInc / 2, schmarkanoid.rowInc / 2);
	}
}