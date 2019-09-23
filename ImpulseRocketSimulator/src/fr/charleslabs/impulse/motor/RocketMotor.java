package fr.charleslabs.impulse.motor;

/**
 * A RocketMotor delivers thrust.
 * 
 * @author Charles Grassin
 */
public abstract class RocketMotor {
	/** Return the thrust of the motor based on the current time. 
	 * The thrust is in Newtons.
	 * @param currentTime Time since epoch.
	 */
	abstract public double getThrust(final double currentTime);
}
