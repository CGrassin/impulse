package fr.charleslabs.impulse.motor;

/**
 * This class represents a SRB (solid rocket
 * booster). It delivers a thrust described
 * by its thrust curve. The thrust only depend
 * of the current time.
 * <br>
 * The thrust will be interpolated with a <a
 * href="https://en.wikipedia.org/wiki/Linear_interpolation">
 * linear interpolation</a>.
 * 
 * @author Charles Grassin
 *
 */
public class SolidFuelMotor extends RocketMotor {
	/** Thrust table, in N */
	double thrust[];
	/** Total burn time, in s */
	double burnTime; //

	/**
	 * Constructor with a variable thrust during the burn time of the motor. The
	 * thrust will be interpolated with a <a
	 * href="https://en.wikipedia.org/wiki/Linear_interpolation">linear
	 * interpolation</a>.
	 * 
	 * @param thrust
	 *            The thrust waveform of the motor. The time interval between
	 *            the points is 'number of points'/'burn time'.
	 * @param burnTime
	 *            The burn time of the motor.
	 */
	public SolidFuelMotor(final double[] thrust, final double burnTime) {
		super();
		this.thrust = thrust;
		this.burnTime = burnTime;
	}

	/**
	 * Constructor with a constant thrust during the burn time of the motor.
	 * There will be not interpolation.
	 * 
	 * @param thrust
	 *            The constant thrust of the motor.
	 * @param burnTime
	 *            The burn time of the motor.
	 */
	public SolidFuelMotor(final double thrust, final double burnTime) {
		super();
		this.thrust = new double[] { thrust };
		this.burnTime = burnTime;
	}

	@Override
	public double getThrust(final double currentTime) {
		final int index = (int) (currentTime * thrust.length / burnTime);

		// Computation of thrust, with linear interpolation
		// --> Motor ran out of fuel
		if (currentTime >= burnTime)
			return 0;
		// --> Motor is running (interpolated)
		else if (index < thrust.length - 1)
			return (thrust[index + 1] - thrust[index])
					* (currentTime * thrust.length / burnTime - index)
					+ thrust[index];
		// --> Motor is running out of fuel (interpolated if there are more than
		// one point in the thrust array)
		else if (thrust.length > 1)
			return thrust[index]
					* (1 - (currentTime * thrust.length / burnTime - index));
		// --> Motor is running out of fuel (NOT interpolated if there is only a
		// constant trust defined)
		else
			return thrust[index];

	}
}
