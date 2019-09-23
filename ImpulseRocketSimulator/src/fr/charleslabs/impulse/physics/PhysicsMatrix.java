package fr.charleslabs.impulse.physics;

/**
 * A 3-by-3 matrix to compute kinetics: 
 * acceleration (x,y,z), speed (x,y,z),
 * position (x,y,z).
 * May be used for both angular and linear
 * motions.
 * 
 * @author Charles Grassin
 *
 */
final public class PhysicsMatrix {
	public PhysicsVector acceleration = new PhysicsVector();
	public PhysicsVector speed = new PhysicsVector();
	public PhysicsVector position = new PhysicsVector();

	/**
	 * Reset the entire matrix:
	 * acceleration (0,0,0), speed (0,0,0),
	 * position (0,0,0).
	 */
	public void reset() {
		acceleration.reset();
		speed.reset();
		position.reset();
	}
}
