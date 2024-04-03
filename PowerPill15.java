
public class PowerPill15 extends PowerPill03 {

	public PowerPill15(SchmarkanoidCanvas sc, int t, double initialX, double initialY) 
	{
		super(sc, t, initialX, initialY);
		radius = radius *5/9;
		ballX = Math.random()*(schmarkanoid.colInc-radius*2-2)+1;		
		ballY = Math.random()*(schmarkanoid.rowInc-radius*2-2)+1;
		xSpeed = 0.4;
		ySpeed = 0.2;
		if (Math.random() < 0.5)
			xSpeed *= -1;
		if (Math.random() < 0.5)
			ySpeed *= -1;	
		}
	public String getTag()
	{
		return "Golf Ball";
	}
	public int getType()
	{
		return 15;
	}

}
