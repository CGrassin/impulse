package fr.charleslabs.impulse.physics;

/**
 * A simple three dimensional vector (x, y and z).
 * 
 * @author Charles Grassin
 *
 */
final public class PhysicsVector {
	public double x, y, z;
	
	/**
	 * Constructs a PhysicsVector, set to (0,0,0).
	 */
	public PhysicsVector() {}

	/**
	 * Constructs a PhysicsVector, set to (x,y,z).
	 */
	public PhysicsVector(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Copy constructor of the PhysicsVector: creates
	 * an identical vector.
	 * 
	 * @param vector The vector to copy.
	 */
	public PhysicsVector(final PhysicsVector vector) {
		x = vector.x;
		y = vector.y;
		z = vector.z;
	}

	/**
	 * Reset the vector to (0,0,0).
	 */
	public void reset() {
		x = 0;
		y = 0;
		z = 0;
	}
}
