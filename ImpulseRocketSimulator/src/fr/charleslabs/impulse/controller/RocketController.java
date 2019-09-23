package fr.charleslabs.impulse.controller;

import fr.charleslabs.impulse.rocket.Rocket;

/**
 * This class is the base for any controller that is called by the
 * PysicalEngine. It is triggered at a given frequency. <br>
 * <br>
 * To create a RocketController the "call" function must be implemented
 * through inheritance.
 * 
 * @author C.Grassin
 */
public abstract class RocketController {
	/** Frequency at which the controller is refreshed (in Hz). **/
	protected double updateFrequency;
	/**	The rocket the RocketController acts on.*/
	protected Rocket rocket;
	
	private int nbCall;

	/**
	 * Constructor to build a RocketController.
	 * 
	 * @param updateFrequency
	 *            Frequency at which the controller is refreshed (in Hz).
	 */
	public RocketController(final double updateFrequency) {
		this.setUpdateFrequency(updateFrequency);
	}
	
	/** Called at a fixed frequency. */
	protected abstract void call(final double timeSinceIgnition);

	/** To be called as often as possible by the PhysicEngine: 
	 * determines if the controller needs to be called, and call it. */
	final public void compute(final double timeSinceIgnition) {
		if ((int) (timeSinceIgnition / this.getPeriod()) > nbCall) {
			nbCall++;
			this.call(timeSinceIgnition);
		}
	}
	
	/** Initialise the controller. Called just before launching a simulation.*/
	public void init() {
		nbCall = 0;
	}
	
	/** Stops the controller. Called just before finishing a simulation.*/
	public void stop() {
		
	}
	
	/** Resets the controller. */ 
	public void reset() {
		nbCall = 0;
	}
	
	/** Gracefully closes the controller. Called before losing its reference. */
	public void close() {
	}
	
	/** Return the period of the controller, in seconds. */
	final public double getPeriod() {
		return 1.0d / updateFrequency;
	}
	
	/** Return the frequency of the controller, in Hz. */
	final public double getUpdateFrequency() {
		return updateFrequency;
	}
	
	/** Sets the period of the controller, in seconds. */
	final public void setUpdateFrequency(final double updateFrequency) {
		this.updateFrequency = updateFrequency;
	}
	
	/** Sets the frequency of the controller, in Hz. */
	public void setRocket(final Rocket rocket) {
		this.rocket = rocket;
	}
}
