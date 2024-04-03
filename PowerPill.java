import java.awt.Color;
import java.awt.Graphics;

import GameApplet.AnimatedScreenObject;


public class PowerPill extends AnimatedScreenObject {

	Color pillColor = Color.white;
	SchmarkanoidCanvas schmarkanoid;
	int type;
	final String[] powerPillTag;

	public PowerPill(SchmarkanoidCanvas sc, int t, double initialX, double initialY)
	{
		schmarkanoid = sc;
		type = t;
		setX(initialX);
		setY(initialY);
		powerPillTag =  new String [schmarkanoid.numPowerPills];
		powerPillTag [0] = "Ex";  // expand paddle
		powerPillTag [1] = "Sh";  // shrink paddle
		powerPillTag [2] = "Fa";  // faster ball
		powerPillTag [3] = "Sl";  // slower ball
		powerPillTag [4] = "SB";  // superball
		powerPillTag [5] = "Fl";  // flat paddle
		powerPillTag [6] = "Ro";  // rounded paddle
		powerPillTag [7] = "Ca";  // catch paddle
		powerPillTag [8] = "Ju";  // jump paddle
		powerPillTag [9] = "La";  // laser paddle
		powerPillTag [10] = "G+"; // gravity on
		powerPillTag [11] = "G-"; // gravity off
		powerPillTag [12] = "CD"; // change ball direction
		powerPillTag [13] = "B+"; // expand ball
		powerPillTag [14] = "B-"; // shrink ball
		powerPillTag [15] = "Re"; // reset speed and powerups
		powerPillTag [16] = "mb"; // multi-ball pill
		powerPillTag [17] = "MB"; // multi-ball x2
		//powerPillTag [18] = "??"; // should always be the last one
	}

	public int getType()
	{
		return type;
	}
	
	public String getTag()
	{
		return powerPillTag[type - 1];
	}
	public void animateOneStep()
	{

	}

	@Override
	public double getBottom() {
		return y+1+schmarkanoid.rowInc-2;
	}

	@Override
	public double getLeft() {
		// TODO Auto-generated method stub
		return x+1;
	}

	@Override
	public double getRight() {
		return y+1+schmarkanoid.colInc-2;
	}

	@Override
	public double getTop() {
		// TODO Auto-generated method stub
		return y+1;
	}

	@Override
	public void paint(Graphics g) {
		g.setColor (pillColor);
		g.drawRoundRect ((int) x+1, (int) y+1, schmarkanoid.colInc - 2, schmarkanoid.rowInc - 2, schmarkanoid.rowInc / 2, schmarkanoid.rowInc / 2);
		g.drawString (powerPillTag [type - 1], (int) x + schmarkanoid.colInc / 4, (int) y + schmarkanoid.rowInc - 3);
	}
}
