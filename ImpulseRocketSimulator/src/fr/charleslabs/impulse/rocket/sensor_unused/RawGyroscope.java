package fr.charleslabs.impulse.rocket.sensor_unused;

import fr.charleslabs.impulse.rocket.Rocket;

public class RawGyroscope extends Sensor {

	public RawGyroscope() {
		super(Sensor.SensorTypeEnum.GYROSCOPE, "Raw Gyroscope");
	}

	@Override
	public String measure(Rocket rocket) {
		return rocket.getAngularMotion().speed.x
				+ " "
				+ rocket.getAngularMotion().speed.y
				+ " "
				+ rocket.getAngularMotion().speed.z
				+ " ";
	}

}
