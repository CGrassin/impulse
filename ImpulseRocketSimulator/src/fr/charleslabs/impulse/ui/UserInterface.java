package fr.charleslabs.impulse.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import fr.charleslabs.impulse.physics.PhysicsEngine;
import fr.charleslabs.impulse.rocket.Rocket;

/**
 * This class is the main user interface of the simulator. It calls the other
 * user interface elements and lays out the interface. <br>
 * <br>
 * Additionally, it also calls the physics engine and runs as a thread to
 * provide the real-time display.
 * 
 * @author Charles Grassin
 * 
 */
final public class UserInterface extends JFrame implements Runnable,
		ActionListener {
	// Constants
	private static final long serialVersionUID = 1L;
	private final int refreshFrequency = 30;
	private static final String logoPath = "/res/logo.png";
	
	// Status variables
	private boolean isRunning = false;

	// UI - Swing variables
	private SidebarControlsPanel controls = new SidebarControlsPanel(this);
	private RocketCreatorPanel creatorPanel= new RocketCreatorPanel(this);
	private Rocket3DView view3D;
	private GraphsPanel angularGraphPanel= new GraphsPanel(1000.0d / refreshFrequency, "deg"), linearGraphPanel= new GraphsPanel(1000.0d / refreshFrequency, "m");

	// Other variable
	protected PhysicsEngine engine = new PhysicsEngine();
	protected Rocket rocket;
	
	public UserInterface() throws HeadlessException {
		super(R.windowTitle);
		
		// Load UI components
		if (Rocket3DView.isCompatible())
			view3D = new Rocket3DView();

		// Tabs
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab(R.angularTabTitle, null, angularGraphPanel,
				R.angularTabTooltip);
		tabs.addTab(R.linearTabTitle, null, linearGraphPanel,
				R.linearTabTooltip);
		if (view3D != null)
			tabs.addTab(R.view3DTabTitle, null, view3D,
					R.view3DTabTooltip);
		tabs.addTab(R.settingsTabTitle, null, creatorPanel,
				R.settingsTabTooltip);
		try {
			tabs.addTab(R.helpTabTitle, null, new HelpPanel(),
					R.helpTabTooltip);
		} catch (IOException ignored) {
		}

		// Build window and set visibility
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(tabs, BorderLayout.CENTER);
		this.getContentPane().add(controls, BorderLayout.EAST);
		this.setIconImage(new ImageIcon(getClass().getResource(logoPath)).getImage());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(800, 600));
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		// Prepare simulator
		try {
			rocket = new Rocket();
			engine.addObject(rocket);
			creatorPanel.makeRocket(rocket);
			controls.setPID(rocket);
		} catch (Exception ignored) {}
	}

	// --- Thread management ---
	/** Resets all the data displayed on the user interface. */
	private void reset() {
		this.angularGraphPanel.clear();
		this.linearGraphPanel.clear();
	}

	/** Stops the running thread if it is running. */
	private void stop() {
		isRunning = false;
	}

	/** Starts the user interface thread (see public void run()) */
	private void start() {
		if (!isRunning) {
			isRunning = true;
			new Thread(this).start();
		}
	}

	@Override
	public void run() {
		// Prepare UI
		controls.updateInterface(true);
		this.setTitle(R.windowTitle + R.runningWindowTitle);

		// Init. physics engine before simulation
		engine.reset();
		controls.addTorque(rocket);

		// Simulation
		engine.start(true);
		while (isRunning && engine.isRunning()) {
			try {
				Thread.sleep(1000 / refreshFrequency);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			refreshView((Rocket)engine.getObjects().get(0));
			this.repaint();
		}
		engine.stop();

		// Post-simulation
		isRunning = false;
		controls.updateInterface(false);
		this.setTitle(R.windowTitle);
	}

	/**
	 * Adds/updates the current simulator data to the interface.
	 * 
	 * @param rocket
	 *            The current simulated rocket.
	 */
	private void refreshView(final Rocket rocket) {
		if (view3D != null)
			view3D.setPostion(rocket.getLinearMotion().position.z,
					rocket.getAngularMotion().position.x,
					rocket.getAngularMotion().position.y);

		this.angularGraphPanel.addPoint(rocket.getAngularMotion());
		this.linearGraphPanel.addPoint(rocket.getLinearMotion());
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// Start and Stop btn
		if (event.getSource() == controls.startStopBtn) {
			if (!this.isRunning) {
				this.reset();
				this.start();
			} else
				this.stop();
		}
		// Apply angle offset btn
		else if (event.getSource() == controls.addTorqueBtn) {
			controls.addTorque(rocket);
		}
		// Connect to serial port btn
		else if (event.getSource() == controls.serialConnectBtn) {
			this.stop();
			controls.serialConnect(this,rocket);
		}
		// PID Apply Btn
		else if (event.getSource() == controls.pidApplyBtn) {
			this.stop();
			controls.setPID(rocket);
		}
		// Rocket create btn
		else if (event.getSource() == creatorPanel.applyBtn) {
			this.stop();
			try {
				creatorPanel.makeRocket(rocket);
				JOptionPane.showMessageDialog(this,
						R.rocketCreateDialogMessage,
						R.rocketCreateDialogTitle,
						JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(
						this,
						R.rocketCreateErrorDialogMessage
								+ e.getMessage(),
						R.rocketCreateErrorDialogTitle,
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}
}
