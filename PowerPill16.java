import java.awt.Color;
import java.awt.Graphics;

public class PowerPill16 extends PowerPill {

	int pause = 50, pauseDuration = 50, toggle = 1;
	Color xColor;
	public PowerPill16(SchmarkanoidCanvas sc, int t, double initialX, double initialY) 
	{
		super(sc, t, initialX, initialY);
		xColor = new Color(200,0,0);
	}
	public String getTag()
	{
		return "Reset";
	}
	public int getType()
	{
		return 16;
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
			g.setColor (xColor);
			g.fillRoundRect ((int) x+1, (int) y+1, schmarkanoid.colInc - 2, schmarkanoid.rowInc - 2, schmarkanoid.rowInc / 2, schmarkanoid.rowInc / 2);
		}
		g.setColor (pillColor);
		g.drawRoundRect ((int) x+1, (int) y+1, schmarkanoid.colInc - 2, schmarkanoid.rowInc - 2, schmarkanoid.rowInc / 2, schmarkanoid.rowInc / 2);
		//g.setFont(new Font("SansSerif",Font.BOLD,12));
		//g.drawString("XXX",(int)x+2, (int)y+schmarkanoid.rowInc-2);
	}

}
