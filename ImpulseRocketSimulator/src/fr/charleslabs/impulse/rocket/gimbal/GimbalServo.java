package fr.charleslabs.impulse.rocket.gimbal;

/**
 * This class is a thruster gimbal, where the displacement
 * of the angle reacts according to a first order equation 
 * (angle=slope*t+initialAngle).
 * 
 * The slope of this function is determine by the "anglePerS" variable.
 * 
 * @author C.Grassin
 * 
 */
public class GimbalServo extends Gimbal {
	/**  Gimbal target angle, in degrees.  */
	double targetAngleX, targetAngleY;
	/** Rate of change of the gimbal angle, in degrees/s. */
	double anglePerS;
	
	/**
	 * Constructor for a gimbal with
	 * a given maximum angle displacement and a
	 * angular speed.
	 * <br><br>
	 * For instance GimbalServo(20, 100) means that
	 * the gimbal would take 20/100=0.2 seconds to go from
	 * 0 degrees to its limit angle of 20 degrees.
	 * 
	 * @param limitAngle The maximum angle of the
	 * gimbal system, in degrees.
	 * @param anglePerS The rate of change of the gimbal,
	 * in degrees per second.
	 */
	public GimbalServo(final double limitAngle, final double anglePerS) {
		super(limitAngle);
		this.anglePerS = anglePerS;
	}

	@Override
	public void reset() {
		super.reset();
		this.targetAngleX = 0;
		this.targetAngleY = 0;
	}

	@Override
	public void compute(final double deltaT) {
		double maxDisplacement = anglePerS * deltaT;
		// X
		if (Math.abs(targetAngleX - gimbalAngleX) < maxDisplacement) {
			gimbalAngleX = targetAngleX;
		} else {
			if (gimbalAngleX < targetAngleX)
				gimbalAngleX += maxDisplacement;
			else
				gimbalAngleX -= maxDisplacement;
		}
		// Y
		if (Math.abs(targetAngleY - gimbalAngleY) < maxDisplacement) {
			gimbalAngleY = targetAngleY;
		} else {
			if (gimbalAngleY < targetAngleY)
				gimbalAngleY += maxDisplacement;
			else
				gimbalAngleY -= maxDisplacement;
		}
		checkLimits();
	}

	@Override
	public void setGimbalAngleX(final double targetAngleX) {
		this.targetAngleX = targetAngleX;
	}

	@Override
	public void setGimbalAngleY(final double targetAngleY) {
		this.targetAngleY = targetAngleY;
	}

}
