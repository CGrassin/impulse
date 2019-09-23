package fr.charleslabs.impulse.gimbal;

/**
 * A basic thrust gimbal class. Gimbal
 * displacement is immediate (unrealistic).
 * 
 * @author Charles Grassin
 * 
 */
public class Gimbal {
	/** Gimbal angle, in degrees. */
	protected double gimbalAngleX, gimbalAngleY;
	
	/**
	 * Magnitude of the max angle of the gimbal system, for both X and Y axes,
	 * in degrees. Should always be inferior to 90 degrees.
	 */
	protected double limitAngle;
	
	/**
	 * Constructor for a gimbal with
	 * a given maximum angle displacement.
	 * 
	 * @param limitAngle The maximum angle of the
	 * gimbal system, in degrees.
	 */
	public Gimbal(final double limitAngle) {
		super();
		this.limitAngle = limitAngle;
	}
	
	/** Function called as often as possible by the PhysicsEngine.
	 * Inherit to change behaviour. 
	 * @param deltaT Time since last update (in s).
	 */
	public void compute(final double deltaT) {
		checkLimits();
	}
	
	/** Resets the gimbal to zero. */
	public void reset() {
		gimbalAngleX = 0;
		gimbalAngleY = 0;
	}
	
	/** Function called by the PysicsEngine before the 
	 * simulation starts. */
	public void init() {}
	
	/** Return the current gimbal X angle, in degrees. */
	public double getGimbalAngleX() {
		return gimbalAngleX;
	}
	
	/** Sets the current gimbal X angle, in degrees. */
	public void setGimbalAngleX(final double gimbalAngleX) {
		this.gimbalAngleX = gimbalAngleX;
		checkLimits();
	}

	/** Return the current gimbal Y angle, in degrees. */
	public double getGimbalAngleY() {
		return gimbalAngleY;

	}
	
	/** Sets the current gimbal Y angle, in degrees. */
	public void setGimbalAngleY(final double gimbalAngleY) {
		this.gimbalAngleY = gimbalAngleY;
		checkLimits();
	}

	/** Return the limit angle of the gimbal (for both X and Y)
	 * , in degrees. */
	public double getLimitAngle() {
		return limitAngle;
	}
	
	/** Sets the limit angle of the gimbal (for both X and Y)
	 * , in degrees. */
	public void setLimitAngle(final double limitAngle) {
		this.limitAngle = limitAngle;
	}
	
	/** Checks the gimbal angle value is within bounds. */
	protected void checkLimits() {
		// X
		if (gimbalAngleX > limitAngle)
			gimbalAngleX = limitAngle;
		else if (gimbalAngleX < -limitAngle)
			gimbalAngleX = -limitAngle;
		// Y
		if (gimbalAngleY > limitAngle)
			gimbalAngleY = limitAngle;
		else if (gimbalAngleY < -limitAngle)
			gimbalAngleY = -limitAngle;
	}
}
