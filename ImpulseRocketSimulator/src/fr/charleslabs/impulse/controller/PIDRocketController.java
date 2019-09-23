package fr.charleslabs.impulse.controller;

import fr.charleslabs.simplypid.SimplyPID;

/**
 * A RocketController that uses a PID controller
 * to compute the angle of the rocket's gimbal.
 * 
 * @author Charles Grassin
 *
 */
public class PIDRocketController extends RocketController {
	private SimplyPID pidX,pidY;
	
	/**
	 * Constructs the PID controllers (for X and Y) with 
	 * the parameters.
	 * 
	 * @param frequency The call frequency of the PID.
	 * @param kp The proportional gain coefficient.
	 * @param ki The integral gain coefficient.
	 * @param kd The derivative gain coefficient.
	 */
	public PIDRocketController(final double frequency,final double kp,final double ki,final double kd) {
		super(frequency);
		pidX = new SimplyPID(0,kp, ki, kd);
		pidY = new SimplyPID(0,kp, ki, kd);
	}

	@Override
	public void call(final double timeSinceIgnition) {
		if (rocket!= null && rocket.getGimbal() != null) {
			rocket.getGimbal().setGimbalAngleX(
					pidX.getOutput(timeSinceIgnition, rocket.getAngularMotion().position.x));
			rocket.getGimbal().setGimbalAngleY(
					pidY.getOutput(timeSinceIgnition, rocket.getAngularMotion().position.y));
		}
	}
	
	@Override
	public void reset() {
		super.reset();
		pidX.reset();
		pidY.reset();
	}
}
