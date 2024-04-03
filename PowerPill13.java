
public class PowerPill13 extends PowerPill03 {

	public PowerPill13(SchmarkanoidCanvas sc, int t, double initialX, double initialY) 
	{
		super(sc, t, initialX, initialY);
		xSpeed = 0.2;
		ySpeed = 0.05;
		radius = 8;
	}
	public String getTag()
	{
		return "Change Direction";
	}
	public int getType()
	{
		return 13;
	}

}
