import java.awt.Dimension;

import javax.swing.JFrame;

import GameApplet.GameApplet;

//Schmarkanoid! Version 1.0
//<applet code="Schmarkanoid" width=600 height=400> </applet>

//Author: Sam Scott (SamScott@Canada.com)
//Date:   April, 2006

//License: Gnu or something?




public class Schmarkanoid extends GameApplet
{
	public static void main(String[] args) {
		JFrame f = new JFrame("Schmarkanoid");
		Schmarkanoid s = new Schmarkanoid();
		f.setContentPane(s);
		s.setPreferredSize(new Dimension(600,400));
		s.init();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.pack();
		f.setVisible(true);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SchmarkanoidCanvas gamePanel;
	/**
	 * Calls makeGUI() to create a new JPanel, then calls
	 * setContentPane to put the JPanel in place on the JApplet
	 *
	 **/
	public void init ()
	{
		gamePanel = new SchmarkanoidCanvas(this);
		gamePanel.setFocusable(true);
		setContentPane (gamePanel);
	} // init method


	public void smartRepaint()
	{
		gamePanel.smartRepaint();
	}
}
