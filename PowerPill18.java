import java.awt.Graphics;

public class PowerPill18 extends PowerPill {

	private int radius = 3;
	private double animationParam;
	private int initialAP;
	private double animationSpeed = 0.5;
	
	public PowerPill18(SchmarkanoidCanvas sc, int t, double initialX,double initialY) 
	{
		super(sc, t, initialX, initialY);
		initialAP = schmarkanoid.colInc/2-radius;
		animationParam = initialAP;
	}
	
	public int getType()
	{
		return 18;
	}

	public String getTag()
	{
		return "Double Multiball";
	}
	
	public void animateOneStep()
	{
		animationParam-=animationSpeed;
		if (animationParam <= -initialAP)
		{
			animationParam = initialAP;
		}
	}
	
	public void paint(Graphics g)
	{
		g.setColor(schmarkanoid.extraBallColor);
		g.fillOval((int)x+schmarkanoid.colInc/2-radius+(int)animationParam, (int)y+schmarkanoid.colInc/2-radius, 2*radius, 2*radius);		
		g.setColor(schmarkanoid.extraBallColor);
		g.fillOval((int)x+schmarkanoid.colInc/2-radius+(int)animationParam/2, (int)y+schmarkanoid.colInc/2-radius, 2*radius, 2*radius);		
		g.setColor(schmarkanoid.ballColor);
		g.fillOval((int)x+schmarkanoid.colInc/2-radius, (int)y+schmarkanoid.colInc/2-radius, 2*radius, 2*radius);
		g.setColor(schmarkanoid.extraBallColor);
		g.fillOval((int)x+schmarkanoid.colInc/2-radius-(int)animationParam, (int)y+schmarkanoid.colInc/2-radius, 2*radius, 2*radius);		
		g.setColor(schmarkanoid.extraBallColor);
		g.fillOval((int)x+schmarkanoid.colInc/2-radius-(int)animationParam/2, (int)y+schmarkanoid.colInc/2-radius, 2*radius, 2*radius);		
	}
}

