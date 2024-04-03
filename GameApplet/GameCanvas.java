// Class: GameApplet
// Author: Sam Scott
// Date: April 19, 2006
// - the superclass for any game applet

package GameApplet;

import javax.swing.JPanel;

public abstract class GameCanvas extends JPanel 
{
  public abstract void smartRepaint ();
}
