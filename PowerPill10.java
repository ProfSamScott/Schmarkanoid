import java.awt.Graphics;

public class PowerPill10 extends PowerPillPaddle {

	private double height, roundHeight, bulletHeight1, bulletHeight2, realWidth,
	animationSpeed = 0.5, maxBulletHeight = 6;
	int pause1=0, pause2=0, pauseDuration = 10, laserWidth = 3;

	public PowerPill10(SchmarkanoidCanvas sc, int t, double initialX, double initialY) 
	{
		super(sc, t, initialX, initialY);
		realWidth = schmarkanoid.colInc/4*2;
		width = realWidth+4;
		height = realWidth/5*3;
		roundHeight = height;
		bulletHeight1 = 0;
		bulletHeight2 = maxBulletHeight/2;
	}
	public String getTag()
	{
		return "Laser Paddle";
	}
	public int getType()
	{
		return 10;
	}
	public void animateOneStep()
	{
		if (pause1==0)
		{
			bulletHeight1+=animationSpeed;
			if (bulletHeight1 >= maxBulletHeight)
			{
				bulletHeight1 = 0;
				pause1=pauseDuration;
			}
		}
		else
			pause1--;

		if (pause2==0)
		{
			bulletHeight2+=animationSpeed;

			if (bulletHeight2 >= maxBulletHeight)
			{
				bulletHeight2 = 0;
				pause2=pauseDuration;
			}
		}
		else 
			pause2--;
		super.animateOneStep();
	}

	public void paint(Graphics g)
	{
		g.setColor (schmarkanoid.paddleColor);
		g.fillArc ((int)(x+paddleX)+2, (int) y + schmarkanoid.rowInc/2- (int)roundHeight / 2+3, (int)realWidth, (int)roundHeight, 0, 180);
		g.fillRoundRect ((int)(x+paddleX)+2, (int) y +schmarkanoid.rowInc/2- (int)height / 6+3, (int)realWidth, (int)height / 2, (int)height / 4, (int)height / 2);
		g.fillRect ((int)((x+paddleX) - (laserWidth + 1))+2, (int) y + schmarkanoid.rowInc/2+4, (int)(realWidth + (laserWidth + 1) * 2), (int)height / 6);
		g.fillRect ((int)((x+paddleX) - laserWidth)+2, (int) y + schmarkanoid.rowInc/2 - 2+4, (int) laserWidth, 2);
		g.fillRect ((int)((x+paddleX) + realWidth + (laserWidth - 3))+2, (int) y + schmarkanoid.rowInc/2 - 2+4, (int) laserWidth, 2);
		g.fillRect ((int)((x+paddleX) - (laserWidth - 1))+2, (int) (y + schmarkanoid.rowInc/2 - 5 - bulletHeight1)+4, (int) laserWidth - 2, 3);
		g.fillRect ((int)((x+paddleX) + realWidth + (laserWidth - 2))+2, (int) (y + schmarkanoid.rowInc/2 - 5 - bulletHeight2)+4, (int) laserWidth - 2, 3);
		g.setColor (pillColor);
		g.drawRoundRect ((int) x+1, (int) y+1, schmarkanoid.colInc - 2, schmarkanoid.rowInc - 2, schmarkanoid.rowInc / 2, schmarkanoid.rowInc / 2);
	}
}
