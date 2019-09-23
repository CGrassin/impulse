package fr.charleslabs.impulse.rocket.sensor_unused;

import fr.charleslabs.impulse.rocket.Rocket;

public class RawAccelerometer extends Sensor {

	public RawAccelerometer() {
		super(Sensor.SensorTypeEnum.ACCELEROMETER, "Raw Accelerometer");
	}

	@Override
	public String measure(Rocket rocket) {
		return rocket.getLinearMotion().acceleration.x
				+ " "
				+ rocket.getLinearMotion().acceleration.y
				+ " "
				+ rocket.getLinearMotion().acceleration.z
				+ " ";
	}

}
