package fr.charleslabs.impulse.physics;

import fr.charleslabs.impulse.controller.RocketController;
import fr.charleslabs.impulse.rocket.Rocket;

/**
 * This class is the actual PhysicalEngine of
 * Impulse. Its main computation loop is
 * threaded and calls the Rocket's physics
 * computation function and the controller
 * if it exists.
 * <br><br>
 * This implement the singleton design
 * patter.
 * 
 * @author Charles Grassin
 * 
 */
final public class PhysicsEngine implements Runnable {
	// / Constants
	public static final double G_CONSTANT = 9.81d; // in m/s2

	// / Physics Engine parameters
	private Rocket rocket;
	private RocketController controller;
	/** Refresh frequency of the engine (Hz) **/
	private double frequency = 200.0d;
	/**
	 * Factor to speed up (precision loss) or slow down (precision gain)
	 * simulation. Default value is 1 (real time).
	 **/
	private double timeFactor = 1.0;
	/**
	 * Choice between busy wait (precise, wasteful) and thread sleep (less
	 * precise, safer)
	 **/
	private final boolean threadBusyWait = false;

	// Internal variables
	private static PhysicsEngine INSTANCE = new PhysicsEngine();
	private boolean isRunning = false;
	/**Current time since epoch (motor ignition) in s*/
	private double timeSinceIgnition;

	private PhysicsEngine() {
	}
	/**
	 * @return Return the singleton instance.
	 */
	public static PhysicsEngine getInstance() {
		return INSTANCE;
	}

	// Physic engine management
	
	/** Starts the PhysicsEngine. */
	public void start() {
		if(isRunning)
			return;
		isRunning = true;
		Thread thread = new Thread(this);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}
	/** Stops the PhysicsEngine. */
	public void stop() {
		isRunning = false;
	}
	/**	Resets the engine and everything it simulates. */
	public void reset() {
		this.stop();
		if (rocket != null)
			this.rocket.reset();
		if (controller != null)
			controller.reset();
	}

	@Override
	public void run() {
		// If no Rocket object is provided, abort the simulation
		if (rocket == null)
			return;

		// Initiate simulation parameters
		timeSinceIgnition = 0;
		rocket.init();
		if (controller != null)
			controller.init();

		// Simulation variables
		double deltaT;
		double instant = System.nanoTime();

		// Main simulation loop
		while (isRunning && !rocket.isLanded()) {

			if (!threadBusyWait) { // Safe but less precise thread sleep wait:
				try {
					Thread.sleep((int) (1000 / frequency));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else { // Wasteful but precise busy wait:
				while ((System.nanoTime() - instant) / 1000000000L < (1 / frequency))
					;
			}

			// Compute the timing variables
			deltaT = (System.nanoTime() - instant) / 1000000000L * timeFactor;
			instant = System.nanoTime();
			timeSinceIgnition += deltaT;

			// Compute the physics of the rocket
			if (rocket != null)
				rocket.computeCinematics(timeSinceIgnition, deltaT);
			if (controller != null)
				controller.compute(timeSinceIgnition);
		}

		// End of simulation
		isRunning=false;
		if (controller != null)
			controller.stop();
	}

	// --- Getters and Setters ---
	public boolean isRunning() {
		return isRunning;
	}
	public void setFrequency(final double frequency) {
		this.frequency = frequency;
	}

	public void setTimeFactor(final double timeFactor) {
		this.timeFactor = timeFactor;
	}

	public Rocket getRocket() {
		return rocket;
	}

	public void setRocket(final Rocket rocket) {
		this.stop();
		this.rocket = rocket;
		if (controller != null)
			controller.setRocket(rocket);
	}

	public void setController(final RocketController controller) {
		this.stop();
		if (this.controller != null)
			this.controller.close();
		this.controller = controller;
		if (this.controller != null && this.rocket != null)
			this.controller.setRocket(rocket);
	}
}
