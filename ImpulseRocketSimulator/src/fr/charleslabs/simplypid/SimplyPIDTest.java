package fr.charleslabs.simplypid;

public class SimplyPIDTest {

	public static void main(String[] args) throws InterruptedException {
		// Factor to add some overshoot
		double overshootFactor = 1.5;
		// 
		double output = 25;
		
		SimplyPID pid = new SimplyPID(0, 0.3, 0, 0.001);

		System.out.printf("SetPoint\tOutput\tError\n");

		for (int i = 0; i < 50; i++){
			// At 50%, change the setPoint
			if (i == 25)
				pid.setSetpoint(50);
			
			// Compute the output, and add it to our current value
			output += pid.getOutput(System.currentTimeMillis()/1000,output) * overshootFactor;
			
			// Print the current status
			System.out.printf("%3.2f\t%3.2f\t%3.2f\n", pid.getSetPoint(), output, (pid.getSetPoint()-output));
			
			// Add some error
			output += (Math.random()-.5)*5;
			
			
			Thread.sleep(100);
		}		
	}

}
