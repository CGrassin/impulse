package fr.charleslabs.impulse.rocket;

import fr.charleslabs.impulse.gimbal.Gimbal;
import fr.charleslabs.impulse.motor.RocketMotor;
import fr.charleslabs.impulse.physics.PhysicalObject;
import fr.charleslabs.impulse.physics.PhysicsEngine;
import fr.charleslabs.impulse.physics.PhysicsVector;

/**
 * A Rocket implements a PhysicalObject to compute
 * the physics on it.
 * 
 * @author Charles Grassin
 *
 */
public class Rocket extends PhysicalObject {
	private final static String INVALID_COM_EXCEPTION = 
			"The center of mass (CoM) height must be > 0 and < rocket length.";
	/** A RocketMotor that generates thrust. */
	private RocketMotor rocketMotor;
	/** A gimbal that swivels the direction of thrust. */
	private Gimbal gimbal;
	// private double rocketLength; // in m
	/** The distance between the motor nozzle and the CoM, in m. */
	private double centerOfMassHeight;

	/**
	 * Constructs a Rocket with the following parameters:
	 * 
	 * @param rocketMotor A RocketMotor object.
	 * @param gimbal The thrust gimbal system.
	 * @param rocketMass
	 *            mass of the rocket, in kg
	 * @param rocketLength
	 *            distance from the thruster to the top of te rocket, in m
	 * @param centerOfMassHeigth
	 *            distance from the thruster to the center of mass, in m
	 * @throws Exception If the Rocket is invalid.
	 */
	public Rocket(final RocketMotor rocketMotor, final Gimbal gimbal,
			final double rocketMass, final double rocketLength,
			final double centerOfMassHeigth) throws Exception {
		super(rocketMass, new PhysicsVector(computeMomentOfInertia(
				rocketLength, rocketMass, centerOfMassHeigth),
				computeMomentOfInertia(rocketLength, rocketMass,
						centerOfMassHeigth), 1));
		if (rocketLength < centerOfMassHeigth)
			throw new Exception(INVALID_COM_EXCEPTION);
		this.rocketMotor = rocketMotor;
		this.gimbal = gimbal;
		// this.rocketLength = rocketLength;
		this.centerOfMassHeight = centerOfMassHeigth;
	}

	/**
	 * This function computes the moment of inertia along one axis of a linear
	 * rocket. The density is supposed to be axially symmetrical. However, the
	 * density can vary in the Z direction. This is taken into account with the
	 * centerOfMassHeight variable.
	 */
	static private double computeMomentOfInertia(final double rocketLength,
			final double rocketMass,final  double centerOfMassHeight) {
		double comRatio = centerOfMassHeight / rocketLength;
		return Math.pow(rocketLength, 2)
				* rocketMass
				/ 3
				* (Math.pow(comRatio, 3) + Math.pow(1 - comRatio, 4) / comRatio)
				/ (comRatio + Math.pow(1 - comRatio, 2) / comRatio);
	}

	@Override
	public void computeCinematics(final double currentT, final double deltaT) {

		if (gimbal != null)
			gimbal.compute(deltaT);
		super.computeCinematics(currentT, deltaT);
	}

	@Override
	public PhysicsVector computeTorque(final double currentT,final  double deltaT) {
		final double u = (gimbal!=null) ? Math.sqrt(1
				+ Math.pow(Math.tan(Math.toRadians(gimbal.getGimbalAngleX())),
						2)
				+ Math.pow(Math.tan(Math.toRadians(gimbal.getGimbalAngleY())),
						2)):1;
		final double thrust = (rocketMotor!=null)?rocketMotor.getThrust(currentT):0;

		return new PhysicsVector(centerOfMassHeight * thrust
				* tan(gimbal.getGimbalAngleX()) / u, centerOfMassHeight
				* thrust * tan(gimbal.getGimbalAngleY()) / u, 0);
	}

	@Override
	public PhysicsVector computeForce(final double currentT, final double deltaT) {
		final double u = (gimbal!=null) ? Math.sqrt(1
				+ Math.pow(Math.tan(Math.toRadians(gimbal.getGimbalAngleX())),
						2)
				+ Math.pow(Math.tan(Math.toRadians(gimbal.getGimbalAngleY())),
						2)):1;
		final double v = Math.sqrt(1
				+ Math.pow(Math.tan(Math.toRadians(angularMotion.position.x)),
						2)
				+ Math.pow(Math.tan(Math.toRadians(angularMotion.position.y)),
						2));
		final double thrust = (rocketMotor!=null)?rocketMotor.getThrust(currentT):0;

		return new PhysicsVector(
				thrust * tan(angularMotion.position.y) / u / v, thrust
						* tan(angularMotion.position.x) / u / v, thrust / u / v
						- PhysicsEngine.G_CONSTANT * mass);
	}

	@Override
	public void reset() {
		super.reset();
		if (gimbal != null)
			gimbal.reset();

	}

	@Override
	public void init() {
		this.linearMotion.position.z = 1;
		if (gimbal != null)
			gimbal.init();
	}

	private double tan(final double angle) {
		return Math.tan(Math.toRadians((Math.abs(angle) > 90) ? 180 - angle
				: angle));
	}

	public void loadFromFile() {
		// TODO : add rocket load from a file
	}

	// --- Getters and Setters ---
	public RocketMotor getRocketMotor() {
		return rocketMotor;
	}

	public Gimbal getGimbal() {
		return gimbal;
	}

	public void setRocketMotor(final RocketMotor rocketMotor) {
		this.rocketMotor = rocketMotor;
	}

	public void setGimbal(final Gimbal gimbal) {
		this.gimbal = gimbal;
	}
}
