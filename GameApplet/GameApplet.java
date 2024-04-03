// Class: GameApplet
// Author: Sam Scott
// Date: April 19, 2006
// - the superclass for any game applet

package GameApplet;

import javax.swing.JApplet;

public abstract class GameApplet extends JApplet 
{
  public abstract void smartRepaint ();
}
