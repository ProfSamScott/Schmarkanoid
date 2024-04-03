import java.awt.Graphics;

public class PowerPill11 extends PowerPill {

	public PowerPill11(SchmarkanoidCanvas sc, int t, double initialX, double initialY)
	{
		super(sc,t,initialX,initialY);
	}
	public int getType()
	{
		return 11;
	}
	
	public String getTag()
	{
		return "Gravity On";
	}
	public void paint(Graphics g)
	{
		g.setColor (schmarkanoid.planetColor);
		//g.fillOval((int)(x+(schmarkanoid.colInc-(schmarkanoid.rowInc-2))/2), (int)y+1,schmarkanoid.rowInc-2, schmarkanoid.rowInc-2);
		g.fillArc((int)x+3, (int)y+schmarkanoid.rowInc*3/5, schmarkanoid.colInc-6, schmarkanoid.rowInc*4/5, 0, 180);
		g.setColor (pillColor);
		g.drawRoundRect ((int) x+1, (int) y+1, schmarkanoid.colInc - 2, schmarkanoid.rowInc - 2, schmarkanoid.rowInc / 2, schmarkanoid.rowInc / 2);
	}
}
