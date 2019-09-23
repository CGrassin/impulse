package fr.charleslabs.simplypid;

/**
 * A simple PID closed control loop example.
 * <br><br>
 * License : MIT
 * @author Charles Grassin
 */
public class SimplyPIDExample {

	public static void main(String[] args) {
		
		// 'output' represent the PID output (for instance, torque on a motor)
		double output = 0;
		
		// 'currentValue' represents the value measured
		double currentValue = 25;
		
		SimplyPID pid = new SimplyPID(0, 1.2, 0, 0.025);

		System.out.printf("Time\tSet Point\tCurrent value\tOutput\tError\n");

		for (int i = 0; i < 30; i++){
			// Print the current status
				System.out.printf("%d\t%3.2f\t%3.2f\t%3.2f\t%3.2f\n", i, pid.getSetPoint(), currentValue, output, (pid.getSetPoint()-currentValue));
						
			// At 50%, change the setPoint
			if (i == 15)
				pid.setSetpoint(50);
			
			// Compute the output (assuming 1 unit of time passed between each measurement)
			output = pid.getOutput(1,currentValue);
			
			// Add it to our current value (which would be the measurement in a true system), 
			// with some random error and an arbitrary overshoot factor
			currentValue += output*1.3 + (Math.random()-.5)*3;
		}		
	}

}
