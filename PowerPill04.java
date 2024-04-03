
public class PowerPill04 extends PowerPill03 {

	public PowerPill04(SchmarkanoidCanvas sc, int t, double initialX, double initialY) 
	{
		super(sc, t, initialX, initialY);
		xSpeed = 0.2;
		ySpeed = 0.05;
	}
	public String getTag()
	{
		return "Slower Ball";
	}
	public int getType()
	{
		return 4;
	}

}
