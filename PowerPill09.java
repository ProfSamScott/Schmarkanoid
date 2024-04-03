import java.awt.Graphics;

public class PowerPill09 extends PowerPillPaddle {

	private double height, roundHeight, jump, finalJumpHeight,
	animationSpeed = 0.5, jumpPaddleHeight;
	int pause=0, pauseDuration = 40;
	boolean direction = true;

	public PowerPill09(SchmarkanoidCanvas sc, int t, double initialX, double initialY) 
	{
		super(sc, t, initialX, initialY);
		width = schmarkanoid.colInc/4*2;
		height = width/5*3;
		roundHeight = height;
		jumpPaddleHeight = height / 3;
		finalJumpHeight = height *1/3;
		jump = 0;
	}
	public String getTag()
	{
		return "Jump Paddle";
	}
	public int getType()
	{
		return 9;
	}
	public void animateOneStep()
	{
		if (pause == 0)
		{
			if (direction)
			{
				jump+=animationSpeed;
				if (jump >= finalJumpHeight)
					direction = false;
			}
			else
			{
				jump-=animationSpeed;
				if (jump <= 0)
				{
					direction = true;
					pause = pauseDuration;
				}
			}
		}
		else
			pause--;
		super.animateOneStep();
	}

	public void paint(Graphics g)
	{
		g.setColor (pillColor);
		g.drawRoundRect ((int) x+1, (int) y+1, schmarkanoid.colInc - 2, schmarkanoid.rowInc - 2, schmarkanoid.rowInc / 2, schmarkanoid.rowInc / 2);
		g.setColor (schmarkanoid.paddleColor);
		g.fillArc ((int)(x + paddleX), (int) (y -jump+ schmarkanoid.rowInc/2- (int)roundHeight / 2)+2, (int)width, (int)roundHeight, 0, 180);
		g.fillRoundRect ((int)(x + paddleX), (int) (y-jump +schmarkanoid.rowInc/2- (int)height / 6)+2, (int)width, (int)height / 2, (int)height / 4, (int)height / 2);
		g.fillRect ((int)(x + paddleX + width/2 - width / 4), (int) y + schmarkanoid.rowInc/2 + (int) (jumpPaddleHeight *16/12)+3, (int)width / 2, 1);
		g.fillRect ((int)(x + paddleX + width/2 - 1), (int) (y + schmarkanoid.rowInc/2-jump)+2, 2, (int) (jumpPaddleHeight*9/6 +jump));
	}
}
