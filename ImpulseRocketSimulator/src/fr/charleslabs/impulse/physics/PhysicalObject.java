package fr.charleslabs.impulse.physics;

/**
 * A PhysicalObject is an abstract object on which
 * physics forces and torques may be computed. It has
 * 6 degrees of freedom, and has two physics matrix 
 * that describes its kinetics: angular and linear motion,
 * for acceleration, speed and position, of X, Y and Z.
 * 
 * @author Charles Grassin
 */
public abstract class PhysicalObject {
	private final static String INVALID_MASS_EXCEPTION = "Mass must be > 0";
	/** The mass of the physical object, in kg */
	protected double mass;
	
	/** The J_delta matrix (x,y,z) in kg.m^2 */
	protected PhysicsVector momentOfInertia;
	
	/** The linear motion position, speed and acceleration matrix. */
	protected PhysicsMatrix linearMotion = new PhysicsMatrix();
	
	/** The angular motion position, speed and acceleration matrix. */
	protected PhysicsMatrix angularMotion = new PhysicsMatrix();

	/**
	 * Constructs a physical object with a given mass and momentOfInertia
	 * matrix.
	 * 
	 * @param mass The mass of the physical object, in kg.
	 * @param momentOfInertia The 3-dimensionnal momentOfInertia of the 
	 * physical object, in kg.m^2.
	 */
	public PhysicalObject(final double mass, final PhysicsVector momentOfInertia) throws Exception{
		if (mass <= 0)
			throw new Exception(INVALID_MASS_EXCEPTION);
		this.mass = mass;
		this.momentOfInertia = momentOfInertia;
	}

	/**
	 * Returns a Vector with the torque
	 * that apply on the rocket, in N.
	 * 
	 * @param currentT The current time since epoch.
	 * @param deltaT The time since last call.
	 * @return 
	 * 		A three-dimensionnal vector, containing
	 * the torques. All the forces must be multiplied
	 * by the distance between the point of application
	 * and the center of mass of the object.
	 */
	public abstract PhysicsVector computeTorque(final double currentT,
			final double deltaT);

	/**
	 * Returns a Vector with the forces
	 * that apply on the rocket, in N.
	 * 
	 * @param currentT The current time since epoch.
	 * @param deltaT The time since last call.
	 * @return 
	 * 		A three-dimensionnal vector, containing
	 * the forces (x, y and z).
	 */
	public abstract PhysicsVector computeForce(final double currentT,
			final double deltaT);

	/**
	 * Computes the cinematics of the object: angular and linear position, speed
	 * and position. To be called by the the physics engine.
	 * 
	 * @param currentT
	 *            The current time since epoch (motor ignition).
	 * @param deltaT
	 *            Time elapsed since last time cinematics where calculated.
	 */
	public void computeCinematics(final double currentT, final double deltaT) {
		PhysicsVector force = computeForce(currentT, deltaT);
		PhysicsVector torque = computeTorque(currentT, deltaT);

		angularMotion.acceleration.x = torque.x / momentOfInertia.x;
		angularMotion.speed.x += angularMotion.acceleration.x * deltaT;
		angularMotion.position.x = normalizeAngle(angularMotion.position.x
				+ angularMotion.speed.x * deltaT);

		angularMotion.acceleration.y = torque.y / momentOfInertia.y;
		angularMotion.speed.y += angularMotion.acceleration.y * deltaT;
		angularMotion.position.y = normalizeAngle(angularMotion.position.y
				+ angularMotion.speed.y * deltaT);

		angularMotion.acceleration.z = torque.z / momentOfInertia.z;
		angularMotion.speed.z += angularMotion.acceleration.z * deltaT;
		angularMotion.position.z = normalizeAngle(angularMotion.position.z
				+ angularMotion.speed.z * deltaT);

		linearMotion.acceleration.x = force.x / mass;
		linearMotion.speed.x += linearMotion.acceleration.x * deltaT;
		linearMotion.position.x += linearMotion.speed.x * deltaT;

		linearMotion.acceleration.y = force.y / mass;
		linearMotion.speed.y += linearMotion.acceleration.y * deltaT;
		linearMotion.position.y += linearMotion.speed.y * deltaT;

		linearMotion.acceleration.z = force.z / mass;
		linearMotion.speed.z += linearMotion.acceleration.z * deltaT;
		linearMotion.position.z += linearMotion.speed.z * deltaT;
	}

	/**
	 * Called on simulation start-up.
	 */
	public abstract void init();

	/**
	 * Resets the position and angle of the object.
	 */
	public void reset() {
		this.angularMotion.reset();
		this.linearMotion.reset();
	}
	
	/**
	 * Normalize an angle to belong to the interval
	 * [-180;180].
	 * @param angle The angle to normalize, in degrees.
	 * @return The normalized angle, in degrees.
	 */
	final private double normalizeAngle(final double angle) {
		return angle - Math.floor((angle + 179.0) / 360) * 360;
	}

	/**
	 * Checks whether the object has touched the ground.
	 * 
	 * @return a boolean : true if object landed
	 */
	public boolean isLanded() {
		return linearMotion.position.z < 0;
	}

	// --- Getters and Setters ---
	public PhysicsMatrix getLinearMotion() {
		return linearMotion;
	}

	public PhysicsMatrix getAngularMotion() {
		return angularMotion;
	}

	protected PhysicsVector getMomentOfInertia() {
		return momentOfInertia;
	}

	protected void setMomentOfInertia(final PhysicsVector momentOfInertia) {
		this.momentOfInertia = momentOfInertia;
	}

	protected double getMass() {
		return mass;
	}

	protected void setMass(final double mass) {
		this.mass = mass;
	}
}
