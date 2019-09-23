package fr.charleslabs.impulse.physics;

import java.util.ArrayList;
import java.util.List;

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
	 // in m/s2

	// / Physics Engine parameters
	private List<PhysicalObject> objects = new ArrayList<PhysicalObject>(1); 

	/** Refresh frequency of the engine (Hz) **/
	private double frequency = 200.0d;
	/**
	 * Factor to speed up (precision loss) or slow down (precision gain)
	 * simulation. Default value is 1 (real time).
	 **/
	private double timeFactor = 1.0;


	// Internal variables
	private boolean isRunning = false;
	private boolean isRealTime = false;
	/**Current time since epoch in s*/
	private double timeSinceIgnition;
	/**
	 * Choice between busy wait (precise, wasteful) and thread sleep (less
	 * precise, safer)
	 **/
	private final boolean threadBusyWait = false;
	
	/** Constructor for real time mode. **/
	public PhysicsEngine(){
	}

	
	/** Starts the PhysicsEngine. */
	public void start(boolean isRealTime) {
		if(isRunning)
			return;
		this.isRealTime = isRealTime;
		this.isRunning = true;
		final Thread thread = new Thread(this);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}
	/** Stops the PhysicsEngine. */
	public void stop() {
		isRunning = false;
	}
	
	/**	Resets the engine and everything it simulates. */
	public void reset() {
		for(PhysicalObject object : objects)
			object.reset();
	}
	private boolean isSimulationOver(){
		for(PhysicalObject object : objects)
			if(object.isSimulationOver())
				return true;
		return false;
	}
	
	@Override
	public void run() {
		// If no Rocket object is provided, abort the simulation
		if (objects.isEmpty())
			return;
		timeSinceIgnition = 0;

		for(PhysicalObject object : objects)
			object.init();
		
		// Simulation variables
		double deltaT;
		double instant = System.nanoTime();
		
		// Main simulation loop
		while (isRunning) {
			if (isRealTime){
				if (!threadBusyWait) { // Safe but less precise thread sleep wait:
					try {
						Thread.sleep((int) (1000 / frequency));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else { // Wasteful but precise busy wait:
					while ((System.nanoTime() - instant) / 1000000000L < (1 / frequency));
				}

				// Compute the timing variables
				deltaT = (System.nanoTime() - instant) / 1000000000L * timeFactor;
				instant = System.nanoTime();
			}
			else {
				deltaT = (1 / frequency);
			}
			
			timeSinceIgnition += deltaT;

			// Compute the physics of the rocket
			for(PhysicalObject object : objects)
				object.computeCinematics(timeSinceIgnition, deltaT);
				
			if(isSimulationOver()) isRunning = false;
		}

		// End of simulation
		isRunning=false;
		for(PhysicalObject object : objects)
			object.stop();
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

	
	public List<PhysicalObject> getObjects() {
		return objects;
	}
	public void addObject(final PhysicalObject object) {
		this.stop();
		this.objects.add(object);
	}
}
