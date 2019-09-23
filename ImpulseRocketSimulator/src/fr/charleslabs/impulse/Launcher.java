package fr.charleslabs.impulse;

import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.bulenkov.darcula.DarculaLaf;

import fr.charleslabs.impulse.ui.UserInterface;

//---
// Before next release:
//TODO Aerodynamic forces simulation
//TODO Save/Load rocket
//TODO Sensor simulation
//TODO Graphical rocket creator (?)
//TODO Move around 3D view

/**
 * Main launcher for the app.
 * 
 * @author Charles Grassin
 */
public final class Launcher {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					// Workaround to run DarculaLaf on GNU/Linux
					UIManager.getFont("Label.font");

					// Use DarculaLaf (Swing Look and Feel)
					final LookAndFeel lookAndFeel = new DarculaLaf();
					if (lookAndFeel.isSupportedLookAndFeel())
						UIManager.setLookAndFeel(lookAndFeel);
				} catch (Exception ignored) {
				}

				// Launch the simulation UI
				@SuppressWarnings("unused")
				final UserInterface ui = new UserInterface();
			}
		});
	}
}
